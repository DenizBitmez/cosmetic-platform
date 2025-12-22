package com.cosmeticPlatform.CosmeticPlatform.controller;

import jakarta.validation.Valid;
import com.cosmeticPlatform.CosmeticPlatform.model.Rating;
import com.cosmeticPlatform.CosmeticPlatform.model.request.RatingRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.cosmeticPlatform.CosmeticPlatform.service.ProductService;
import com.cosmeticPlatform.CosmeticPlatform.service.RatingService;
import com.cosmeticPlatform.CosmeticPlatform.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/rating")
public class RatingController {
    private final RatingService ratingService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public RatingController(RatingService ratingService, UserService userService, ProductService productService) {
        this.ratingService = ratingService;
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Rating addRating(@Valid @RequestBody RatingRequestDTO ratingRequestDTO) {
        var user = userService.getUserById(ratingRequestDTO.getUserId());
        var product = productService.getProductById(ratingRequestDTO.getProductId());

        return ratingService.addRating(user, product, ratingRequestDTO.getScore());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Rating updateRating(@PathVariable Long id, @Valid @RequestBody RatingRequestDTO ratingRequestDTO) {
        return ratingService.updateRating(id, ratingRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
    }

    @GetMapping("/all")
    public List<Rating> getAllRating() {
        return ratingService.getAllRating();
    }
}
