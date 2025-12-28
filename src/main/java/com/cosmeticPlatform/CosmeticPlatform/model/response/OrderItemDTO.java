package com.cosmeticPlatform.CosmeticPlatform.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {
    private String productName;
    private int quantity;
    private double price;
    private String category;
}
