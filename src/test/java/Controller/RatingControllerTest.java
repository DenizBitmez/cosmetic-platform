package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.cosmeticPlatform.CosmeticPlatform.controller.RatingController;
import com.cosmeticPlatform.CosmeticPlatform.model.Rating;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.request.RatingRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.cosmeticPlatform.CosmeticPlatform.service.ProductService;
import com.cosmeticPlatform.CosmeticPlatform.service.RatingService;
import com.cosmeticPlatform.CosmeticPlatform.service.UserService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RatingControllerTest {
    @Mock
    private RatingService ratingService;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private RatingController ratingController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
        objectMapper = new ObjectMapper(); // JSON nesnelerini dönüştürmek için
    }

    @Test
    void addRating_ShouldReturnCreatedRating() throws Exception {
        // Given
        RatingRequestDTO ratingRequestDTO = new RatingRequestDTO();
        ratingRequestDTO.setId(1L); // Kullanıcı ve ürün ID'si
        ratingRequestDTO.setScore(5); // Skor

        User user = new User(); // Kullanıcı örneği
        user.setId(Math.toIntExact(1L));
        user.setUsername("Deniz");

        Product product = new Product(); // Ürün örneği
        product.setId(Math.toIntExact(1L));
        product.setName("Kozmetik Ürün");

        Rating savedRating = new Rating(); // Kaydedilecek Rating nesnesi
        savedRating.setId(1L);
        savedRating.setScore(5);
        savedRating.setUser(user);
        savedRating.setProduct(product);

        // Mock davranışları
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(productService.getProductById(anyLong())).thenReturn(product);
        when(ratingService.addRating(any(User.class), any(Product.class), anyInt())).thenReturn(savedRating);

        // When & Then
        mockMvc.perform(post("/api/rating/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingRequestDTO)))
                .andExpect(status().isCreated()) // 201 CREATED bekliyoruz
                .andExpect(jsonPath("$.id").value(1)) // Rating ID'sini doğruluyoruz
                .andExpect(jsonPath("$.score").value(5)); // Skoru doğruluyoruz
    }

    @Test
    void updateRating_ShouldReturnNoContent() throws Exception {
        // Given
        Long ratingId = 1L;
        RatingRequestDTO ratingRequestDTO = new RatingRequestDTO();
        ratingRequestDTO.setScore(4); // Yeni skor

        // Mock davranışı
        when(ratingService.updateRating(anyLong(), any(RatingRequestDTO.class))).thenReturn(new Rating());

        // When & Then
        mockMvc.perform(put("/api/rating/{id}", ratingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ratingRequestDTO))) // JSON'u body'e ekliyoruz
                .andExpect(status().isNoContent()); // 204 NO CONTENT bekliyoruz
    }

    @Test
    void deleteRating_ShouldReturnNoContent() throws Exception {
        // Given
        Long ratingId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/rating/{id}", ratingId))
                .andExpect(status().isNoContent()); // 204 NO CONTENT bekliyoruz
    }

    @Test
    void getAllRating_ShouldReturnRatingList() throws Exception {
        // Given
        List<Rating> ratings = Collections.singletonList(new Rating()); // Rating listesini oluşturuyoruz

        // Mock davranışı
        when(ratingService.getAllRating()).thenReturn(ratings);

        // When & Then
        mockMvc.perform(get("/api/rating/all"))
                .andExpect(status().isOk()) // 200 OK bekliyoruz
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // JSON içeriği bekliyoruz
                .andExpect(jsonPath("$").isArray()) // Dizi olduğunu kontrol ediyoruz
                .andExpect(jsonPath("$.length()").value(1)); // Rating sayısını kontrol ediyoruz
    }
}
