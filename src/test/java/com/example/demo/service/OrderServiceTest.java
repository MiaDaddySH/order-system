package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.model.Order;

class OrderServiceTest {

    @Test
    void createOrderAssignsIncrementalId() {
        OrderService orderService = new OrderService();

        Order first = orderService.createOrder(new CreateOrderRequest("Book", 1));
        Order second = orderService.createOrder(new CreateOrderRequest("Pen", 2));

        assertEquals(1001, first.getId());
        assertEquals(1002, second.getId());
    }

    @Test
    void deleteOrderReturnsTrueForExistingOrder() {
        OrderService orderService = new OrderService();
        Order order = orderService.createOrder(new CreateOrderRequest("Book", 1));

        boolean deleted = orderService.deleteOrder(order.getId());

        assertTrue(deleted);
        assertTrue(orderService.getOrder(order.getId()).isEmpty());
    }

    @Test
    void deleteOrderReturnsFalseForMissingOrder() {
        OrderService orderService = new OrderService();

        boolean deleted = orderService.deleteOrder(999999);

        assertFalse(deleted);
    }
}
