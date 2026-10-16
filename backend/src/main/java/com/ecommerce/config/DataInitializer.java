package com.ecommerce.config;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;

    public DataInitializer(UserService userService, CategoryService categoryService, ProductService productService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @Override
    public void run(String... args) {
        userService.createAdminIfNotExists();

        if (categoryService.getAll().isEmpty()) {
            Category electronics = new Category(null, "Elektronika", "Gadžeti i uređaji");
            Category clothing = new Category(null, "Odeća", "Modna odeća");
            Category books = new Category(null, "Knjige", "Štampane i e-knjige");
            categoryService.save(electronics);
            categoryService.save(clothing);
            categoryService.save(books);

            // Sample products
            productService.save(new Product(null, "iPhone 15", "Najnoviji Apple telefon", 999.99, electronics.getId(), 50, "https://via.placeholder.com/300x300?text=iPhone+15", 0, 0));
            productService.save(new Product(null, "Samsung Galaxy S24", "Android flagship", 899.99, electronics.getId(), 40, "https://via.placeholder.com/300x300?text=Galaxy+S24", 0, 0));
            productService.save(new Product(null, "Majica", "Pamučna majica", 19.99, clothing.getId(), 100, "https://via.placeholder.com/300x300?text=Majica", 0, 0));
            productService.save(new Product(null, "Java knjiga", "Učenje Jave", 29.99, books.getId(), 30, "https://via.placeholder.com/300x300?text=Java+Knjiga", 0, 0));
        }
    }
}
