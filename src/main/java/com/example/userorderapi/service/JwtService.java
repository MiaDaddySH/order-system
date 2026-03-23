package com.example.userorderapi.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class JwtService {
    private final Algorithm algorithm;
    private final long expirationSeconds;

    public JwtService(
            @Value("${app.auth.jwt-secret}") String jwtSecret,
            @Value("${app.auth.jwt-expiration-seconds}") long expirationSeconds
    ) {
        this.algorithm = Algorithm.HMAC256(jwtSecret);
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(int userId, String email) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expirationSeconds);
        return JWT.create()
                .withIssuer("user-order-api")
                .withSubject(String.valueOf(userId))
                .withClaim("email", email)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}
