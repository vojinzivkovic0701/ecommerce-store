package com.ecommerce.service;

import com.ecommerce.model.User;
import com.ecommerce.repository.JsonFileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final String FILENAME = "users.json";
    private final JsonFileRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(JsonFileRepository repository, @Lazy PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return repository.readAll(FILENAME, new TypeReference<List<User>>() {});
    }

    public Optional<User> findByUsername(String username) {
        return getAllUsers().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    public Optional<User> findById(String id) {
        return getAllUsers().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public User register(User user) {
        if (findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        user.setId(repository.generateId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole("BUYER");
        }
        List<User> users = getAllUsers();
        users.add(user);
        repository.writeAll(FILENAME, users);
        return user;
    }

    public void createAdminIfNotExists() {
        if (findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setEmail("admin@ecommerce.com");
            admin.setRole("ADMIN");
            admin.setFirstName("Admin");
            admin.setLastName("User");
            register(admin);
        }
    }
}
