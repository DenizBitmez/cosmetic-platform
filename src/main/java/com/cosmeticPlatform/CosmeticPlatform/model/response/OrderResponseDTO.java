package com.cosmeticPlatform.CosmeticPlatform.model.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private LocalDateTime orderDate;
    private String status;
    private double totalAmount;
    private String addressTitle;
    private List<OrderItemDTO> orderItems;
}
