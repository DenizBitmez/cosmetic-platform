package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Ingredient;
import com.cosmeticPlatform.CosmeticPlatform.repository.IngredientRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Cacheable(value = "ingredients", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Ingredient> getIngredients(Pageable pageable) {
        return ingredientRepository.findAll(pageable);
    }

    @Cacheable(value = "ingredients", key = "#query + '-' + #pageable.pageNumber")
    public Page<Ingredient> searchIngredients(String query, Pageable pageable) {
        return ingredientRepository.findByNameContainingIgnoreCase(query, pageable);
    }

    @Cacheable(value = "ingredients", key = "#id")
    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id).orElse(null);
    }
}
