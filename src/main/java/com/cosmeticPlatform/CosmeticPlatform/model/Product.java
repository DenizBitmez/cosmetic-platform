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
    private int id;

    private String category;

    private double price;

    private int stock;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_ingredients", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> ingredients;

    // @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    // private List<Comment> comments;
    //
    // @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    // private List<Rating> ratings;

}
