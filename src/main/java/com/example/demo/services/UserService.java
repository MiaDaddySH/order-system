package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.models.User;

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

    public User saveUser(User user) {
        return createUser(user);
    }

    public List<User> getAllUsers() {
        return users;
    }
}
