package com.ecommerce.controller;

import com.ecommerce.model.Cart;
import com.ecommerce.model.User;
import com.ecommerce.service.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Cart getCart(@AuthenticationPrincipal User user) {
        return cartService.getCart(user.getId());
    }

    @PostMapping("/add")
    public Cart add(@AuthenticationPrincipal User user, @RequestBody Map<String, Object> body) {
        String productId = (String) body.get("productId");
        int quantity = body.get("quantity") != null ? ((Number) body.get("quantity")).intValue() : 1;
        return cartService.addToCart(user.getId(), productId, quantity);
    }

    @DeleteMapping("/remove/{productId}")
    public Cart remove(@AuthenticationPrincipal User user, @PathVariable String productId) {
        return cartService.removeFromCart(user.getId(), productId);
    }
}
