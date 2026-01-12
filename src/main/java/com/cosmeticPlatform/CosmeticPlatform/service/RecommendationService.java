package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get personalized product recommendations based on user's skin profile
     */
    public List<Product> getPersonalizedRecommendations(Integer userId, int limit) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Collections.emptyList();
        }

        User user = userOpt.get();

        // If user hasn't completed skin quiz, return popular products
        if (user.getSkinType() == null) {
            return getPopularProducts(limit);
        }

        List<Product> allProducts = productRepository.findAll();

        // Score each product based on user profile
        Map<Product, Integer> productScores = new HashMap<>();
        for (Product product : allProducts) {
            int score = calculateProductScore(product, user);
            if (score >= 20) { // Lowered threshold from 50 to 20
                productScores.put(product, score);
            }
        }

        // Sort by score and return top N
        List<Product> recommendations = productScores.entrySet().stream()
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // If no recommendations, fallback to popular products
        if (recommendations.isEmpty()) {
            System.out.println("No scored recommendations, falling back to popular products");
            return getPopularProducts(limit);
        }

        System.out.println("Returning " + recommendations.size() + " personalized recommendations");
        return recommendations;
    }

    /**
     * Calculate product score based on user profile
     * Score range: 0-100
     */
    private int calculateProductScore(Product product, User user) {
        int score = 0;

        // Base score for all products
        score += 10;

        // Skin type match (40 points)
        if (matchesSkinType(product, user.getSkinType())) {
            score += 40;
        }

        // Skin concerns match (up to 30 points)
        score += calculateConcernMatch(product, user.getSkinConcerns());

        // No allergens (20 points)
        if (!containsAllergens(product, user.getAllergies())) {
            score += 20;
        }

        // Stock availability bonus (10 points)
        if (product.getStock() > 0) {
            score += 10;
        }

        // Age-appropriate bonus (5 points)
        if (isAgeAppropriate(product, user.getAge())) {
            score += 5;
        }

        return score;
    }

    /**
     * Check if product matches user's skin type
     */
    private boolean matchesSkinType(Product product, String skinType) {
        if (skinType == null || product.getCategory() == null) {
            return false;
        }

        String category = product.getCategory().toLowerCase();
        String type = skinType.toLowerCase();

        // Simple keyword matching - can be enhanced with product tags
        return category.contains(type) ||
                category.contains("all skin types") ||
                category.contains("universal");
    }

    /**
     * Calculate concern match score (0-30 points)
     */
    private int calculateConcernMatch(Product product, String skinConcerns) {
        if (skinConcerns == null || skinConcerns.isEmpty()) {
            return 0;
        }

        int matchCount = 0;
        String[] concerns = skinConcerns.replace("[", "").replace("]", "")
                .replace("\"", "").split(",");

        for (String concern : concerns) {
            if (product.getCategory() != null &&
                    product.getCategory().toLowerCase().contains(concern.trim().toLowerCase())) {
                matchCount++;
            }
        }

        return Math.min(matchCount * 10, 30); // Max 30 points
    }

    /**
     * Check if product contains user allergens
     */
    private boolean containsAllergens(Product product, String allergies) {
        if (allergies == null || allergies.isEmpty()) {
            return false;
        }

        // This would need to check product ingredients
        // For now, return false (no allergen check)
        // TODO: Implement ingredient checking when ingredient data is available
        return false;
    }

    /**
     * Check if product is age-appropriate
     */
    private boolean isAgeAppropriate(Product product, Integer age) {
        if (age == null) {
            return true;
        }

        // Simple age-based logic
        // Anti-aging products for 25+
        // Acne products for under 30
        // This can be enhanced with product metadata
        return true; // For now, all products are age-appropriate
    }

    /**
     * Get popular products as fallback
     */
    private List<Product> getPopularProducts(int limit) {
        List<Product> allProducts = productRepository.findAll();

        return allProducts.stream()
                .filter(p -> p.getStock() > 0)
                .sorted(Comparator.comparing(Product::getStock).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get similar products based on category and type
     */
    public List<Product> getSimilarProducts(Integer productId, int limit) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Product targetProduct = productOpt.get();
        List<Product> allProducts = productRepository.findAll();

        return allProducts.stream()
                .filter(p -> p.getId() != productId)
                .filter(p -> p.getCategory() != null &&
                        targetProduct.getCategory() != null &&
                        p.getCategory().equalsIgnoreCase(targetProduct.getCategory()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
