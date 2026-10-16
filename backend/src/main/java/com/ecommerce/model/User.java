package com.ecommerce.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String username;
    private String password; // hashed
    private String email;
    private String role; // BUYER or ADMIN
    private String firstName;
    private String lastName;
    private String address;
}
