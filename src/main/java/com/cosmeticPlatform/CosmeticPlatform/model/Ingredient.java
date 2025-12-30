package com.cosmeticPlatform.CosmeticPlatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ingredient")
@Getter
@Setter
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inci_name", nullable = false)
    private String inciName;

    @Column(name = "turkish_name")
    private String turkishName;

    @Column(columnDefinition = "TEXT")
    private String description;

    // "İyi", "Çok İyi", "Orta", "Riskli"
    private String score;
}
