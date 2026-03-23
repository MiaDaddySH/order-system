package com.example.userorderapi.dto;

public record UserLoginResponse(
        String token,
        String tokenType,
        long expiresIn,
        int userId,
        String email
) {
}
