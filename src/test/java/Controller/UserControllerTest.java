package Controller;

import com.cosmeticPlatform.CosmeticPlatform.controller.UserController;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.model.UserType;
import com.cosmeticPlatform.CosmeticPlatform.model.request.UserRequestDTO;
import com.cosmeticPlatform.CosmeticPlatform.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.validation.ValidationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private MockMvc mockMvc;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
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
        user.setPassword("hashedPassword"); // Burada hashlenmiş şifre kullanılıyor
        user.setUserType(UserType.CLIENT);

        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userService.addUser(any(User.class))).thenReturn(user);

        // When
        User result;
        result = userController.addUser(userRequestDTO);

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

        when(userService.addUser(any(User.class)))
                .thenThrow(new ValidationException("Bu email ile kayıtlı kullanıcı var."));

        // When & Then
        ValidationException exception;
        exception = assertThrows(ValidationException.class, () -> {
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

        when(userService.getUserById(1)).thenReturn(user);

        // When
        User result = userController.getUserById(1);

        // Then
        assertNotNull(result);
        assertEquals("deniz@example.com", result.getEmail());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    public void getUserById_UserDoesNotExist_ThrowsException() {
        // Given
        when(userService.getUserById(1)).thenThrow(new RuntimeException("Kullanıcı bulunamadı."));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userController.getUserById(1);
        });

        assertEquals("Kullanıcı bulunamadı.", exception.getMessage());
        verify(userService, times(1)).getUserById(1);
    }
}
