package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.UserProduct;
import com.cosmeticPlatform.CosmeticPlatform.service.UserProductService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shelf")
@CrossOrigin(origins = "*")
public class ShelfController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ShelfController.class);
    private final UserProductService userProductService;

    public ShelfController(UserProductService userProductService) {
        this.userProductService = userProductService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserProduct>> getUserProducts(@PathVariable int userId) {
        return ResponseEntity.ok(userProductService.getUserProducts(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<UserProduct> addProduct(
            @RequestParam int userId,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) String customName,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Integer paoMonths,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate openedDate,
            @RequestParam(required = false) String imageUrl) {
        return ResponseEntity
                .ok(userProductService.addProductToShelf(userId, productId, customName, brand, paoMonths, openedDate,
                        imageUrl));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        userProductService.deleteUserProduct(id);
        return ResponseEntity.ok().build();
    }
}
