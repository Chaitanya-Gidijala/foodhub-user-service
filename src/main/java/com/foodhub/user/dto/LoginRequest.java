package com.foodhub.user.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String emailOrPhone;
    private String password;
}
