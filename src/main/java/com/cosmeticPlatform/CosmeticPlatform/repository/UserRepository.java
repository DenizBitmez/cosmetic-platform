package com.cosmeticPlatform.CosmeticPlatform.repository;

import com.cosmeticPlatform.CosmeticPlatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String name);
    User findByUsername(String username);
}