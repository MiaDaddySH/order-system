package com.example.userorderapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(
        @NotBlank(message = "Product is required")
        String product,
        @Positive(message = "Quantity must be positive")
        int quantity
) {
}
