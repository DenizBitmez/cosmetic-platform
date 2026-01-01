package com.cosmeticPlatform.CosmeticPlatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "recently_viewed")
@Getter
@Setter
public class RecentlyViewed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int externalProductId;

    private String productName;
    private String productImage;
    private String productBrand;
    private String productPrice;

    private LocalDateTime viewedAt;

    public RecentlyViewed() {
        this.viewedAt = LocalDateTime.now();
    }
}
