package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.Reaction;
import com.cosmeticPlatform.CosmeticPlatform.model.Reply;
import com.cosmeticPlatform.CosmeticPlatform.model.Review;
import com.cosmeticPlatform.CosmeticPlatform.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @PostMapping("/add")
    public Review addReview(@RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @PutMapping("/{id}")
    public Review updateReview(@PathVariable Long id, @RequestBody Review review) {
        return reviewService.updateReview(id, review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    @PostMapping("/{id}/reply")
    public Reply addReply(@PathVariable Long id, @RequestBody Reply reply) {
        return reviewService.addReply(id, reply);
    }

    @PostMapping("/{id}/react")
    public Reaction addReaction(@PathVariable Long id, @RequestBody Reaction reaction) {
        return reviewService.addReaction(id, reaction);
    }
}
