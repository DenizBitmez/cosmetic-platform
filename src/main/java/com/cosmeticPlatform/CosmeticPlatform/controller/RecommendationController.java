package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;
import com.cosmeticPlatform.CosmeticPlatform.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get personalized product recommendations
     * GET /api/recommendations/personalized?userId={userId}&limit={limit}
     */
    @GetMapping("/personalized")
    public ResponseEntity<List<Product>> getPersonalizedRecommendations(
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "12") int limit) {
        try {
            List<Product> recommendations = recommendationService.getPersonalizedRecommendations(userId, limit);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            System.err.println("Error getting recommendations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get similar products
     * GET /api/recommendations/similar/{productId}?limit={limit}
     */
    @GetMapping("/similar/{productId}")
    public ResponseEntity<List<Product>> getSimilarProducts(
            @PathVariable Integer productId,
            @RequestParam(defaultValue = "6") int limit) {
        try {
            List<Product> similar = recommendationService.getSimilarProducts(productId, limit);
            return ResponseEntity.ok(similar);
        } catch (Exception e) {
            System.err.println("Error getting similar products: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Save user skin profile (quiz results)
     * POST /api/recommendations/skin-profile
     */
    @PostMapping("/skin-profile")
    public ResponseEntity<?> saveSkinProfile(
            @RequestParam Integer userId,
            @RequestParam String skinType,
            @RequestParam(required = false) String skinConcerns,
            @RequestParam(required = false) String skinTone,
            @RequestParam(required = false) Integer age) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            User user = userOpt.get();
            user.setSkinType(skinType);
            user.setSkinConcerns(skinConcerns);
            user.setSkinTone(skinTone);
            user.setAge(age);

            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Skin profile saved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error saving skin profile: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to save skin profile");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get user skin profile
     * GET /api/recommendations/skin-profile/{userId}
     */
    @GetMapping("/skin-profile/{userId}")
    public ResponseEntity<?> getSkinProfile(@PathVariable Integer userId) {
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            User user = userOpt.get();
            Map<String, Object> profile = new HashMap<>();
            profile.put("skinType", user.getSkinType());
            profile.put("skinConcerns", user.getSkinConcerns());
            profile.put("skinTone", user.getSkinTone());
            profile.put("age", user.getAge());
            profile.put("hasCompletedQuiz", user.getSkinType() != null);

            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            System.err.println("Error getting skin profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
