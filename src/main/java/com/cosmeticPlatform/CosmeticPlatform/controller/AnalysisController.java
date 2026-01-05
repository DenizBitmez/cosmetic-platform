package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.Ingredient;
import com.cosmeticPlatform.CosmeticPlatform.service.CombinationGuideService;
import com.cosmeticPlatform.CosmeticPlatform.service.IngredientAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(origins = "*")
public class AnalysisController {

    private final IngredientAnalysisService ingredientAnalysisService;
    private final CombinationGuideService combinationGuideService;

    public AnalysisController(IngredientAnalysisService ingredientAnalysisService,
            CombinationGuideService combinationGuideService) {
        this.ingredientAnalysisService = ingredientAnalysisService;
        this.combinationGuideService = combinationGuideService;
    }

    @PostMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> analyzeIngredients(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        return ResponseEntity.ok(ingredientAnalysisService.analyzeIngredients(text));
    }

    @PostMapping("/check-conflicts")
    public ResponseEntity<List<String>> checkConflicts(@RequestBody Map<String, List<String>> request) {
        List<String> ingredients = request.get("ingredients");
        return ResponseEntity.ok(combinationGuideService.checkCompatibility(ingredients));
    }
}
