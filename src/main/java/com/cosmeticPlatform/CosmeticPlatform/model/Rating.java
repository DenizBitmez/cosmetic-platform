package com.cosmeticPlatform.CosmeticPlatform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rating")
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;

    public Object getProduct() {
        throw new UnsupportedOperationException("Unimplemented method 'getProduct'");
    }

    public Object getUser() {
        throw new UnsupportedOperationException("Unimplemented method 'getUser'");
    }

    public void setUser(User user) {
        throw new UnsupportedOperationException("Unimplemented method 'setUser'");
    }

    public void setProduct(Product product) {
        throw new UnsupportedOperationException("Unimplemented method 'setProduct'");
    }

    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private User user;
    //
    // @ManyToOne
    // @JoinColumn(name = "product_id")
    // private Product product;
}
