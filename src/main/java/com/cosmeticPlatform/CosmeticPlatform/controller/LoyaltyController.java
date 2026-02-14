package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.LoyaltyPoints;
import com.cosmeticPlatform.CosmeticPlatform.model.PointTransaction;
import com.cosmeticPlatform.CosmeticPlatform.service.LoyaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loyalty")
@CrossOrigin(origins = "http://localhost:5173")
public class LoyaltyController {

    @Autowired
    private LoyaltyService loyaltyService;

    @GetMapping("/balance/{userId}")
    public ResponseEntity<LoyaltyPoints> getPoints(@PathVariable Integer userId) {
        return ResponseEntity.ok(loyaltyService.getPoints(userId));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<PointTransaction>> getHistory(@PathVariable Integer userId) {
        return ResponseEntity.ok(loyaltyService.getHistory(userId));
    }

    @PostMapping("/redeem")
    public ResponseEntity<Map<String, Object>> redeemPoints(@RequestBody Map<String, Object> payload) {
        Integer userId = Integer.parseInt(payload.get("userId").toString());
        Integer amount = Integer.parseInt(payload.get("amount").toString());
        String description = (String) payload.getOrDefault("description", "Point Redemption");

        boolean success = loyaltyService.redeemPoints(userId, amount, description);
        if (success) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Points redeemed successfully!"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Insufficient points."));
        }
    }
}
