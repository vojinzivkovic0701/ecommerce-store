package com.ecommerce.service;

import com.ecommerce.model.Wishlist;
import com.ecommerce.repository.JsonFileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private static final String FILENAME = "wishlists.json";
    private final JsonFileRepository repository;

    public WishlistService(JsonFileRepository repository) {
        this.repository = repository;
    }

    private List<Wishlist> getAll() {
        return repository.readAll(FILENAME, new TypeReference<List<Wishlist>>() {});
    }

    public Wishlist getWishlist(String userId) {
        return getAll().stream()
                .filter(w -> w.getUserId().equals(userId))
                .findFirst()
                .orElseGet(() -> {
                    Wishlist w = new Wishlist();
                    w.setUserId(userId);
                    return w;
                });
    }

    public Wishlist addToWishlist(String userId, String productId) {
        List<Wishlist> lists = getAll();
        Wishlist wishlist = lists.stream()
                .filter(w -> w.getUserId().equals(userId))
                .findFirst()
                .orElseGet(() -> {
                    Wishlist w = new Wishlist();
                    w.setUserId(userId);
                    lists.add(w);
                    return w;
                });
        if (!wishlist.getProductIds().contains(productId)) {
            wishlist.getProductIds().add(productId);
        }
        repository.writeAll(FILENAME, lists);
        return wishlist;
    }

    public Wishlist removeFromWishlist(String userId, String productId) {
        List<Wishlist> lists = getAll();
        Wishlist wishlist = lists.stream()
                .filter(w -> w.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
        if (wishlist != null) {
            wishlist.getProductIds().remove(productId);
            repository.writeAll(FILENAME, lists);
        }
        return wishlist != null ? wishlist : getWishlist(userId);
    }
}
