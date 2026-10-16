package com.ecommerce.service;

import com.ecommerce.model.Review;
import com.ecommerce.repository.JsonFileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private static final String FILENAME = "reviews.json";
    private final JsonFileRepository repository;
    private final ProductService productService;

    public ReviewService(JsonFileRepository repository, ProductService productService) {
        this.repository = repository;
        this.productService = productService;
    }

    public List<Review> getReviewsForProduct(String productId) {
        return repository.readAll(FILENAME, new TypeReference<List<Review>>() {}).stream()
                .filter(r -> r.getProductId().equals(productId))
                .collect(Collectors.toList());
    }

    public Review addReview(Review review) {
        review.setId(repository.generateId());
        review.setCreatedAt(LocalDateTime.now());
        List<Review> reviews = repository.readAll(FILENAME, new TypeReference<List<Review>>() {});
        reviews.add(review);
        repository.writeAll(FILENAME, reviews);

        // Update product rating
        List<Review> productReviews = getReviewsForProduct(review.getProductId());
        double avg = productReviews.stream().mapToInt(Review::getRating).average().orElse(0);
        productService.updateRating(review.getProductId(), avg, productReviews.size());

        return review;
    }
}
