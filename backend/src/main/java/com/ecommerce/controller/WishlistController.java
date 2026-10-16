package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.model.Wishlist;
import com.ecommerce.service.WishlistService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public Wishlist get(@AuthenticationPrincipal User user) {
        return wishlistService.getWishlist(user.getId());
    }

    @PostMapping("/{productId}")
    public Wishlist add(@AuthenticationPrincipal User user, @PathVariable String productId) {
        return wishlistService.addToWishlist(user.getId(), productId);
    }

    @DeleteMapping("/{productId}")
    public Wishlist remove(@AuthenticationPrincipal User user, @PathVariable String productId) {
        return wishlistService.removeFromWishlist(user.getId(), productId);
    }
}
