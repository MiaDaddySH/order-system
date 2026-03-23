package com.example.userorderapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.userorderapi.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
