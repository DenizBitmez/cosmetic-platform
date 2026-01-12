package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.PriceAlert;
import com.cosmeticPlatform.CosmeticPlatform.model.response.PriceAlertResponseDTO;
import com.cosmeticPlatform.CosmeticPlatform.service.PriceAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/price-alert")
@CrossOrigin(origins = "*")
public class PriceAlertController {

    @Autowired
    private PriceAlertService priceAlertService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PriceAlertResponseDTO>> getUserAlerts(@PathVariable Integer userId) {
        try {
            List<PriceAlertResponseDTO> alerts = priceAlertService.getUserAlerts(userId);
            return ResponseEntity.ok(alerts);
        } catch (Exception e) {
            System.err.println("Error fetching alerts: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAlert(
            @RequestParam Integer userId,
            @RequestParam Integer productId,
            @RequestParam Double targetPrice,
            @RequestParam(required = false) String externalName,
            @RequestParam(required = false) String externalImage,
            @RequestParam(required = false) String externalBrand) {

        System.out.println("=== PRICE ALERT CREATE REQUEST ===");
        System.out.println("User ID: " + userId);
        System.out.println("Product ID: " + productId);
        System.out.println("Target Price: $" + targetPrice);
        System.out.println("External product: " + (externalName != null ? externalName : "none"));

        try {
            PriceAlert alert = priceAlertService.createAlert(userId, productId, targetPrice,
                    externalName, externalImage, externalBrand);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Price alert created successfully");
            response.put("alertId", alert.getId());
            System.out.println("✅ Price alert created with ID: " + alert.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            System.err.println("❌ RuntimeException: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            System.err.println("❌ Exception: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to create price alert: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{alertId}")
    public ResponseEntity<?> deleteAlert(@PathVariable Long alertId) {
        try {
            priceAlertService.deleteAlert(alertId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Price alert deleted");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to delete alert");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkAlert(
            @RequestParam Integer userId,
            @RequestParam Integer productId) {
        boolean hasAlert = priceAlertService.hasActiveAlert(userId, productId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasAlert", hasAlert);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Map<String, Long>> getAlertCount(@PathVariable Integer userId) {
        long count = priceAlertService.getAlertCount(userId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check-now")
    public ResponseEntity<?> checkPriceDropsNow() {
        try {
            priceAlertService.checkPriceDropsNow();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Price drop check completed");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to check price drops");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
