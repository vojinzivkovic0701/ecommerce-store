package com.ecommerce.service;

import com.ecommerce.model.*;
import com.ecommerce.repository.JsonFileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final String FILENAME = "orders.json";
    private final JsonFileRepository repository;
    private final CartService cartService;
    private final ProductService productService;

    public OrderService(JsonFileRepository repository, CartService cartService, ProductService productService) {
        this.repository = repository;
        this.cartService = cartService;
        this.productService = productService;
    }

    public List<Order> getAllOrders() {
        return repository.readAll(FILENAME, new TypeReference<List<Order>>() {});
    }

    public List<Order> getOrdersByUser(String userId) {
        return getAllOrders().stream()
                .filter(o -> o.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Optional<Order> findById(String id) {
        return getAllOrders().stream().filter(o -> o.getId().equals(id)).findFirst();
    }

    public Order createOrder(String userId, String shippingAddress) {
        Cart cart = cartService.getCart(userId);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setId(repository.generateId());
        order.setUserId(userId);
        order.setShippingAddress(shippingAddress);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        double total = 0;
        for (CartItem item : cart.getItems()) {
            Product product = productService.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getName());
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());
            order.getItems().add(orderItem);
            total += product.getPrice() * item.getQuantity();
            productService.updateStock(product.getId(), -item.getQuantity());
        }
        order.setTotal(total);

        List<Order> orders = getAllOrders();
        orders.add(order);
        repository.writeAll(FILENAME, orders);

        cartService.clearCart(userId);
        return order;
    }

    public Order updateStatus(String orderId, String status) {
        List<Order> orders = getAllOrders();
        Order order = orders.stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        repository.writeAll(FILENAME, orders);
        return order;
    }
}
