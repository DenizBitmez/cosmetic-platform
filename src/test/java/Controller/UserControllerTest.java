package Controller;

import com.cosmeticPlatform.CosmeticPlatform.controller.UserController;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.model.UserType;
import com.cosmeticPlatform.CosmeticPlatform.model.request.UserRequestDTO;
import com.cosmeticPlatform.CosmeticPlatform.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ValidationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addUser_NewUser_ReturnsCreatedResponse() {
        // Given
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername("deniz");
        userRequestDTO.setEmail("deniz@example.com");
        userRequestDTO.setPassword("password123");
        userRequestDTO.setUserType(UserType.CLIENT);

        User user = new User();
        user.setUsername("deniz");
        user.setEmail("deniz@example.com");
        user.setPassword("hashedPassword");  // Burada hashlenmiş şifre kullanılıyor
        user.setUserType(UserType.CLIENT);

        when(userService.addUser(any(User.class))).thenReturn(user);

        // When
        User result = userController.addUser(userRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals("deniz@example.com", result.getEmail());
        assertEquals(UserType.CLIENT, result.getUserType());
        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    public void addUser_ExistingEmail_ThrowsValidationException() {
        // Given
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername("deniz");
        userRequestDTO.setEmail("deniz@example.com");
        userRequestDTO.setPassword("password123");
        userRequestDTO.setUserType(UserType.CLIENT);

        User user = new User();
        user.setUsername("deniz");
        user.setEmail("deniz@example.com");
        user.setPassword("hashedPassword");
        user.setUserType(UserType.CLIENT);

        when(userService.addUser(any(User.class))).thenThrow(new ValidationException("Bu email ile kayıtlı kullanıcı var."));

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(userRequestDTO);
        });

        assertEquals("Bu email ile kayıtlı kullanıcı var.", exception.getMessage());
        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    public void getAllUsers_ReturnsUserList() {
        // Given
        User user1 = new User();
        user1.setEmail("deniz@example.com");
        User user2 = new User();
        user2.setEmail("ahmet@example.com");

        when(userService.getAllUser()).thenReturn(List.of(user1, user2));

        // When
        List<User> users = userController.getAllUser();

        // Then
        assertEquals(2, users.size());
        assertEquals("deniz@example.com", users.get(0).getEmail());
        assertEquals("ahmet@example.com", users.get(1).getEmail());
        verify(userService, times(1)).getAllUser();
    }

    @Test
    public void getUserById_UserExists_ReturnsUser() {
        // Given
        User user = new User();
        user.setEmail("deniz@example.com");

        when(userService.getUserById(1L)).thenReturn(user);

        // When
        User result = userController.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals("deniz@example.com", result.getEmail());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void getUserById_UserDoesNotExist_ThrowsException() {
        // Given
        when(userService.getUserById(1L)).thenThrow(new RuntimeException("Kullanıcı bulunamadı."));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userController.getUserById(1L);
        });

        assertEquals("Kullanıcı bulunamadı.", exception.getMessage());
        verify(userService, times(1)).getUserById(1L);
    }
}
