package com.ecommerce.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private String id;
    private String productId;
    private String userId;
    private String username;
    private int rating; // 1-5
    private String comment;
    private LocalDateTime createdAt;
}
