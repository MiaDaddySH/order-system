package com.example.userorderapi.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.userorderapi.model.User;
import com.example.userorderapi.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User createUser(User user, String rawPassword) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        User newUser = new User();
        newUser.setName(user.getName() == null ? "" : user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPasswordHash(passwordEncoder.encode(rawPassword));
        return userRepository.save(newUser);
    }

    public Optional<User> getUser(int id) {
        return userRepository.findById(id);
    }

    public User getUserOrThrow(int id) {
        return getUser(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User saveUser(User user, String rawPassword) {
        return createUser(user, rawPassword);
    }

    public User updateUser(int id, User user) {
        User existingUser = getUserOrThrow(id);
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
