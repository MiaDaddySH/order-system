package com.example.demo.dto;

public record OrderResponse(
        int id,
        String product,
        int quantity,
        String status
) {
}
