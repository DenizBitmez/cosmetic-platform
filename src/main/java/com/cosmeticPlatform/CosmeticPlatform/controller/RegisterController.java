package com.cosmeticPlatform.CosmeticPlatform.controller;

import jakarta.validation.Valid;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.model.request.UserRequestDTO;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cosmeticPlatform.CosmeticPlatform.service.UserService;

@RestController
    @RequestMapping("/api/register")
    public class RegisterController {
    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.addUser(user);
            return ResponseEntity.ok("Kayit basarili");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

