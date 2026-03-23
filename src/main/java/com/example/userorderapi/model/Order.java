package com.example.userorderapi.model;

public class Order {
    private final int id;
    private final String product;
    private final int quantity;
    private final String status;

    public Order(int id, String product, int quantity, String status) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }
}
