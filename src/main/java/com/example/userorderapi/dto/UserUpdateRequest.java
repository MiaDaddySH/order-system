package com.example.userorderapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email
) {
}
