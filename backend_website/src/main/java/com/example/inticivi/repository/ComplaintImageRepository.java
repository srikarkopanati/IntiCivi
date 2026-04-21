package com.example.inticivi.repository;

import com.example.inticivi.entity.ComplaintImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ComplaintImageRepository extends MongoRepository<ComplaintImage, String> {
    List<ComplaintImage> findByComplaintId(String complaintId);
    void deleteByComplaintId(String complaintId);
}
