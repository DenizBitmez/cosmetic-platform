package Service;
import jakarta.validation.ValidationException;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.model.UserType;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;
import com.cosmeticPlatform.CosmeticPlatform.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addUser_UserAlreadyExists_ThrowsValidationException() {
        // Given
        User user = new User();
        user.setEmail("deniz@example.com");
        user.setPassword("password123");
        user.setUserType(UserType.CLIENT);

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userService.addUser(user);
        });

        assertEquals("Bu email ile kayıtlı kullanıcı var.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void addUser_NewUser_SavesUser() {
        // Given
        User user = new User();
        user.setEmail("deniz@example.com");
        user.setPassword("password123");
        user.setUserType(UserType.CLIENT);

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(Math.toIntExact(1L));
        savedUser.setEmail("deniz@example.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setUserType(UserType.CLIENT);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.addUser(user);

        // Then
        assertNotNull(result);
        assertEquals("deniz@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void getAllUser_ReturnsUserList() {
        // Given
        User user = new User();
        user.setId(Math.toIntExact(1L));
        user.setEmail("deniz@example.com");
        user.setPassword("password123");
        user.setUserType(UserType.CLIENT);

        when(userRepository.findAll()).thenReturn(List.of(user));

        // When
        List<User> users = userService.getAllUser();

        // Then
        assertEquals(1, users.size());
        assertEquals("deniz@example.com", users.getFirst().getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getUserById_UserExists_ReturnsUser() {
        // Given
        User user = new User();
        user.setId(Math.toIntExact(1L));
        user.setEmail("deniz@example.com");
        user.setPassword("password123");
        user.setUserType(UserType.CLIENT);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        User result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals("deniz@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void getUserById_UserDoesNotExist_ThrowsException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("Kullanıcı bulunamadı", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }
}