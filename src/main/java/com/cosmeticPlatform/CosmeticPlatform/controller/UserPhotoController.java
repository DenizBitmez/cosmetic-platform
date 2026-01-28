package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.model.UserPhoto;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserPhotoRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;
import com.cosmeticPlatform.CosmeticPlatform.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@CrossOrigin(origins = "http://localhost:5173")
public class UserPhotoController {

        @Autowired
        private UserPhotoRepository userPhotoRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private FileStorageService fileStorageService;

        @PostMapping("/upload")
        public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file,
                        @RequestParam("userId") Integer userId,
                        @RequestParam("productId") Integer productId,
                        @RequestParam(value = "description", required = false) String description) {

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new RuntimeException("Product not found"));

                String fileName = fileStorageService.storeFile(file);

                // Build the download URI
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/uploads/")
                                .path(fileName)
                                .toUriString();

                UserPhoto userPhoto = new UserPhoto();
                userPhoto.setImageUrl(fileDownloadUri);
                userPhoto.setDescription(description);
                userPhoto.setUser(user);
                userPhoto.setProduct(product);

                userPhotoRepository.save(userPhoto);

                // Update product photo count
                product.setPhotoCount(product.getPhotoCount() + 1);
                productRepository.save(product);

                return ResponseEntity.ok(userPhoto);
        }

        @GetMapping("/product/{productId}")
        public ResponseEntity<List<UserPhoto>> getPhotosByProduct(@PathVariable Integer productId) {
                List<UserPhoto> photos = userPhotoRepository.findByProductId(productId);
                return ResponseEntity.ok(photos);
        }

        @PostMapping("/{photoId}/like")
        public ResponseEntity<?> likePhoto(@PathVariable Long photoId) {
                UserPhoto photo = userPhotoRepository.findById(photoId)
                                .orElseThrow(() -> new RuntimeException("Photo not found"));

                photo.setLikes(photo.getLikes() + 1);
                userPhotoRepository.save(photo);

                return ResponseEntity.ok(photo);
        }
}
