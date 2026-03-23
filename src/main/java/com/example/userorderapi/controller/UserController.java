package com.example.userorderapi.controller;

import java.util.List;
import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userorderapi.dto.UserRegisterRequest;
import com.example.userorderapi.dto.UserLoginRequest;
import com.example.userorderapi.dto.UserLoginResponse;
import com.example.userorderapi.dto.UserResponse;
import com.example.userorderapi.dto.UserUpdateRequest;
import com.example.userorderapi.mapper.UserMapper;
import com.example.userorderapi.model.User;
import com.example.userorderapi.service.JwtService;
import com.example.userorderapi.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserMapper userMapper, UserService userService, JwtService jwtService) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        User user = new User();
        user.setEmail(request.email());
        User newUser = userService.saveUser(user, request.password());
        return ResponseEntity.created(URI.create("/users/" + newUser.getId()))
                .body(userMapper.toUserResponse(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@Valid @RequestBody UserLoginRequest request) {
        User user = userService.authenticate(request.email(), request.password());
        String token = jwtService.generateToken(user.getId(), user.getEmail());
        UserLoginResponse response = new UserLoginResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                user.getId(),
                user.getEmail()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> responses = userMapper.toUserResponses(users);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable int id) {
        User user = userService.getUserOrThrow(id);
        return ResponseEntity.ok(userMapper.toUserResponse(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable int id, @Valid @RequestBody UserUpdateRequest request) {
        User user = userMapper.toUser(request);
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(userMapper.toUserResponse(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
