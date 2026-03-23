package com.example.userorderapi.dto;

public record OrderResponse(
        int id,
        String product,
        int quantity,
        String status
) {
}
