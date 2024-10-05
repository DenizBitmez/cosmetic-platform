package Controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cosmeticPlatform.CosmeticPlatform.controller.CommentController;
import com.cosmeticPlatform.CosmeticPlatform.model.Comment;
import com.cosmeticPlatform.CosmeticPlatform.model.request.CommentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.cosmeticPlatform.CosmeticPlatform.service.CommentService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {
    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        objectMapper = new ObjectMapper(); // JSON nesnelerini dönüştürmek için
    }

    @Test
    void addComment_ShouldReturnCreatedComment() throws Exception {
        // Given
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setId(1L); // Comment ID
        commentRequestDTO.setContent("Bu bir test yorumudur."); // Yorum içeriği

        Comment savedComment = new Comment(); // Kaydedilecek Comment nesnesi
        savedComment.setId(1L);
        savedComment.setContent("Bu bir test yorumudur.");

        // Mock davranışı
        when(commentService.addComment(any(Comment.class))).thenReturn(savedComment);

        // When & Then
        mockMvc.perform(post("/api/comment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDTO))) // JSON'u body'e ekliyoruz
                .andExpect(status().isCreated()) // 201 CREATED bekliyoruz
                .andExpect(jsonPath("$.id").value(1)) // Yorum ID'sini doğruluyoruz
                .andExpect(jsonPath("$.content").value("Bu bir test yorumudur.")); // Yorum içeriğini doğruluyoruz
    }

    @Test
    void updateComment_ShouldReturnNoContent() throws Exception {
        // Given
        Long commentId = 1L;
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setContent("Güncellenmiş yorum."); // Yeni içerik

        // Mock davranışı
        when(commentService.updateComment(anyLong(), any(CommentRequestDTO.class))).thenReturn(new Comment());

        // When & Then
        mockMvc.perform(put("/api/comment/update/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDTO))) // JSON'u body'e ekliyoruz
                .andExpect(status().isNoContent()); // 204 NO CONTENT bekliyoruz
    }

    @Test
    void deleteComment_ShouldReturnNoContent() throws Exception {
        // Given
        Long commentId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/comment/delete/{id}", commentId))
                .andExpect(status().isNoContent()); // 204 NO CONTENT bekliyoruz
    }

    @Test
    void getAllComment_ShouldReturnCommentList() throws Exception {
        // Given
        List<Comment> comments = Collections.singletonList(new Comment()); // Yorum listesini oluşturuyoruz

        // Mock davranışı
        when(commentService.getAllComment()).thenReturn(comments);

        // When & Then
        mockMvc.perform(get("/api/comment/all"))
                .andExpect(status().isOk()) // 200 OK bekliyoruz
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // JSON içeriği bekliyoruz
                .andExpect(jsonPath("$").isArray()) // Dizi olduğunu kontrol ediyoruz
                .andExpect(jsonPath("$.length()").value(1)); // Yorum sayısını kontrol ediyoruz
    }
}
