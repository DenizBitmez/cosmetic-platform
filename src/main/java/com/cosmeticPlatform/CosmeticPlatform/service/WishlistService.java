package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.Wishlist;
import com.cosmeticPlatform.CosmeticPlatform.model.response.WishlistResponseDTO;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<WishlistResponseDTO> getUserWishlist(Integer userId) {
        List<Wishlist> wishlistItems = wishlistRepository.findByUserIdOrderByAddedDateDesc(userId);

        return wishlistItems.stream().map(item -> {
            WishlistResponseDTO dto = new WishlistResponseDTO();
            dto.setId(item.getId());
            dto.setUserId(item.getUserId());
            dto.setProductId(item.getProductId());
            dto.setAddedDate(item.getAddedDate());

            // Fetch product details
            Optional<Product> productOpt = productRepository.findById(item.getProductId());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                dto.setProductName(product.getName());
                dto.setProductImage(product.getImage());
                dto.setProductPrice(product.getPrice());
                dto.setProductCategory(product.getCategory());
                dto.setProductStock(product.getStock());
                // Brand can be extracted from product if available
                dto.setProductBrand(product.getCategory()); // Placeholder
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public Wishlist addToWishlist(Integer userId, Integer productId) {
        // Check if already exists
        if (wishlistRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new RuntimeException("Product already in wishlist");
        }

        // Verify product exists
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.setProductId(productId);

        return wishlistRepository.save(wishlist);
    }

    @Transactional
    public void removeFromWishlist(Integer userId, Integer productId) {
        wishlistRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Transactional
    public void removeById(Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }

    public boolean isInWishlist(Integer userId, Integer productId) {
        return wishlistRepository.existsByUserIdAndProductId(userId, productId);
    }

    public long getWishlistCount(Integer userId) {
        return wishlistRepository.countByUserId(userId);
    }

    /**
     * Generate a shareable link token for wishlist
     * In a real implementation, this would be stored in database
     */
    public String generateShareToken(Integer userId) {
        // Simple token generation - in production, store this in a separate table
        return UUID.randomUUID().toString();
    }

    /**
     * Get wishlist by share token
     * In a real implementation, this would look up the token in database
     */
    public List<WishlistResponseDTO> getWishlistByShareToken(String token) {
        // Placeholder - would need to implement token-to-userId mapping
        throw new UnsupportedOperationException("Share token lookup not yet implemented");
    }
}
