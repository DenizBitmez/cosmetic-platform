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
        List<Wishlist> wishlists = wishlistRepository.findByUserIdOrderByAddedDateDesc(userId);

        return wishlists.stream().map(wishlist -> {
            WishlistResponseDTO dto = new WishlistResponseDTO();
            dto.setId(wishlist.getId());
            dto.setProductId(wishlist.getProductId());
            dto.setAddedDate(wishlist.getAddedDate());
            dto.setUserId(wishlist.getUserId()); // Added this line to ensure userId is set

            // Use external product data if available, otherwise fetch from database
            if (wishlist.getExternalProductName() != null) {
                dto.setProductName(wishlist.getExternalProductName());
                dto.setProductImage(wishlist.getExternalProductImage());
                dto.setProductPrice(wishlist.getExternalProductPrice());
                dto.setProductBrand(wishlist.getExternalProductBrand());
                dto.setProductCategory(wishlist.getExternalProductCategory());
                dto.setProductStock(null); // External products don't have stock in this context
            } else if (wishlist.getProductId() != null) {
                Optional<Product> productOpt = productRepository.findById(wishlist.getProductId());
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    dto.setProductName(product.getName());
                    dto.setProductImage(product.getImage());
                    dto.setProductPrice(product.getPrice());
                    dto.setProductBrand(product.getCategory()); // Product doesn't have brand field
                    dto.setProductCategory(product.getCategory());
                    dto.setProductStock(product.getStock());
                }
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public Wishlist addToWishlist(Integer userId, Integer productId, String externalName, String externalImage,
            Double externalPrice, String externalBrand, String externalCategory) {
        System.out.println("WishlistService.addToWishlist called with userId=" + userId + ", productId=" + productId);
        System.out.println("External product data: name=" + externalName + ", price=" + externalPrice);

        // Check if already exists
        boolean exists = wishlistRepository.existsByUserIdAndProductId(userId, productId);
        System.out.println("Already in wishlist: " + exists);
        if (exists) {
            throw new RuntimeException("Product already in wishlist");
        }

        // For database products, verify they exist
        // For external products (with external data), skip this check
        boolean isExternalProduct = externalName != null && !externalName.isEmpty();
        if (!isExternalProduct) {
            boolean productExists = productRepository.existsById(productId);
            System.out.println("Product exists in database: " + productExists);
            if (!productExists) {
                throw new RuntimeException("Product not found");
            }
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.setProductId(productId);

        // Set external product data if provided
        if (isExternalProduct) {
            wishlist.setExternalProductName(externalName);
            wishlist.setExternalProductImage(externalImage);
            wishlist.setExternalProductPrice(externalPrice);
            wishlist.setExternalProductBrand(externalBrand);
            wishlist.setExternalProductCategory(externalCategory);
            System.out.println("Saving external product to wishlist");
        }

        System.out.println("Saving wishlist to database...");
        Wishlist saved = wishlistRepository.save(wishlist);
        System.out.println("Wishlist saved successfully with ID: " + saved.getId());

        return saved;
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
