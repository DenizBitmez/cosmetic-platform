package com.cosmeticPlatform.CosmeticPlatform.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponseDTO {
    private Long id;
    private Integer userId;
    private Integer productId;
    private String productName;
    private String productImage;
    private Double productPrice;
    private String productCategory;
    private String productBrand;
    private Integer productStock;
    private LocalDateTime addedDate;
}
