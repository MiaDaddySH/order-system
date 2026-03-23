package com.example.userorderapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.userorderapi.dto.CreateOrderRequest;
import com.example.userorderapi.model.Order;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanData() {
        jdbcTemplate.execute("TRUNCATE TABLE orders RESTART IDENTITY CASCADE");
    }

    @Test
    void createOrderAssignsGeneratedId() {
        Order first = orderService.createOrder(new CreateOrderRequest("Book", 1));
        Order second = orderService.createOrder(new CreateOrderRequest("Pen", 2));

        assertEquals(1, first.getId());
        assertEquals(2, second.getId());
    }

    @Test
    void deleteOrderReturnsTrueForExistingOrder() {
        Order order = orderService.createOrder(new CreateOrderRequest("Book", 1));

        boolean deleted = orderService.deleteOrder(order.getId());

        assertTrue(deleted);
        assertTrue(orderService.getOrder(order.getId()).isEmpty());
    }

    @Test
    void deleteOrderReturnsFalseForMissingOrder() {
        boolean deleted = orderService.deleteOrder(999999);

        assertFalse(deleted);
    }
}
