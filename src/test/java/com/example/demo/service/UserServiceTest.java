package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.demo.model.User;

class UserServiceTest {

    @Test
    void saveUserAssignsIncrementalId() {
        UserService userService = new UserService();

        User first = new User();
        first.setName("Alice");
        first.setEmail("alice@example.com");
        User second = new User();
        second.setName("Bob");
        second.setEmail("bob@example.com");

        User savedFirst = userService.saveUser(first);
        User savedSecond = userService.saveUser(second);

        assertEquals(1001, savedFirst.getId());
        assertEquals(1002, savedSecond.getId());
        assertEquals("Alice", savedFirst.getName());
        assertEquals("bob@example.com", savedSecond.getEmail());
    }
}
