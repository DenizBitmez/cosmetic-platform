package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.BlogPost;
import com.cosmeticPlatform.CosmeticPlatform.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    public List<BlogPost> getApprovedPosts() {
        return blogPostRepository.findByIsApprovedTrueOrderByCreatedAtDesc();
    }

    public List<BlogPost> getPendingPosts() {
        return blogPostRepository.findByIsApprovedFalseOrderByCreatedAtDesc();
    }

    public BlogPost submitPost(BlogPost post) {
        post.setApproved(false); // Always needs approval initially
        return blogPostRepository.save(post);
    }

    public BlogPost approvePost(Long id) {
        BlogPost post = blogPostRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setApproved(true);
        return blogPostRepository.save(post);
    }

    public void deletePost(Long id) {
        blogPostRepository.deleteById(id);
    }

    public BlogPost getPostById(Long id) {
        return blogPostRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }
}
