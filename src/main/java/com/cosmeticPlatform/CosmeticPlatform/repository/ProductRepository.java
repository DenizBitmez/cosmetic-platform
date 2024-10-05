package com.cosmeticPlatform.CosmeticPlatform.repository;

import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    Optional<Product> findById(Long id);
}