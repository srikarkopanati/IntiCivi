package com.example.inticivi.config;

import com.example.inticivi.entity.Complaint;
import com.example.inticivi.entity.Department;
import com.example.inticivi.entity.Role;
import com.example.inticivi.entity.User;
import com.example.inticivi.repository.ComplaintRepository;
import com.example.inticivi.repository.DepartmentRepository;
import com.example.inticivi.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ComplaintRepository complaintRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, ComplaintRepository complaintRepository, 
                     DepartmentRepository departmentRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.complaintRepository = complaintRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            return;
        }

        loadUsers();
        loadDepartments();
        loadComplaints();
        
        System.out.println("MongoDB sample data loaded successfully!");
    }

    private void loadUsers() {
        User admin = new User("Administrator", "admin@inticivi.com", passwordEncoder.encode("admin123"), Role.ADMIN);
        User user = new User("Demo User", "user@inticivi.com", passwordEncoder.encode("password"), Role.USER);
        userRepository.saveAll(List.of(admin, user));
        System.out.println("Loaded: 1 admin user + 1 normal user");
    }

    private void loadDepartments() {
        List<Department> departments = new ArrayList<>();
        departments.add(new Department("Public Works", "Roads and Infrastructure", "pw@gov.in", "1800-123-4567", "Downtown", "Infrastructure"));
        departments.add(new Department("Sanitation", "Waste Management", "sanitation@gov.in", "1800-234-5678", "Central", "Sanitation"));
        departments.add(new Department("Water Supply", "Water and Sewerage", "water@gov.in", "1800-345-6789", "North Zone", "Water"));
        departments.add(new Department("Electrical", "Power and Electricity", "electrical@gov.in", "1800-456-7890", "South Zone", "Electricity"));
        departments.add(new Department("Public Safety", "Police and Security", "police@gov.in", "1800-567-8901", "Downtown", "Safety"));

        departmentRepository.saveAll(departments);
        System.out.println("Loaded: 5 departments");
    }

    private void loadComplaints() {
        User admin = userRepository.findByEmail("admin@inticivi.com").orElse(null);
        User user = userRepository.findByEmail("user@inticivi.com").orElse(null);

        if (admin == null || user == null) {
            return;
        }

        List<Complaint> complaints = new ArrayList<>();
        
        Complaint c1 = new Complaint("Broken Streetlight on Main Road", 
            "The streetlight near the central market has been broken for 3 weeks. It's creating safety concerns.",
            "Infrastructure", "Main Road Central Market", "560001", 12.9716, 77.5946,
            "https://example.com/images/complaint1.jpg", "PENDING", user.getId());
        c1.setCreatedAt(LocalDateTime.now().minusDays(10));
        c1.setUpdatedAt(LocalDateTime.now().minusDays(10));
        c1.setPriorityScore(8.5);
        complaints.add(c1);

        Complaint c2 = new Complaint("Potholes on Highway",
            "Multiple potholes have damaged vehicle alignments. Immediate repairs needed.",
            "Infrastructure", "Highway North Bypass", "560002", 13.0245, 77.6100,
            "https://example.com/images/complaint2.jpg", "IN_PROGRESS", user.getId());
        c2.setCreatedAt(LocalDateTime.now().minusDays(9));
        c2.setUpdatedAt(LocalDateTime.now().minusDays(5));
        c2.setPriorityScore(7.8);
        complaints.add(c2);

        Complaint c3 = new Complaint("Water Supply Disruption",
            "We have been without water for 5 days. Need immediate action.",
            "Water", "Residential Complex B", "560003", 12.9450, 77.5790,
            "https://example.com/images/complaint3.jpg", "PENDING", user.getId());
        c3.setCreatedAt(LocalDateTime.now().minusDays(8));
        c3.setUpdatedAt(LocalDateTime.now().minusDays(8));
        c3.setPriorityScore(9.2);
        complaints.add(c3);

        Complaint c4 = new Complaint("Illegal Dumping at Park",
            "Large amount of construction waste illegally dumped near Green Park.",
            "Sanitation", "Green Park Area", "560004", 12.9580, 77.6050,
            "https://example.com/images/complaint4.jpg", "PENDING", admin.getId());
        c4.setCreatedAt(LocalDateTime.now().minusDays(7));
        c4.setUpdatedAt(LocalDateTime.now().minusDays(7));
        c4.setPriorityScore(8.1);
        complaints.add(c4);

        Complaint c5 = new Complaint("Frequent Power Cuts",
            "Power cuts every evening between 5-7 PM affecting work from home.",
            "Electricity", "Tech Park Zone", "560005", 13.0120, 77.5900,
            "https://example.com/images/complaint5.jpg", "RESOLVED", user.getId());
        c5.setCreatedAt(LocalDateTime.now().minusDays(6));
        c5.setUpdatedAt(LocalDateTime.now().minusDays(2));
        c5.setPriorityScore(6.5);
        complaints.add(c5);

        Complaint c6 = new Complaint("Blocked Drainage System",
            "Drainage overflowing causing water to stagnate on streets.",
            "Infrastructure", "Downtown Area", "560001", 12.9650, 77.5980,
            "https://example.com/images/complaint6.jpg", "IN_PROGRESS", user.getId());
        c6.setCreatedAt(LocalDateTime.now().minusDays(5));
        c6.setUpdatedAt(LocalDateTime.now().minusDays(3));
        c6.setPriorityScore(7.9);
        complaints.add(c6);

        Complaint c7 = new Complaint("Street Cleaning Not Done",
            "Street has not been cleaned for weeks, garbage accumulating.",
            "Sanitation", "Market Lane", "560002", 12.9700, 77.6000,
            "https://example.com/images/complaint7.jpg", "PENDING", user.getId());
        c7.setCreatedAt(LocalDateTime.now().minusDays(4));
        c7.setUpdatedAt(LocalDateTime.now().minusDays(4));
        c7.setPriorityScore(7.2);
        complaints.add(c7);

        Complaint c8 = new Complaint("Traffic Signal Malfunction",
            "Traffic signal at South Road junction not working, causing traffic jams.",
            "Infrastructure", "South Road Junction", "560006", 12.9400, 77.5850,
            "https://example.com/images/complaint8.jpg", "PENDING", admin.getId());
        c8.setCreatedAt(LocalDateTime.now().minusDays(3));
        c8.setUpdatedAt(LocalDateTime.now().minusDays(3));
        c8.setPriorityScore(8.3);
        complaints.add(c8);

        Complaint c9 = new Complaint("Damaged Bus Stop Shelter",
            "Bus stop shelter roof damaged, people getting wet during rain.",
            "Infrastructure", "Central Bus Stand", "560001", 12.9720, 77.5950,
            "https://example.com/images/complaint9.jpg", "IN_PROGRESS", user.getId());
        c9.setCreatedAt(LocalDateTime.now().minusDays(2));
        c9.setUpdatedAt(LocalDateTime.now().minusDays(1));
        c9.setPriorityScore(7.4);
        complaints.add(c9);

        Complaint c10 = new Complaint("Overflowing Garbage Bins",
            "Public garbage bins at market area are overflowing, causing bad smell.",
            "Sanitation", "Public Market", "560007", 12.9750, 77.5920,
            "https://example.com/images/complaint10.jpg", "PENDING", user.getId());
        c10.setCreatedAt(LocalDateTime.now().minusDays(1));
        c10.setUpdatedAt(LocalDateTime.now().minusDays(1));
        c10.setPriorityScore(6.8);
        complaints.add(c10);

        complaintRepository.saveAll(complaints);
        System.out.println("Loaded: 10 sample complaints");
    }
}
