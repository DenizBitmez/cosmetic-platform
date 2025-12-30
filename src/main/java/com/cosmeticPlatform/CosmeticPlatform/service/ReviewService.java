package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Review;
import com.cosmeticPlatform.CosmeticPlatform.repository.ReactionRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.ReplyRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReplyRepository replyRepository;
    private final ReactionRepository reactionRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ReplyRepository replyRepository,
            ReactionRepository reactionRepository) {
        this.reviewRepository = reviewRepository;
        this.replyRepository = replyRepository;
        this.reactionRepository = reactionRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, Review reviewDetails) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        review.setComment(reviewDetails.getComment());
        review.setRating(reviewDetails.getRating());
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public com.cosmeticPlatform.CosmeticPlatform.model.Reply addReply(Long reviewId,
            com.cosmeticPlatform.CosmeticPlatform.model.Reply reply) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        reply.setReview(review);
        return replyRepository.save(reply);
    }

    public com.cosmeticPlatform.CosmeticPlatform.model.Reaction addReaction(Long reviewId,
            com.cosmeticPlatform.CosmeticPlatform.model.Reaction reaction) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        reaction.setReview(review);
        return reactionRepository.save(reaction);
    }
}
