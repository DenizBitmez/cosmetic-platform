package com.cosmeticPlatform.CosmeticPlatform.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CombinationGuideService {

    private static final Map<String, List<String>> INCOMPATIBLE_PAIRS = new HashMap<>();

    static {
        // Source: General Dermatological Guidelines
        INCOMPATIBLE_PAIRS.put("Retinol", List.of("Vitamin C", "Benzoyl Peroxide", "Salicylic Acid"));
        INCOMPATIBLE_PAIRS.put("Vitamin C", List.of("Retinol", "AHAs", "BHAs", "Niacinamide"));
        INCOMPATIBLE_PAIRS.put("Niacinamide", List.of("Vitamin C"));
        INCOMPATIBLE_PAIRS.put("AHAs", List.of("Retinol", "Vitamin C"));
        INCOMPATIBLE_PAIRS.put("Salicylic Acid", List.of("Retinol"));
    }

    public List<String> checkCompatibility(List<String> ingredients) {
        List<String> warnings = new ArrayList<>();

        for (int i = 0; i < ingredients.size(); i++) {
            for (int j = i + 1; j < ingredients.size(); j++) {
                String ing1 = ingredients.get(i);
                String ing2 = ingredients.get(j);

                if (isConflict(ing1, ing2)) {
                    warnings.add("Conflict detected: " + ing1 + " and " + ing2
                            + " should not be used together in the same routine.");
                }
            }
        }
        return warnings;
    }

    private boolean isConflict(String ing1, String ing2) {
        for (Map.Entry<String, List<String>> entry : INCOMPATIBLE_PAIRS.entrySet()) {
            String primary = entry.getKey();
            if (containsIgnoreCase(ing1, primary)) {
                for (String conflicting : entry.getValue()) {
                    if (containsIgnoreCase(ing2, conflicting))
                        return true;
                }
            }
            // Check reverse
            if (containsIgnoreCase(ing2, primary)) {
                for (String conflicting : entry.getValue()) {
                    if (containsIgnoreCase(ing1, conflicting))
                        return true;
                }
            }
        }
        return false;
    }

    private boolean containsIgnoreCase(String source, String target) {
        if (source == null || target == null)
            return false;
        return source.toLowerCase().contains(target.toLowerCase());
    }
}
