package com.cosmeticPlatform.CosmeticPlatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "price_alert")
@Getter
@Setter
public class PriceAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer userId;

    // Product ID - nullable for external products
    private Integer productId;

    // External product support (for products from external APIs)
    private String externalProductName;

    @Column(length = 500)
    private String externalProductImage;

    private String externalProductBrand;

    @Column(nullable = false)
    private Double targetPrice;

    private Double currentPrice;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    private LocalDateTime notifiedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
    }
}
