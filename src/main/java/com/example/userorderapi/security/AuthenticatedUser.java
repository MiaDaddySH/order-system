package com.example.userorderapi.security;

public record AuthenticatedUser(
        int userId,
        String email
) {
}
