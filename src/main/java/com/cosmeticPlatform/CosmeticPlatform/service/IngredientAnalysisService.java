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
    private final com.cosmeticPlatform.CosmeticPlatform.repository.ChemicalRiskRepository chemicalRiskRepository;

    public IngredientAnalysisService(IngredientRepository ingredientRepository,
            com.cosmeticPlatform.CosmeticPlatform.repository.ChemicalRiskRepository chemicalRiskRepository) {
        this.ingredientRepository = ingredientRepository;
        this.chemicalRiskRepository = chemicalRiskRepository;
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
                // Check Kaggle DB
                List<com.cosmeticPlatform.CosmeticPlatform.model.ChemicalRisk> risks = chemicalRiskRepository
                        .findByChemicalNameContainingIgnoreCase(name);

                Ingredient result = new Ingredient();
                result.setName(name);

                if (!risks.isEmpty()) {
                    // It's a risk!
                    com.cosmeticPlatform.CosmeticPlatform.model.ChemicalRisk risk = risks.get(0);
                    result.setSafetyRating(9); // Extremely High Risk
                    result.setAlertType("danger");
                    result.setDescription("WARNING: Listed by California Safe Cosmetics Program. Found in "
                            + risk.getProductName() + " by " + risk.getCompanyName() + ".");
                } else {
                    // Unknown
                    result.setSafetyRating(0); // 0 means unknown/unrated
                    result.setAlertType("neutral");
                    result.setDescription("No specific data found for this ingredient.");
                }
                results.add(result);
            }
        }

        return results;
    }
}
