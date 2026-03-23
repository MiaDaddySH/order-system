package com.example.userorderapi.dto;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
        @NotBlank(message = "Name is required")
        String name,
        String address,
        String phone
) {
}
