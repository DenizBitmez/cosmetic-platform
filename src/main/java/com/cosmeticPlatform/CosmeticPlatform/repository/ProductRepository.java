package com.cosmeticPlatform.CosmeticPlatform.repository;

import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
        Optional<Product> findById(Integer id);

        List<Product> findByCategory(String category);

        @org.springframework.data.jpa.repository.Query("SELECT p FROM Product p WHERE p.category = :category " +
                        "AND (:isVegan IS NULL OR p.isVegan = :isVegan) " +
                        "AND (:isCrueltyFree IS NULL OR p.isCrueltyFree = :isCrueltyFree)")
        List<Product> findByCategoryWithFilters(
                        @org.springframework.data.repository.query.Param("category") String category,
                        @org.springframework.data.repository.query.Param("isVegan") Boolean isVegan,
                        @org.springframework.data.repository.query.Param("isCrueltyFree") Boolean isCrueltyFree);

        List<Product> findByNameContainingIgnoreCase(String name);
}