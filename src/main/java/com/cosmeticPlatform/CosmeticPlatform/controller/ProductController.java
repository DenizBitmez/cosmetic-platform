package com.cosmeticPlatform.CosmeticPlatform.controller;

import jakarta.validation.Valid;
import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.request.ProductRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cosmeticPlatform.CosmeticPlatform.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setId(productRequestDTO.getId());
        product.setCategory(productRequestDTO.getCategory());
        product.setStock(productRequestDTO.getStock());
        product.setPrice(productRequestDTO.getPrice());
        return productService.addProduct(product);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Integer id, @Valid @RequestBody Product updatedProduct) {
        return productService.updateProduct(id, updatedProduct);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> product = productService.getAllProduct();

        if (product.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        List<Product> products = productService.searchProducts(name);
        return ResponseEntity.ok(products);
    }
}
