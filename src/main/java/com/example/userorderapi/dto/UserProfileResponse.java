package com.example.userorderapi.dto;

public record UserProfileResponse(
        int id,
        String name,
        String email,
        String address,
        String phone
) {
}
