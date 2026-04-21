package com.example.inticivi.repository;

import com.example.inticivi.entity.Complaint;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ComplaintRepository extends MongoRepository<Complaint, String> {
    List<Complaint> findByCreatedByUserIdOrderByCreatedAtDesc(String createdByUserId);
    long countByCategoryAndPincode(String category, String pincode);
}
