package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.*;
import com.cosmeticPlatform.CosmeticPlatform.repository.OrderRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.AddressRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CartService cartService, AddressRepository addressRepository,
            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order createOrder(Integer userId, Long addressId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // Check if address belongs to user
        if (address.getUser().getId() != userId) {
            throw new RuntimeException("Address does not belong to user");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PREPARING);
        order.setTotalAmount(cart.getTotalAmount());

        List<OrderItem> orderItems = cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - cartItem.getQuantity());
            // We need to save the product or rely on cascade/transactional dirty checking.
            // Better to explicitly save if not managed, but here it is managed.
            // Since we are in @Transactional, changes to managed entities (product) should
            // be flushed.
            // However, cartItem.getProduct() might refer to a detached instance depending
            // on how Cart was loaded.
            // CartService uses cartRepository which returns attached entities usually.

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            return orderItem;
        }).toList();

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);
        return savedOrder;
    }

    public List<Order> getUserOrders(Integer userId) {
        return orderRepository.findByUser_Id(userId);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
