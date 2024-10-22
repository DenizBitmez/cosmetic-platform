package com.cosmeticPlatform.CosmeticPlatform.controller;

import jakarta.validation.Valid;
import com.cosmeticPlatform.CosmeticPlatform.model.Comment;
import com.cosmeticPlatform.CosmeticPlatform.model.request.CommentRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.cosmeticPlatform.CosmeticPlatform.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService=commentService;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment addComment(@Valid @RequestBody CommentRequestDTO commentRequestDTO){
        Comment comment=new Comment();
        comment.setId(commentRequestDTO.getId());
        comment.setContent(commentRequestDTO.getContent());
        return commentService.addComment(comment);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Comment updateComment(@PathVariable Long id, @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        return commentService.updateComment(id, commentRequestDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Comment deleteComment(@PathVariable Long id){
        return commentService.deleteComment(id);
//        return null;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Comment> getAllComment(){
        return commentService.getAllComment();
    }

}
