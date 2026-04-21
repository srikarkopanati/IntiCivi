package com.example.inticivi.config;

import com.example.inticivi.entity.Complaint;
import com.example.inticivi.entity.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

@Configuration
public class MongoIndexConfig implements ApplicationRunner {

    private final MongoTemplate mongoTemplate;

    public MongoIndexConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createUserIndexes();
        createComplaintIndexes();
    }

    private void createUserIndexes() {
        IndexOperations indexOps = mongoTemplate.indexOps(User.class);
        indexOps.ensureIndex(new Index().on("email", Sort.Direction.ASC).unique());
    }

    private void createComplaintIndexes() {
        IndexOperations indexOps = mongoTemplate.indexOps(Complaint.class);
        indexOps.ensureIndex(new Index().on("status", Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("pincode", Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("createdByUserId", Sort.Direction.ASC));
        indexOps.ensureIndex(new Index().on("createdAt", Sort.Direction.ASC));
    }
}