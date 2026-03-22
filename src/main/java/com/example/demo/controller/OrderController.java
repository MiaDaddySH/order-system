package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.Order;
import com.example.demo.services.CreateOrderRequest;
import com.example.demo.services.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    // 👉 构造函数注入（推荐方式）
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request);
        return order;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable int id) {
        return ResponseEntity.of(orderService.getOrder(id));
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable int id) {
        boolean deleted = orderService.deleteOrder(id);

        if (deleted) {
            return "Deleted order " + id;
        }
        return "Order not found: " + id;
    }
}
