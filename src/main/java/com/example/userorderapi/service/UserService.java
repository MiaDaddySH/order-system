package com.example.userorderapi.service;

import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.userorderapi.model.User;
import com.example.userorderapi.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

    public User authenticate(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (user.getPasswordHash() == null || !passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return user;
    }

    public User updateUser(int id, User user) {
        User existingUser = getUserOrThrow(id);
        existingUser.setName(user.getName());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhone(user.getPhone());
        return userRepository.save(existingUser);
    }

    public User getCurrentUserProfile(int userId) {
        return getUserOrThrow(userId);
    }

    public User updateCurrentUserProfile(int userId, User user) {
        return updateUser(userId, user);
    }

    public void changeCurrentUserPassword(int userId, String oldPassword, String newPassword) {
        User existingUser = getUserOrThrow(userId);
        if (existingUser.getPasswordHash() == null || !passwordEncoder.matches(oldPassword, existingUser.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Old password is incorrect");
        }
        existingUser.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(existingUser);
        log.info("security event=password_changed userId={}", userId);
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
