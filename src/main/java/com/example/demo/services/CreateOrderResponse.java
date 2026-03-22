package com.example.demo.services;

public class CreateOrderResponse {
    private int id;
    private String status;

    public CreateOrderResponse(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
