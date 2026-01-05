package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.model.UserProduct;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserProductRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserProductService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserProductService.class);
    private final UserProductRepository userProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public UserProductService(UserProductRepository userProductRepository, ProductRepository productRepository,
            UserRepository userRepository) {
        this.userProductRepository = userProductRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<UserProduct> getUserProducts(int userId) {
        List<UserProduct> products = userProductRepository.findByUserId(userId);
        // Update expiry status on the fly
        LocalDate today = LocalDate.now();
        for (UserProduct up : products) {
            if (up.getExpiryDate() != null && up.getExpiryDate().isBefore(today)) {
                up.setExpired(true);
            }
        }
        return products;
    }

    public UserProduct addProductToShelf(int userId, Integer productId, String customName, String brand,
            Integer paoMonths, LocalDate openedDate, String imageUrl) {
        log.info("Shelf add request: user={}, productId={}, customName={}, brand={}", userId, productId, customName,
                brand);
        try {
            UserProduct up = new UserProduct();
            User user = userRepository.findById(userId).orElseThrow(() -> {
                log.error("User not found for ID: {}", userId);
                return new RuntimeException("User not found: " + userId);
            });
            up.setUser(user);

            if (productId != null) {
                Product product = productRepository.findById(productId).orElse(null);
                if (product != null) {
                    up.setProduct(product);
                    up.setCustomName(product.getName());
                    up.setBrand(product.getCategory());
                    up.setImageUrl(product.getImage());
                    if (paoMonths == null)
                        paoMonths = product.getPaoMonths();
                    log.info("Linked to catalog product: {}", product.getName());
                } else {
                    log.warn("Product ID {} not found in local DB, using provided metadata", productId);
                    up.setCustomName(customName);
                    up.setBrand(brand);
                    up.setImageUrl(imageUrl);
                }
            } else {
                up.setCustomName(customName);
                up.setBrand(brand);
                up.setImageUrl(imageUrl);
            }

            if (up.getCustomName() == null || up.getCustomName().isEmpty()) {
                up.setCustomName(customName != null ? customName : "Unknown Product");
            }
            if (up.getBrand() == null || up.getBrand().isEmpty()) {
                up.setBrand(brand != null ? brand : "Various");
            }

            up.setPaoMonths(paoMonths != null ? paoMonths : 12);
            up.setOpenedDate(openedDate);

            if (openedDate != null && up.getPaoMonths() != null) {
                up.setExpiryDate(openedDate.plusMonths(up.getPaoMonths()));
            }

            log.info("Saving UserProduct: {} for user {}", up.getCustomName(), userId);
            UserProduct saved = userProductRepository.save(up);
            log.info("Successfully saved UserProduct with ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("CRITICAL ERROR in addProductToShelf: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void deleteUserProduct(Long id) {
        userProductRepository.deleteById(id);
    }
}
