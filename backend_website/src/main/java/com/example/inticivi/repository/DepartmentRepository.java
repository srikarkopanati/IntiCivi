package com.example.inticivi.repository;

import com.example.inticivi.entity.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends MongoRepository<Department, String> {
    Optional<Department> findByName(String name);
    List<Department> findByCategory(String category);
}
