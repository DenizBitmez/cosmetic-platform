package com.cosmeticPlatform.CosmeticPlatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class Payment {
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentType;

    private double amount;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;//ödeme kime yapıldı
//
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    private Product product;//hangi ürün için yapıldı.

}
