package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Comment;
import com.cosmeticPlatform.CosmeticPlatform.model.request.CommentRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cosmeticPlatform.CosmeticPlatform.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository){
        this.commentRepository=commentRepository;
    }

    public Comment addComment(Comment comment){
        return commentRepository.save(comment);

    }
    public Comment updateComment(Long id, CommentRequestDTO commentRequestDTO){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Yorum bulunamadı" + id));

        comment.setContent(commentRequestDTO.getContent());
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id){
        commentRepository.deleteById(id);
    }

    public List<Comment> getAllComment(){
        return commentRepository.findAll();
    }
}
