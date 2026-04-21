package com.example.inticivi.service;

import com.example.inticivi.entity.Complaint;
import com.example.inticivi.util.HighTrafficLocationUtil;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ComplaintPriorityService {

    private static final Map<String, Integer> SEVERITY_SCORE = Map.of(
            "critical", 5,
            "high", 4,
            "medium", 3,
            "low", 2,
            "minor", 1
    );

    public double calculatePriority(Complaint complaint, long nearbySimilarCount) {
        int severity = SEVERITY_SCORE.getOrDefault(complaint.getCategory().toLowerCase(), 3);
        double nearbyImpact = Math.min(nearbySimilarCount, 8) * 1.2;
        double ageFactor = calculateAgeFactor(complaint.getCreatedAt());
        double trafficBonus = HighTrafficLocationUtil.isHighTrafficArea(complaint.getLocation()) ? 1.8 : 0;
        double score = severity * 2.5 + nearbyImpact + ageFactor + trafficBonus;
        return Math.round(Math.min(score, 10.0) * 10.0) / 10.0;
    }

    private double calculateAgeFactor(LocalDateTime createdAt) {
        if (createdAt == null) {
            return 0.5;
        }
        long days = Duration.between(createdAt, LocalDateTime.now()).toDays();
        if (days >= 30) {
            return 2.5;
        }
        return Math.round((days / 15.0 + 0.5) * 10.0) / 10.0;
    }
}
