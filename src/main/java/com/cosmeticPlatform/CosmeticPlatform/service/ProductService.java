package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.exception.ProductNotFoundException;
import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository=productRepository;
    }

    public Product addProduct(Product product){
         return productRepository.save(product);
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setId(updatedProduct.getId());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setComments(updatedProduct.getComments());
        existingProduct.setRatings(updatedProduct.getRatings());
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product existingProduct = getProductById(id);
        productRepository.delete(existingProduct);
    }

    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }
}
