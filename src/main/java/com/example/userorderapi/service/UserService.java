package com.example.userorderapi.service;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.userorderapi.model.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {
    private final List<User> users = new CopyOnWriteArrayList<>();
    private final AtomicInteger currentId = new AtomicInteger(1000);

    private User createUser(User user) {
        int newId = currentId.incrementAndGet();
        User newUser = new User();
        newUser.setId(newId);
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        users.add(newUser);
        return newUser;
    }

    public Optional<User> getUser(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    public User getUserOrThrow(int id) {
        return getUser(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User saveUser(User user) {
        return createUser(user);
    }

    public User updateUser(int id, User user) {
        User existingUser = getUserOrThrow(id);
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        return existingUser;
    }

    public void deleteUser(int id) {
        boolean deleted = users.removeIf(user -> user.getId() == id);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public List<User> getAllUsers() {
        return users;
    }
}
