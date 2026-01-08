package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.Wishlist;
import com.cosmeticPlatform.CosmeticPlatform.model.response.WishlistResponseDTO;
import com.cosmeticPlatform.CosmeticPlatform.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    /**
     * Get user's wishlist
     * GET /api/wishlist/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<WishlistResponseDTO>> getUserWishlist(@PathVariable Integer userId) {
        try {
            List<WishlistResponseDTO> wishlist = wishlistService.getUserWishlist(userId);
            return ResponseEntity.ok(wishlist);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Add product to wishlist
     * POST /api/wishlist/add?userId={userId}&productId={productId}
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(
            @RequestParam Integer userId,
            @RequestParam Integer productId) {
        try {
            Wishlist wishlist = wishlistService.addToWishlist(userId, productId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product added to wishlist");
            response.put("wishlistId", wishlist.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to add to wishlist");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Remove product from wishlist
     * DELETE /api/wishlist/remove?userId={userId}&productId={productId}
     */
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromWishlist(
            @RequestParam Integer userId,
            @RequestParam Integer productId) {
        try {
            wishlistService.removeFromWishlist(userId, productId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product removed from wishlist");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to remove from wishlist");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Remove wishlist item by ID
     * DELETE /api/wishlist/{wishlistId}
     */
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<?> removeById(@PathVariable Long wishlistId) {
        try {
            wishlistService.removeById(wishlistId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Item removed from wishlist");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to remove item");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Check if product is in wishlist
     * GET /api/wishlist/check?userId={userId}&productId={productId}
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkWishlist(
            @RequestParam Integer userId,
            @RequestParam Integer productId) {
        boolean inWishlist = wishlistService.isInWishlist(userId, productId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("inWishlist", inWishlist);
        return ResponseEntity.ok(response);
    }

    /**
     * Get wishlist count
     * GET /api/wishlist/{userId}/count
     */
    @GetMapping("/{userId}/count")
    public ResponseEntity<Map<String, Long>> getWishlistCount(@PathVariable Integer userId) {
        long count = wishlistService.getWishlistCount(userId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    /**
     * Generate shareable wishlist link
     * POST /api/wishlist/{userId}/share
     */
    @PostMapping("/{userId}/share")
    public ResponseEntity<Map<String, String>> generateShareLink(@PathVariable Integer userId) {
        try {
            String token = wishlistService.generateShareToken(userId);
            Map<String, String> response = new HashMap<>();
            response.put("shareToken", token);
            response.put("shareUrl", "/wishlist/shared/" + token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
