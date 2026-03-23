package com.example.userorderapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.userorderapi.dto.CreateOrderRequest;
import com.example.userorderapi.dto.OrderResponse;
import com.example.userorderapi.mapper.OrderMapper;
import com.example.userorderapi.model.Order;
import com.example.userorderapi.service.OrderService;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderMapper orderMapper;
    private final OrderService orderService;

    public OrderController(OrderMapper orderMapper, OrderService orderService) {
        this.orderMapper = orderMapper;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity.created(URI.create("/orders/" + order.getId()))
                .body(orderMapper.toOrderResponse(order));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orderMapper.toOrderResponses(orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable int id) {
        return orderService.getOrder(id)
                .map(orderMapper::toOrderResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable int id) {
        boolean deleted = orderService.deleteOrder(id);

        if (deleted) {
            return "Deleted order " + id;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
    }
}
