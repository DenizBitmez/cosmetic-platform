package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.RecentlyViewed;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.model.Order;
import com.cosmeticPlatform.CosmeticPlatform.model.OrderItem;
import com.cosmeticPlatform.CosmeticPlatform.repository.RecentlyViewedRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final RecentlyViewedRepository recentlyViewedRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public HistoryController(RecentlyViewedRepository recentlyViewedRepository,
            UserRepository userRepository,
            OrderRepository orderRepository) {
        this.recentlyViewedRepository = recentlyViewedRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/viewed/{userId}")
    public ResponseEntity<?> addRecentlyViewed(@PathVariable int userId, @RequestBody RecentlyViewed item) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        recentlyViewedRepository.findByUserAndExternalProductId(user, item.getExternalProductId())
                .ifPresentOrElse(
                        existing -> {
                            existing.setViewedAt(LocalDateTime.now());
                            recentlyViewedRepository.save(existing);
                        },
                        () -> {
                            item.setUser(user);
                            recentlyViewedRepository.save(item);
                        });

        return ResponseEntity.ok().build();
    }

    @GetMapping("/viewed/{userId}")
    public ResponseEntity<List<RecentlyViewed>> getRecentlyViewed(@PathVariable int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(recentlyViewedRepository.findByUserOrderByViewedAtDesc(user));
    }

    @GetMapping("/buy-again/{userId}")
    public ResponseEntity<List<?>> getBuyAgain(@PathVariable int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Order> orders = orderRepository.findByUser(user);

        // This is a simplified version, it should probably return unique products from
        // previous orders
        List<?> products = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(OrderItem::getProduct)
                .distinct()
                .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }
}
