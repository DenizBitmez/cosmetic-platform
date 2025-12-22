package Controller;

import com.cosmeticPlatform.CosmeticPlatform.controller.ProductController;
import com.cosmeticPlatform.CosmeticPlatform.exception.ProductNotFoundException;
import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.request.ProductRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.cosmeticPlatform.CosmeticPlatform.service.ProductService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ProductControllerTest {
    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;
    private ProductRequestDTO productRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(1);
        product.setName("Sample Product");
        product.setCategory("Cosmetics");

        productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setId(1);
        productRequestDTO.setName("Sample Product");
        productRequestDTO.setCategory("Cosmetics");
    }

    @Test
    void addProduct_shouldReturnCreatedProduct() {
        when(productService.addProduct(any(Product.class))).thenReturn(product);

        Product createdProduct = productController.addProduct(productRequestDTO);

        assertNotNull(createdProduct);
        assertEquals("Sample Product", createdProduct.getName());
        verify(productService, times(1)).addProduct(any(Product.class));
    }

    @Test
    void getProductById_shouldReturnProduct_whenProductExists() {
        when(productService.getProductById(1)).thenReturn(product);

        ResponseEntity<Product> response = productController.getProductById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sample Product", response.getBody().getName());
        verify(productService, times(1)).getProductById(1);
    }

    @Test
    void getProductById_shouldReturnNotFound_whenProductDoesNotExist() {
        when(productService.getProductById(1)).thenThrow(new ProductNotFoundException("Product not found"));

        assertThrows(ProductNotFoundException.class, () -> productController.getProductById(1));
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setId(1);
        updatedProduct.setName("Updated Product");
        updatedProduct.setCategory("Updated Category");

        when(productService.updateProduct(eq(1), any(Product.class))).thenReturn(updatedProduct);

        Product result = productController.updateProduct(1, updatedProduct);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        verify(productService, times(1)).updateProduct(eq(1), any(Product.class));
    }

    @Test
    void deleteProduct_shouldCallDeleteOnService() {
        doNothing().when(productService).deleteProduct(1);

        productController.deleteProduct(1);

        verify(productService, times(1)).deleteProduct(1);
    }

    @Test
    void getAllProduct_shouldReturnListOfProducts() {
        when(productService.getAllProduct()).thenReturn(Collections.singletonList(product));

        ResponseEntity<List<Product>> response = productController.getAllProduct();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
        assertEquals(1, response.getBody().size());
        verify(productService, times(1)).getAllProduct();
    }

    @Test
    void getAllProduct_shouldReturnNoContent_whenNoProducts() {
        when(productService.getAllProduct()).thenReturn(Collections.emptyList()); // Boş liste döndür

        ResponseEntity<List<Product>> response = productController.getAllProduct();

        // Status kodunu kontrol et
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Response gövdesinin null olmamasını kontrol et
        assertNull(response.getBody()); // response.getBody() null olmamalı

        // productService'in çağrıldığını doğrula
        verify(productService, times(1)).getAllProduct();
    }
}
