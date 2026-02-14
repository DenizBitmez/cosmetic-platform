package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Ingredient;
import com.cosmeticPlatform.CosmeticPlatform.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientAnalysisService {

    private final IngredientRepository ingredientRepository;

    public IngredientAnalysisService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> analyzeIngredients(String text) {
        if (text == null || text.isEmpty())
            return new ArrayList<>();

        // Split by comma, newline, or semicolon
        String[] parts = text.split("[,\\n;]+");
        List<Ingredient> results = new ArrayList<>();

        for (String part : parts) {
            String name = part.trim();
            if (name.isEmpty())
                continue;

            Optional<Ingredient> ingredient = ingredientRepository.findByName(name)
                    .or(() -> ingredientRepository.findFirstByNameContainingIgnoreCase(name));
            if (ingredient.isPresent()) {
                results.add(ingredient.get());
            } else {
                // Return a "virtual" ingredient if not found, with unknown score
                Ingredient unknown = new Ingredient();
                unknown.setName(name);
                unknown.setSafetyRating(0); // 0 means unknown/unrated
                unknown.setAlertType("neutral");
                results.add(unknown);
            }
        }

        return results;
    }
}
