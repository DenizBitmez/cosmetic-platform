package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.Rating;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.model.request.RatingRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.RatingRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;

import java.util.List;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository, UserRepository userRepository,
            ProductRepository productRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Rating addRating(User user, Product product, int score) {
        if (user == null || userRepository.findById(user.getId()).isEmpty()) {
            throw new IllegalArgumentException("Geçersiz kullanıcı.");
        }
        if (product == null || productRepository.findById(product.getId()).isEmpty()) {
            throw new IllegalArgumentException("Geçersiz ürün.");
        }

        if (score < 0 || score > 5) {
            throw new IllegalArgumentException("Puan 0 ile 5 arasında olmalı.");
        }
        Rating rating = new Rating();
        rating.setId(rating.getId());
        // rating.setProduct(product);
        rating.setScore(score);
        return ratingRepository.save(rating);
    }

    public Rating updateRating(Long ratingId, RatingRequestDTO ratingRequestDTO) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Yorum bulunamadı, ID: " + ratingId));

        rating.setScore(ratingRequestDTO.getScore());
        return ratingRepository.save(rating);
    }

    public void deleteRating(Long id) {
        ratingRepository.deleteById(id);
    }

    public List<Rating> getAllRating() {
        return ratingRepository.findAll();
    }
}
