package com.example.demo.dto;

public record CreateOrderRequest(
        String product,
        int quantity
) {
}
