package com.cosmeticPlatform.CosmeticPlatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    @JsonIgnore
    private String password;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    // private List<Comment> comments;
    //
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    // private List<Rating> ratings;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private java.util.List<Address> addresses;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private java.util.List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private java.util.List<RecentlyViewed> recentlyViewed;

    @Column(columnDefinition = "TEXT")
    private String allergies; // Comma separated list of ingredients

    // Skin Profile Fields for Personalization
    private String skinType; // OILY, DRY, COMBINATION, NORMAL, SENSITIVE

    @Column(columnDefinition = "TEXT")
    private String skinConcerns; // JSON array: ["acne", "wrinkles", "dark_spots"]

    private String skinTone; // FAIR, LIGHT, MEDIUM, TAN, DEEP

    private Integer age;
}
