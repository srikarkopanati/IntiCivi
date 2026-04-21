package com.example.inticivi.service;

import com.example.inticivi.entity.Complaint;
import com.example.inticivi.exception.ResourceNotFoundException;
import com.example.inticivi.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintPriorityService priorityService;

    public Complaint createComplaint(Complaint complaint) {
        if (complaint.getCreatedAt() == null) {
            complaint.setCreatedAt(LocalDateTime.now());
        }
        complaint.setUpdatedAt(LocalDateTime.now());
        long nearbyCount = complaintRepository.countByCategoryAndPincode(complaint.getCategory(), complaint.getPincode());
        complaint.setPriorityScore(priorityService.calculatePriority(complaint, nearbyCount));
        return complaintRepository.save(complaint);
    }

    public List<Complaint> getComplaintsByCreator(String createdByUserId) {
        return complaintRepository.findByCreatedByUserIdOrderByCreatedAtDesc(createdByUserId);
    }

    public Optional<Complaint> getComplaintById(String id) {
        return complaintRepository.findById(id);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public Complaint updateComplaintStatus(String id, String status) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id " + id));
        complaint.setStatus(status);
        complaint.setUpdatedAt(LocalDateTime.now());
        return complaintRepository.save(complaint);
    }

    public void deleteComplaint(String id) {
        if (!complaintRepository.existsById(id)) {
            throw new ResourceNotFoundException("Complaint not found with id " + id);
        }
        complaintRepository.deleteById(id);
    }
}
