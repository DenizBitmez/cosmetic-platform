package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Ingredient;
import com.cosmeticPlatform.CosmeticPlatform.repository.IngredientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Page<Ingredient> getIngredients(Pageable pageable) {
        return ingredientRepository.findAll(pageable);
    }

    public Page<Ingredient> searchIngredients(String query, Pageable pageable) {
        return ingredientRepository.findByNameContainingIgnoreCase(query, pageable);
    }

    public Optional<Ingredient> getIngredientById(Long id) {
        return ingredientRepository.findById(id);
    }
}
