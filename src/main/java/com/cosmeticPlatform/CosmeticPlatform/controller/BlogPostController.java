package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.BlogPost;
import com.cosmeticPlatform.CosmeticPlatform.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogPostController {

    private final BlogPostService blogPostService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping
    public List<BlogPost> getApprovedPosts() {
        return blogPostService.getApprovedPosts();
    }

    @GetMapping("/pending")
    public List<BlogPost> getPendingPosts() {
        return blogPostService.getPendingPosts();
    }

    @GetMapping("/{id}")
    public BlogPost getPost(@PathVariable Long id) {
        return blogPostService.getPostById(id);
    }

    @PostMapping("/submit")
    public BlogPost submitPost(@RequestBody BlogPost post) {
        return blogPostService.submitPost(post);
    }

    @PutMapping("/{id}/approve")
    public BlogPost approvePost(@PathVariable Long id) {
        return blogPostService.approvePost(id);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        blogPostService.deletePost(id);
    }
}
