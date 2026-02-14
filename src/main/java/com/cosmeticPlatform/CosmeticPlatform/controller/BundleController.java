package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.ProductBundle;
import com.cosmeticPlatform.CosmeticPlatform.service.BundleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bundles")
@CrossOrigin(origins = "http://localhost:5173")
public class BundleController {

    @Autowired
    private BundleService bundleService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductBundle>> getBundlesForProduct(
            @PathVariable Integer productId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String image) {
        return ResponseEntity.ok(bundleService.getBundlesForProduct(productId, category, name, price, image));
    }
}
