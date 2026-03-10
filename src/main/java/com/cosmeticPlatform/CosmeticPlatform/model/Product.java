package com.cosmeticPlatform.CosmeticPlatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String category;

    private double price;

    private int stock;

    private Integer paoMonths; // Period After Opening in months

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_ingredients", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> ingredients;

    // @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    // private List<Comment> comments;
    // private List<Rating> ratings;

    private int photoCount = 0;

    // Sustainability Metrics
    private Boolean isVegan = false;

    public Boolean getIsVegan() {
        if (this.ingredients != null && !this.ingredients.isEmpty()) {
            for (Ingredient ingredient : this.ingredients) {
                if (Boolean.FALSE.equals(ingredient.getIsVegan())) {
                    return false;
                }
            }
            return true;
        }
        return this.isVegan;
    }

    private Boolean isCrueltyFree = false;
    private Integer ecoPackagingScore = 5; // 1 to 10
    private String carbonFootprintRating = "C"; // A, B, C, D, E

    public Integer getOverallSustainabilityScore() {
        int score = 0;
        if (Boolean.TRUE.equals(getIsVegan()))
            score += 20;
        if (Boolean.TRUE.equals(getIsCrueltyFree()))
            score += 20;

        if (ecoPackagingScore != null) {
            score += (ecoPackagingScore * 3);
        }

        // Carbon rating max 30 points
        if (carbonFootprintRating != null) {
            switch (carbonFootprintRating.toUpperCase()) {
                case "A":
                    score += 30;
                    break;
                case "B":
                    score += 24;
                    break;
                case "C":
                    score += 18;
                    break;
                case "D":
                    score += 10;
                    break;
                case "E":
                    score += 0;
                    break;
            }
        }
        return score;
    }
}
