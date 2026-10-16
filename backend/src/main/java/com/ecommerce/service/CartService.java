package com.ecommerce.service;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.repository.JsonFileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private static final String FILENAME = "carts.json";
    private final JsonFileRepository repository;
    private final ProductService productService;

    public CartService(JsonFileRepository repository, ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    private List<Cart> getAllCarts() {
        return repository.readAll(FILENAME, new TypeReference<List<Cart>>() {});
    }

    public Cart getCart(String userId) {
        return getAllCarts().stream()
                .filter(c -> c.getUserId().equals(userId))
                .findFirst()
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    return cart;
                });
    }

    public Cart addToCart(String userId, String productId, int quantity) {
        Product product = productService.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        List<Cart> carts = getAllCarts();
        Cart cart = carts.stream()
                .filter(c -> c.getUserId().equals(userId))
                .findFirst()
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    carts.add(newCart);
                    return newCart;
                });

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst();
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + quantity);
        } else {
            cart.getItems().add(new CartItem(productId, quantity));
        }
        repository.writeAll(FILENAME, carts);
        return cart;
    }

    public Cart removeFromCart(String userId, String productId) {
        List<Cart> carts = getAllCarts();
        Cart cart = carts.stream()
                .filter(c -> c.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
        if (cart != null) {
            cart.getItems().removeIf(i -> i.getProductId().equals(productId));
            repository.writeAll(FILENAME, carts);
        }
        return cart != null ? cart : getCart(userId);
    }

    public void clearCart(String userId) {
        List<Cart> carts = getAllCarts();
        carts.removeIf(c -> c.getUserId().equals(userId));
        repository.writeAll(FILENAME, carts);
    }
}
