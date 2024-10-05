package Service;

import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.Rating;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.model.request.RatingRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.RatingRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;
import com.cosmeticPlatform.CosmeticPlatform.service.RatingService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RatingServiceTest {
    private RatingService ratingService;
    private RatingRepository ratingRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        ratingRepository = mock(RatingRepository.class);
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        ratingService = new RatingService(ratingRepository, userRepository, productRepository);
    }

    @Test
    public void addRating_ShouldSaveRating_WhenValidInput() {
        // Given
        User user = new User();
        user.setId(Math.toIntExact(1L));

        Product product = new Product();
        product.setId(Math.toIntExact(1L));

        int score = 4;

        // Mocking the repository responses
        when(userRepository.findById((long) user.getId())).thenReturn(Optional.of(user));
        when(productRepository.findById((long) product.getId())).thenReturn(Optional.of(product));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Rating rating = ratingService.addRating(user, product, score);

        // Then
        assertNotNull(rating);
        assertEquals(score, rating.getScore());
        assertEquals(user, rating.getUser());
        assertEquals(product, rating.getProduct());
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    public void addRating_ShouldThrowException_WhenUserIsInvalid() {
        // Given
        User user = new User();
        user.setId(Math.toIntExact(1L));

        Product product = new Product();
        product.setId(Math.toIntExact(1L));

        int score = 4;

        // Mocking invalid user scenario
        when(userRepository.findById((long) user.getId())).thenReturn(Optional.empty());
        when(productRepository.findById((long) product.getId())).thenReturn(Optional.of(product));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ratingService.addRating(user, product, score);
        });
        assertEquals("Geçersiz kullanıcı.", exception.getMessage());
    }

    @Test
    public void updateRating_ShouldUpdateRating_WhenValidInput() {
        // Given
        Long ratingId = 1L;
        Rating existingRating = new Rating();
        existingRating.setId(ratingId);
        existingRating.setScore(3);

        RatingRequestDTO ratingRequestDTO = new RatingRequestDTO();
        ratingRequestDTO.setScore(5);

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Rating updatedRating = ratingService.updateRating(ratingId, ratingRequestDTO);

        // Then
        assertNotNull(updatedRating);
        assertEquals(5, updatedRating.getScore());
        verify(ratingRepository, times(1)).save(existingRating);
    }

    @Test
    public void updateRating_ShouldThrowException_WhenRatingNotFound() {
        // Given
        Long ratingId = 1L;
        RatingRequestDTO ratingRequestDTO = new RatingRequestDTO();
        ratingRequestDTO.setScore(5);

        // Mocking not found scenario
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ratingService.updateRating(ratingId, ratingRequestDTO);
        });
        assertEquals("Yorum bulunamadı, ID: " + ratingId, exception.getMessage());
    }

    @Test
    public void deleteRating_ShouldCallDeleteById_WhenValidId() {
        // Given
        Long ratingId = 1L;

        // When
        ratingService.deleteRating(ratingId);

        // Then
        verify(ratingRepository, times(1)).deleteById(ratingId);
    }

    @Test
    public void getAllRating_ShouldReturnListOfRatings() {
        // Given
        Rating rating1 = new Rating();
        Rating rating2 = new Rating();
        List<Rating> ratings = List.of(rating1, rating2);

        when(ratingRepository.findAll()).thenReturn(ratings);

        // When
        List<Rating> result = ratingService.getAllRating();

        // Then
        assertEquals(2, result.size());
        assertEquals(ratings, result);
        verify(ratingRepository, times(1)).findAll();
    }
}


