package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.PasswordResetToken;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.repository.PasswordResetTokenRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;
import com.cosmeticPlatform.CosmeticPlatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            // For security, don't reveal if user exists.
            // But for now, we return ok.
            return ResponseEntity.ok("If an account exists for " + email + ", a reset link has been sent.");
        }

        // Check for existing token and remove it
        tokenRepository.findByUser(user).ifPresent(existingToken -> {
            tokenRepository.delete(existingToken);
        });

        // Create Token
        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDate(LocalDateTime.now().plusMinutes(60)); // 1 hour expiry
        tokenRepository.save(myToken);

        // Send Email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Şifre Sıfırlama İsteği");
            message.setText("Merhaba,\n\n"
                    + "Şifrenizi sıfırlamak için lütfen aşağıdaki bağlantıya tıklayın:\n"
                    + "http://localhost:5173/reset-password?token=" + token + "\n\n"
                    + "Bu isteği siz yapmadıysanız, bu e-postayı görmezden gelebilirsiniz.\n\n"
                    + "İyi günler,\n"
                    + "Cosmetic Platform Ekibi");
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
        }

        System.out.println("Token generated for " + email + ": " + token); // Log for manual testing without SMTP

        return ResponseEntity.ok("Reset link sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestBody String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElse(null);

        if (resetToken == null || resetToken.isExpired()) {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }

        User user = resetToken.getUser();
        userService.updatePassword(user, newPassword);

        tokenRepository.delete(resetToken);

        return ResponseEntity.ok("Password reset successfully.");
    }
}
