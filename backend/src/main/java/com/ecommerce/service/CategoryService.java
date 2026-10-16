package com.ecommerce.service;

import com.ecommerce.model.Category;
import com.ecommerce.repository.JsonFileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private static final String FILENAME = "categories.json";
    private final JsonFileRepository repository;

    public CategoryService(JsonFileRepository repository) {
        this.repository = repository;
    }

    public List<Category> getAll() {
        return repository.readAll(FILENAME, new TypeReference<List<Category>>() {});
    }

    public Optional<Category> findById(String id) {
        return getAll().stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public Category save(Category category) {
        List<Category> categories = getAll();
        if (category.getId() == null || category.getId().isBlank()) {
            category.setId(repository.generateId());
            categories.add(category);
        } else {
            categories.removeIf(c -> c.getId().equals(category.getId()));
            categories.add(category);
        }
        repository.writeAll(FILENAME, categories);
        return category;
    }

    public void delete(String id) {
        List<Category> categories = getAll();
        categories.removeIf(c -> c.getId().equals(id));
        repository.writeAll(FILENAME, categories);
    }
}
