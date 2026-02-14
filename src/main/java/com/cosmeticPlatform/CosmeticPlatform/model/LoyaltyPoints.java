package com.cosmeticPlatform.CosmeticPlatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "loyalty_points")
@Getter
@Setter
public class LoyaltyPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private int pointsBalance;

    private int totalPointsEarned; // Lifetime points
}
