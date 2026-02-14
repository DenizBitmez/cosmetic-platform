package com.cosmeticPlatform.CosmeticPlatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "point_transaction")
@Getter
@Setter
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int points; // Positive for earning, negative for redemption

    @Column(nullable = false)
    private String description; // e.g., "Order #123", "Redemption"

    private LocalDateTime transactionDate;

    // Optional: Link to an order if applicable
    // @ManyToOne
    // @JoinColumn(name = "order_id")
    // private Order order;

    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
    }
}
