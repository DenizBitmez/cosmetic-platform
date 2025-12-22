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

    // @JsonIgnore // Şifrenin gizlenmesi için
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private java.util.List<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private java.util.List<Order> orders;
}
