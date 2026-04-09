package com.foodhub.user.dto;

import com.foodhub.user.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
    private String panCardNumber;
    private String fssaiLicenseNumber;
}
