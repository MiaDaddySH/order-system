package com.example.userorderapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String status;

    public Order() {
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
