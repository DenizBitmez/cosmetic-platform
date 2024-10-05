package Service;

import com.cosmeticPlatform.CosmeticPlatform.exception.ProductNotFoundException;
import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;
import com.cosmeticPlatform.CosmeticPlatform.service.ProductService;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(Math.toIntExact(1L));
        product.setName("Sample Product");
        product.setCategory("Cosmetics");
    }

    @Test
    void addProduct_shouldReturnSavedProduct() {
        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.addProduct(product);

        assertNotNull(savedProduct);
        assertEquals("Sample Product", savedProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void getProductById_shouldReturnProduct_whenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals("Sample Product", foundProduct.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_shouldThrowException_whenProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void updateProduct_shouldUpdateAndReturnProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setId(Math.toIntExact(1L));
        updatedProduct.setName("Updated Product");
        updatedProduct.setCategory("Updated Category");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.updateProduct(1L, updatedProduct);

        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Category", result.getCategory());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void deleteProduct_shouldRemoveProduct_whenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void deleteProduct_shouldThrowException_whenProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    void getAllProduct_shouldReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.getAllProduct();

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findAll();
    }
}
