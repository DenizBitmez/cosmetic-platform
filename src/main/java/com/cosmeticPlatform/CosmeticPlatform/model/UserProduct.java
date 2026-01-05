package com.cosmeticPlatform.CosmeticPlatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "user_product")
@Getter
@Setter
public class UserProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String customName; // If user adds a product manually
    private String brand;

    private LocalDate openedDate;
    private LocalDate expiryDate;

    private Integer paoMonths;

    private boolean isExpired;

    @Column(columnDefinition = "TEXT")
    private String ingredientsText; // For manually entered products

    @Column(columnDefinition = "TEXT")
    private String imageUrl;
}
