package com.example.inticivi.util;

import java.util.Set;

public class HighTrafficLocationUtil {

    private static final Set<String> HIGH_TRAFFIC_KEYWORDS = Set.of(
            "downtown", "central", "market", "station", "airport", "mall", "highway", "main road", "metro"
    );

    public static boolean isHighTrafficArea(String location) {
        if (location == null) {
            return false;
        }
        String lowerCase = location.toLowerCase();
        return HIGH_TRAFFIC_KEYWORDS.stream().anyMatch(lowerCase::contains);
    }
}
