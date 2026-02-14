package com.cosmeticPlatform.CosmeticPlatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Standardized INCI name

    @Column(columnDefinition = "TEXT")
    private String description;

    private String functionality; // e.g., "Preservative", "Humectant"

    private int safetyRating; // 1 (Safe) - 10 (Hazardous)

    private int comedogenicRating; // 0 (Non-clogging) - 5 (High clogging)

    @Column(columnDefinition = "TEXT")
    private String benefits;

    private String alertType; // "safe", "warning", "danger" for UI coloring
}
