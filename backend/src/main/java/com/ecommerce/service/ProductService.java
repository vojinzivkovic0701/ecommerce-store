package com.ecommerce.service;

import com.ecommerce.model.Product;
import com.ecommerce.repository.JsonFileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final String FILENAME = "products.json";
    private final JsonFileRepository repository;

    public ProductService(JsonFileRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts() {
        return repository.readAll(FILENAME, new TypeReference<List<Product>>() {});
    }

    public Optional<Product> findById(String id) {
        return getAllProducts().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public List<Product> search(String query, String categoryId, Double minPrice, Double maxPrice) {
        return getAllProducts().stream()
                .filter(p -> query == null || query.isBlank() || 
                        p.getName().toLowerCase().contains(query.toLowerCase()) ||
                        (p.getDescription() != null && p.getDescription().toLowerCase().contains(query.toLowerCase())))
                .filter(p -> categoryId == null || categoryId.isBlank() || p.getCategoryId().equals(categoryId))
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public Product save(Product product) {
        List<Product> products = getAllProducts();
        if (product.getId() == null || product.getId().isBlank()) {
            product.setId(repository.generateId());
            products.add(product);
        } else {
            products.removeIf(p -> p.getId().equals(product.getId()));
            products.add(product);
        }
        repository.writeAll(FILENAME, products);
        return product;
    }

    public void delete(String id) {
        List<Product> products = getAllProducts();
        products.removeIf(p -> p.getId().equals(id));
        repository.writeAll(FILENAME, products);
    }

    public void updateStock(String productId, int quantityChange) {
        Product product = findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStock(product.getStock() + quantityChange);
        if (product.getStock() < 0) {
            throw new RuntimeException("Insufficient stock");
        }
        save(product);
    }

    public void updateRating(String productId, double averageRating, int reviewCount) {
        Product product = findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setAverageRating(averageRating);
        product.setReviewCount(reviewCount);
        save(product);
    }
}
