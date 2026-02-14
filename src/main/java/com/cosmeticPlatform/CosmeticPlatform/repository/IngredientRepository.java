package com.cosmeticPlatform.CosmeticPlatform.repository;

import com.cosmeticPlatform.CosmeticPlatform.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByName(String name);

    List<Ingredient> findByNameContainingIgnoreCase(String name);

    // Legacy support or alias

    // Alias for service compatibility if needed, but we should switch to
    // findFirstByNameContainingIgnoreCase
    default Optional<Ingredient> findFirstByNameContainingIgnoreCase(String name) {
        return findByNameContainingIgnoreCase(name).stream().findFirst();
    }
}
