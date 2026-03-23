package com.example.userorderapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.userorderapi.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
