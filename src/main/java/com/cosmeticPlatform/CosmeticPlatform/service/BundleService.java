package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.ProductBundle;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductBundleRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BundleService {

    @Autowired
    private ProductBundleRepository bundleRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<ProductBundle> getBundlesForProduct(Integer productId, String category, String name, Double price,
            String image) {
        // 1. Get manually defined bundles
        List<ProductBundle> existingBundles = bundleRepository.findBundlesByProductId(productId);

        // 2. If no bundles exist, generate "Frequently Bought Together" dynamic bundle
        if (existingBundles.isEmpty()) {
            ProductBundle dynamicBundle = generateDynamicBundle(productId, category, name, price, image);
            if (dynamicBundle != null) {
                existingBundles.add(dynamicBundle);
            }
        }

        return existingBundles;
    }

    private ProductBundle generateDynamicBundle(Integer productId, String category, String name, Double price,
            String image) {
        Product mainProduct = productRepository.findById(productId).orElse(null);

        // Handle external product case
        if (mainProduct == null) {
            if (name == null)
                return null; // Can't generate without at least a name

            mainProduct = new Product();
            mainProduct.setId(productId);
            mainProduct.setName(name);
            mainProduct.setCategory(category);
            mainProduct.setPrice(price != null ? price : 0.0);
            mainProduct.setImage(image); // Set the image for display
            mainProduct.setStock(100); // Assume stock for external
        }

        // Simple heuristic: Get 2 other products from same category or random
        List<Product> recommendations = new ArrayList<>();

        // Try same category first
        String searchCategory = mainProduct.getCategory();
        if (searchCategory != null && !searchCategory.isEmpty()) {
            List<Product> sameCategory = productRepository.findAll().stream()
                    .filter(p -> p.getCategory() != null &&
                            (p.getCategory().equalsIgnoreCase(searchCategory)
                                    || searchCategory.contains(p.getCategory()))
                            &&
                            p.getId() != productId)
                    .limit(5)
                    .collect(Collectors.toList());

            if (!sameCategory.isEmpty()) {
                Collections.shuffle(sameCategory);
                recommendations.addAll(sameCategory.subList(0, Math.min(2, sameCategory.size())));
            }
        }

        // If not enough, fill with randoms
        if (recommendations.size() < 2) {
            List<Product> allProducts = productRepository.findAll();
            Collections.shuffle(allProducts);
            allProducts.stream()
                    .filter(p -> p.getId() != productId && !recommendations.contains(p))
                    .limit(2 - recommendations.size())
                    .forEach(recommendations::add);
        }

        if (recommendations.isEmpty())
            return null;

        // Create a transient bundle
        ProductBundle bundle = new ProductBundle();
        bundle.setName("Perfect Together Set");
        bundle.setDescription("Frequently bought with " + mainProduct.getName());
        bundle.setDiscountPercentage(0.10); // 10% off for dynamic bundles
        bundle.setValidUntil(LocalDateTime.now().plusDays(1));
        bundle.setSystemGenerated(true);

        List<Product> bundleProducts = new ArrayList<>();
        bundleProducts.add(mainProduct);
        bundleProducts.addAll(recommendations);
        bundle.setProducts(bundleProducts);

        return bundle;
    }
}
