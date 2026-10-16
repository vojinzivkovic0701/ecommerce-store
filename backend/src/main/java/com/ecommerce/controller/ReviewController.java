package com.ecommerce.controller;

import com.ecommerce.model.Review;
import com.ecommerce.model.User;
import com.ecommerce.service.ReviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/product/{productId}")
    public List<Review> getForProduct(@PathVariable String productId) {
        return reviewService.getReviewsForProduct(productId);
    }

    @PostMapping
    public Review add(@AuthenticationPrincipal User user, @RequestBody Review review) {
        review.setUserId(user.getId());
        review.setUsername(user.getUsername());
        return reviewService.addReview(review);
    }
}
