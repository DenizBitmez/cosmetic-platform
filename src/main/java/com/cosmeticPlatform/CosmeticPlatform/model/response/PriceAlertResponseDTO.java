package com.cosmeticPlatform.CosmeticPlatform.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceAlertResponseDTO {
    private Long id;
    private Integer userId;
    private Integer productId;
    private String productName;
    private String productImage;
    private Double targetPrice;
    private Double currentPrice;
    private Double originalPrice;
    private Boolean isActive;
    private Boolean isPriceDropped;
    private LocalDateTime createdDate;
    private LocalDateTime notifiedDate;
}
