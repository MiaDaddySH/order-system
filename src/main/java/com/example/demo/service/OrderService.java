package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {
    private final AtomicInteger currentId = new AtomicInteger(1000);
    private final List<Order> orders = new CopyOnWriteArrayList<>();

    public Order createOrder(CreateOrderRequest request) {
        int newId = currentId.incrementAndGet();

        Order order = new Order(
                newId,
                request.getProduct(),
                request.getQuantity(),
                "created"
        );

        orders.add(order);
        return order;
    }

    public List<Order> getAllOrders() {
        return orders;
    }

    public Optional<Order> getOrder(int id) {
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst();
    }

    public boolean deleteOrder(int id) {
        return orders.removeIf(order -> order.getId() == id);
    }
}
