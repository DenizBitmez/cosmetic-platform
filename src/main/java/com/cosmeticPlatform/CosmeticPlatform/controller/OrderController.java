package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.Order;
import com.cosmeticPlatform.CosmeticPlatform.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<com.cosmeticPlatform.CosmeticPlatform.model.response.OrderResponseDTO> createOrder(
            @PathVariable Integer userId, @RequestParam Long addressId) {
        Order order = orderService.createOrder(userId, addressId);
        return ResponseEntity.ok(mapToDTO(order));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<com.cosmeticPlatform.CosmeticPlatform.model.response.OrderResponseDTO>> getUserOrders(
            @PathVariable Integer userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        List<com.cosmeticPlatform.CosmeticPlatform.model.response.OrderResponseDTO> orderDTOs = orders.stream()
                .map(this::mapToDTO).toList();
        return ResponseEntity.ok(orderDTOs);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<com.cosmeticPlatform.CosmeticPlatform.model.response.OrderResponseDTO> getOrder(
            @PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(mapToDTO(order));
    }

    private com.cosmeticPlatform.CosmeticPlatform.model.response.OrderResponseDTO mapToDTO(Order order) {
        com.cosmeticPlatform.CosmeticPlatform.model.response.OrderResponseDTO dto = new com.cosmeticPlatform.CosmeticPlatform.model.response.OrderResponseDTO();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus().name());
        dto.setTotalAmount(order.getTotalAmount());
        if (order.getAddress() != null) {
            dto.setAddressTitle(order.getAddress().getTitle() + ", " + order.getAddress().getCity());
        }

        List<com.cosmeticPlatform.CosmeticPlatform.model.response.OrderItemDTO> itemDTOs = order.getOrderItems()
                .stream().map(item -> {
                    com.cosmeticPlatform.CosmeticPlatform.model.response.OrderItemDTO itemDTO = new com.cosmeticPlatform.CosmeticPlatform.model.response.OrderItemDTO();
                    itemDTO.setProductName(item.getProduct().getName());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPrice(item.getPrice());
                    itemDTO.setCategory(item.getProduct().getCategory());
                    return itemDTO;
                }).toList();

        dto.setOrderItems(itemDTOs);
        return dto;
    }
}
