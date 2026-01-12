package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.PriceAlert;
import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.response.PriceAlertResponseDTO;
import com.cosmeticPlatform.CosmeticPlatform.repository.PriceAlertRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PriceAlertService {

    @Autowired
    private PriceAlertRepository priceAlertRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<PriceAlertResponseDTO> getUserAlerts(Integer userId) {
        List<PriceAlert> alerts = priceAlertRepository.findByUserIdOrderByCreatedDateDesc(userId);

        return alerts.stream().map(alert -> {
            PriceAlertResponseDTO dto = new PriceAlertResponseDTO();
            dto.setId(alert.getId());
            dto.setUserId(alert.getUserId());
            dto.setProductId(alert.getProductId());
            dto.setTargetPrice(alert.getTargetPrice());
            dto.setCurrentPrice(alert.getCurrentPrice());
            dto.setOriginalPrice(alert.getCurrentPrice());
            dto.setIsActive(alert.getIsActive());
            dto.setCreatedDate(alert.getCreatedDate());
            dto.setNotifiedDate(alert.getNotifiedDate());

            // Use external product data if available, otherwise fetch from database
            if (alert.getExternalProductName() != null) {
                dto.setProductName(alert.getExternalProductName());
                dto.setProductImage(alert.getExternalProductImage());
                dto.setIsPriceDropped(false); // External products don't have live price updates
            } else if (alert.getProductId() != null) {
                Optional<Product> productOpt = productRepository.findById(alert.getProductId());
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    dto.setProductName(product.getName());
                    dto.setProductImage(product.getImage());
                    dto.setCurrentPrice(product.getPrice());
                    dto.setIsPriceDropped(product.getPrice() <= alert.getTargetPrice());
                }
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public PriceAlert createAlert(Integer userId, Integer productId, Double targetPrice,
            String externalName, String externalImage, String externalBrand) {
        System.out.println(
                "Creating price alert: userId=" + userId + ", productId=" + productId + ", targetPrice=" + targetPrice);
        System.out.println("External product: " + (externalName != null ? externalName : "none"));

        // Check if alert already exists
        Optional<PriceAlert> existing = priceAlertRepository.findByUserIdAndProductIdAndIsActiveTrue(userId, productId);
        if (existing.isPresent()) {
            throw new RuntimeException("Price alert already exists for this product");
        }

        // For database products, verify they exist and get current price
        // For external products (with external data), skip this check
        boolean isExternalProduct = externalName != null && !externalName.isEmpty();
        Double currentPrice = null;

        if (!isExternalProduct && productId != null) {
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                throw new RuntimeException("Product not found");
            }
            currentPrice = productOpt.get().getPrice();
        }

        // Validate target price
        if (targetPrice <= 0) {
            throw new RuntimeException("Target price must be greater than 0");
        }

        PriceAlert alert = new PriceAlert();
        alert.setUserId(userId);
        alert.setProductId(productId);
        alert.setTargetPrice(targetPrice);
        alert.setCurrentPrice(currentPrice);
        alert.setIsActive(true);

        // Set external product data if provided
        if (isExternalProduct) {
            alert.setExternalProductName(externalName);
            alert.setExternalProductImage(externalImage);
            alert.setExternalProductBrand(externalBrand);
            System.out.println("Saving external product price alert");
        }

        return priceAlertRepository.save(alert);
    }

    @Transactional
    public void deleteAlert(Long alertId) {
        priceAlertRepository.deleteById(alertId);
    }

    @Transactional
    public void deactivateAlert(Long alertId) {
        Optional<PriceAlert> alertOpt = priceAlertRepository.findById(alertId);
        if (alertOpt.isPresent()) {
            PriceAlert alert = alertOpt.get();
            alert.setIsActive(false);
            priceAlertRepository.save(alert);
        }
    }

    public boolean hasActiveAlert(Integer userId, Integer productId) {
        return priceAlertRepository.existsByUserIdAndProductIdAndIsActiveTrue(userId, productId);
    }

    public long getAlertCount(Integer userId) {
        return priceAlertRepository.countByUserId(userId);
    }

    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void checkPriceDrops() {
        System.out.println("=== CHECKING PRICE DROPS ===");
        List<PriceAlert> activeAlerts = priceAlertRepository.findByIsActiveTrue();
        System.out.println("Active alerts: " + activeAlerts.size());

        int notifiedCount = 0;

        for (PriceAlert alert : activeAlerts) {
            // Skip external products (no live price updates)
            if (alert.getExternalProductName() != null) {
                continue;
            }

            if (alert.getProductId() != null) {
                Optional<Product> productOpt = productRepository.findById(alert.getProductId());

                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    double currentPrice = product.getPrice();

                    if (currentPrice <= alert.getTargetPrice()) {
                        System.out.println("✅ Price drop detected! Product: " + product.getName() +
                                ", Target: $" + alert.getTargetPrice() +
                                ", Current: $" + currentPrice);

                        alert.setCurrentPrice(currentPrice);
                        alert.setNotifiedDate(LocalDateTime.now());
                        alert.setIsActive(false);
                        priceAlertRepository.save(alert);

                        notifiedCount++;
                    } else {
                        alert.setCurrentPrice(currentPrice);
                        priceAlertRepository.save(alert);
                    }
                }
            }
        }

        System.out.println("Price drop check completed. Notifications sent: " + notifiedCount);
    }

    public void checkPriceDropsNow() {
        checkPriceDrops();
    }
}
