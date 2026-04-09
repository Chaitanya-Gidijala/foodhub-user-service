package com.foodhub.user.dto;

import com.foodhub.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private Long id;
    private String username;
    private String email;
    private Role role;
    private String token;
}
