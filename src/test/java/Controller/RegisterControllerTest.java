package Controller;

import com.cosmeticPlatform.CosmeticPlatform.controller.RegisterController;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.model.UserType;
import com.cosmeticPlatform.CosmeticPlatform.model.request.UserRequestDTO;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.cosmeticPlatform.CosmeticPlatform.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RegisterControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private RegisterController registerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
    }

    @Test
    void registerUser_ReturnsSuccessfulMessage() throws Exception {
        // given
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername("John Doe");
        userRequestDTO.setEmail("john@example.com");
        userRequestDTO.setPassword("password123");
        userRequestDTO.setUserType(UserType.CLIENT); // UserType'ı CLIENT olarak ayarladık.

        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setUserType(userRequestDTO.getUserType());

        when(userService.addUser(any(User.class))).thenReturn(user);

        // when & then
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\", \"email\":\"john@example.com\", \"password\":\"password123\", \"userType\":\"CLIENT\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Kayit basarili"));
    }

    @Test
    void registerUser_UserAlreadyExists_ReturnsBadRequest() throws Exception {
        // given
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername("John Doe");
        userRequestDTO.setEmail("john@example.com");
        userRequestDTO.setPassword("password123");
        userRequestDTO.setUserType(UserType.CLIENT); // UserType'ı CLIENT olarak ayarladık.

        when(userService.addUser(any(User.class))).thenThrow(new ValidationException("Bu email ile kayitli kullanici var."));

        // when & then
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\", \"email\":\"john@example.com\", \"password\":\"password123\", \"userType\":\"CLIENT\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bu email ile kayitli kullanici var."));
    }


}
