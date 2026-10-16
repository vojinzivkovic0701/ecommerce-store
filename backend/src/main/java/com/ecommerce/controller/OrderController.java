package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import com.ecommerce.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getMyOrders(@AuthenticationPrincipal User user) {
        return orderService.getOrdersByUser(user.getId());
    }

    @PostMapping
    public Order create(@AuthenticationPrincipal User user, @RequestBody Map<String, String> body) {
        String address = body.getOrDefault("shippingAddress", user.getAddress());
        return orderService.createOrder(user.getId(), address);
    }
}
