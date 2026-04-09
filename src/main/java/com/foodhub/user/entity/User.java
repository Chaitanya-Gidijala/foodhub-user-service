package com.foodhub.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    
    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private Role role;
    
    // Restaurant owner specific fields
    @Column(nullable = true)
    private String panCardNumber;
    
    @Column(nullable = true)
    private String fssaiLicenseNumber;
    
    private String approvalStatus = "APPROVED";
}
