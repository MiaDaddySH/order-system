package com.example.userorderapi.service;

import org.springframework.stereotype.Service;

import com.example.userorderapi.dto.CreateOrderRequest;
import com.example.userorderapi.model.Order;
import com.example.userorderapi.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setProduct(request.product());
        order.setQuantity(request.quantity());
        order.setStatus("created");
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrder(int id) {
        return orderRepository.findById(id);
    }

    public boolean deleteOrder(int id) {
        if (!orderRepository.existsById(id)) {
            return false;
        }
        orderRepository.deleteById(id);
        return true;
    }
}
