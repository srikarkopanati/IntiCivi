package com.example.inticivi.controller;

import com.example.inticivi.dto.ComplaintRequest;
import com.example.inticivi.dto.ComplaintResponse;
import com.example.inticivi.dto.StatusUpdateRequest;
import com.example.inticivi.entity.Complaint;
import com.example.inticivi.service.AuthService;
import com.example.inticivi.service.ComplaintService;
import com.example.inticivi.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "http://localhost:5173")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<ComplaintResponse> createComplaint(@Valid @RequestBody ComplaintRequest request,
                                                              Authentication authentication) {
        String email = authentication.getName();
        User user = authService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Complaint complaint = new Complaint(
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getLocation(),
                request.getPincode(),
                request.getLatitude(),
                request.getLongitude(),
                request.getImageUrl(),
                "PENDING",
                user.getId()
        );
        complaint.setCreatedAt(LocalDateTime.now());
        complaint.setUpdatedAt(LocalDateTime.now());
        Complaint saved = complaintService.createComplaint(complaint);
        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ComplaintResponse>> getMyComplaints(Authentication authentication) {
        String email = authentication.getName();
        User user = authService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        List<Complaint> complaints = complaintService.getComplaintsByCreator(user.getId());
        return ResponseEntity.ok(complaints.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getComplaintById(@PathVariable String id, Authentication authentication) {
        String email = authentication.getName();
        User user = authService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Complaint complaint = complaintService.getComplaintById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        if (!complaint.getCreatedByUserId().equals(user.getId()) && !authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(toResponse(complaint));
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ComplaintResponse>> getAllComplaints() {
        List<Complaint> complaints = complaintService.getAllComplaints();
        return ResponseEntity.ok(complaints.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @PutMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComplaintResponse> updateStatus(@PathVariable String id,
                                                          @Valid @RequestBody StatusUpdateRequest request) {
        Complaint updated = complaintService.updateComplaintStatus(id, request.getStatus());
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaint(@PathVariable String id, Authentication authentication) {
        Complaint complaint = complaintService.getComplaintById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        String email = authentication.getName();
        User user = authService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (!complaint.getCreatedByUserId().equals(user.getId()) && !isAdmin) {
            return ResponseEntity.status(403).build();
        }
        complaintService.deleteComplaint(id);
        return ResponseEntity.noContent().build();
    }

    private ComplaintResponse toResponse(Complaint complaint) {
        return new ComplaintResponse(
                complaint.getId(),
                complaint.getTitle(),
                complaint.getDescription(),
                complaint.getCategory(),
                complaint.getLocation(),
                complaint.getPincode(),
                complaint.getLatitude(),
                complaint.getLongitude(),
                complaint.getImageUrl(),
                complaint.getPriorityScore(),
                complaint.getStatus(),
                complaint.getCreatedAt(),
                complaint.getUpdatedAt(),
                complaint.getCreatedByUserId()
        );
    }
}
