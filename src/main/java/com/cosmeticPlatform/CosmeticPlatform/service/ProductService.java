package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product updateProduct(Integer id, Product updatedProduct) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setId(updatedProduct.getId());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setStock(updatedProduct.getStock());
        existingProduct.setPrice(updatedProduct.getPrice());
        // existingProduct.setComments(updatedProduct.getComments());
        // existingProduct.setRatings(updatedProduct.getRatings());
        return productRepository.save(existingProduct);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Integer id) {
        Product existingProduct = getProductById(id);
        productRepository.delete(existingProduct);
    }

    @Cacheable(value = "products", key = "'all'")
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Cacheable(value = "products", key = "#category + '-' + #isVegan + '-' + #isCrueltyFree")
    public List<Product> getProductsByCategory(String category, Boolean isVegan, Boolean isCrueltyFree) {
        if (isVegan == null && isCrueltyFree == null) {
            return productRepository.findByCategory(category);
        }
        return productRepository.findByCategoryWithFilters(category, isVegan, isCrueltyFree);
    }

    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
}
