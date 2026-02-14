package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.Ingredient;
import com.cosmeticPlatform.CosmeticPlatform.repository.IngredientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@CrossOrigin(origins = "http://localhost:5173")
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    public IngredientController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    @GetMapping("/search")
    public List<Ingredient> searchIngredients(@RequestParam String query) {
        return ingredientRepository.findByNameContainingIgnoreCase(query);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable Long id) {
        return ingredientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
