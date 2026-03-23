package com.example.userorderapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.example.userorderapi.model.User;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanData() {
        jdbcTemplate.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");
    }

    @Test
    void saveUserAssignsGeneratedId() {
        User first = new User();
        first.setName("Alice");
        first.setEmail("alice@example.com");
        User second = new User();
        second.setName("Bob");
        second.setEmail("bob@example.com");

        User savedFirst = userService.saveUser(first);
        User savedSecond = userService.saveUser(second);

        assertEquals(1, savedFirst.getId());
        assertEquals(2, savedSecond.getId());
        assertEquals("Alice", savedFirst.getName());
        assertEquals("bob@example.com", savedSecond.getEmail());
    }

    @Test
    void getUserOrThrowReturnsUserWhenUserExists() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");
        User savedUser = userService.saveUser(user);

        User foundUser = userService.getUserOrThrow(savedUser.getId());

        assertEquals(savedUser.getId(), foundUser.getId());
    }

    @Test
    void getUserOrThrowThrowsWhenUserMissing() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.getUserOrThrow(999999));

        assertEquals("User not found", ex.getReason());
    }

    @Test
    void updateUserUpdatesExistingUser() {
        User original = new User();
        original.setName("Alice");
        original.setEmail("alice@example.com");
        User saved = userService.saveUser(original);

        User updatePayload = new User();
        updatePayload.setName("Alice Updated");
        updatePayload.setEmail("alice.updated@example.com");

        User updated = userService.updateUser(saved.getId(), updatePayload);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("Alice Updated", updated.getName());
        assertEquals("alice.updated@example.com", updated.getEmail());
    }

    @Test
    void updateUserThrowsWhenUserMissing() {
        User payload = new User();
        payload.setName("Nobody");
        payload.setEmail("nobody@example.com");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.updateUser(999999, payload));

        assertEquals("User not found", ex.getReason());
    }

    @Test
    void deleteUserRemovesExistingUser() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");
        User saved = userService.saveUser(user);

        userService.deleteUser(saved.getId());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.getUserOrThrow(saved.getId()));
        assertEquals("User not found", ex.getReason());
    }

    @Test
    void deleteUserThrowsWhenUserMissing() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.deleteUser(999999));

        assertEquals("User not found", ex.getReason());
    }
}
