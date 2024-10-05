package Service;
import com.cosmeticPlatform.CosmeticPlatform.model.Comment;
import com.cosmeticPlatform.CosmeticPlatform.model.request.CommentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.cosmeticPlatform.CosmeticPlatform.repository.CommentRepository;
import com.cosmeticPlatform.CosmeticPlatform.service.CommentService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommentServiceTest {
    private CommentService commentService;
    private CommentRepository commentRepository;

    @BeforeEach
    public void setUp() {
        commentRepository = mock(CommentRepository.class);
        commentService = new CommentService(commentRepository);
    }

    @Test
    public void addComment_ShouldSaveComment_WhenValidInput() {
        // Given
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test comment");

        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Comment savedComment = commentService.addComment(comment);

        // Then
        assertNotNull(savedComment);
        assertEquals(comment.getContent(), savedComment.getContent());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    public void updateComment_ShouldUpdateComment_WhenValidInput() {
        // Given
        Long commentId = 1L;
        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setContent("Old comment");

        CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setContent("Updated comment");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Comment updatedComment = commentService.updateComment(commentId, commentRequestDTO);

        // Then
        assertNotNull(updatedComment);
        assertEquals(commentRequestDTO.getContent(), updatedComment.getContent());
        verify(commentRepository, times(1)).save(existingComment);
    }

    @Test
    public void updateComment_ShouldThrowException_WhenCommentNotFound() {
        // Given
        Long commentId = 1L;
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setContent("Updated comment");

        // Mocking not found scenario
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.updateComment(commentId, commentRequestDTO);
        });
        assertEquals("Yorum bulunamadı" + commentId, exception.getMessage());
    }

    @Test
    public void deleteComment_ShouldCallDeleteById_WhenValidId() {
        // Given
        Long commentId = 1L;

        // When
        commentService.deleteComment(commentId);

        // Then
        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
    public void getAllComment_ShouldReturnListOfComments() {
        // Given
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setContent("Comment 1");

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setContent("Comment 2");

        List<Comment> comments = List.of(comment1, comment2);

        when(commentRepository.findAll()).thenReturn(comments);

        // When
        List<Comment> result = commentService.getAllComment();

        // Then
        assertEquals(2, result.size());
        assertEquals(comments, result);
        verify(commentRepository, times(1)).findAll();
    }
}
