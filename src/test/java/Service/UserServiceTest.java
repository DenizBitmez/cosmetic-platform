package Service;
import jakarta.validation.ValidationException;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.model.UserType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;
import com.cosmeticPlatform.CosmeticPlatform.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.addUser(user);
        assertEquals(user.getEmail(),savedUser.getEmail());
        assertNotNull(savedUser.getPassword());
        verify(userRepository).save(any(User.class));

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
        verify(userRepository).findAll();
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
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository).findById(1L);
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
        verify(userRepository).findById(1L);
    }

    @Test
    public void loadUserByUsername_UserExists_ReturnsUserDetails() {
        //Given
        User user = new User();
        user.setEmail("deniz@example.com");
        user.setPassword("password123");
        user.setUserType(UserType.CLIENT);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        //When
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());

        //Then
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertNotNull(userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> {
            a.getAuthority();
            return true;
        }));
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    public void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        //Given
        User user = new User();
        user.setEmail("deniz@example.com");
        user.setPassword("password123");
        user.setUserType(UserType.CLIENT);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        //When
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(user.getEmail());
        });

        //Then
        verify(userRepository).findByEmail(user.getEmail());
    }
}