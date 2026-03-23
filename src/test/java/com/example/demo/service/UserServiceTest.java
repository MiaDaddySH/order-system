package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

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

    @Test
    void getUserOrThrowReturnsUserWhenUserExists() {
        UserService userService = new UserService();
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");
        User savedUser = userService.saveUser(user);

        User foundUser = userService.getUserOrThrow(savedUser.getId());

        assertEquals(savedUser.getId(), foundUser.getId());
    }

    @Test
    void getUserOrThrowThrowsWhenUserMissing() {
        UserService userService = new UserService();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.getUserOrThrow(999999));

        assertEquals("User not found", ex.getReason());
    }

    @Test
    void updateUserUpdatesExistingUser() {
        UserService userService = new UserService();
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
        UserService userService = new UserService();
        User payload = new User();
        payload.setName("Nobody");
        payload.setEmail("nobody@example.com");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.updateUser(999999, payload));

        assertEquals("User not found", ex.getReason());
    }

    @Test
    void deleteUserRemovesExistingUser() {
        UserService userService = new UserService();
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
        UserService userService = new UserService();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.deleteUser(999999));

        assertEquals("User not found", ex.getReason());
    }
}
