package com.example.userorderapi.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Service
public class JwtService {
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long expirationSeconds;

    public JwtService(
            @Value("${app.auth.jwt-secret}") String jwtSecret,
            @Value("${app.auth.jwt-expiration-seconds}") long expirationSeconds
    ) {
        this.algorithm = Algorithm.HMAC256(jwtSecret);
        this.verifier = JWT.require(this.algorithm)
                .withIssuer("user-order-api")
                .build();
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

    public Optional<DecodedJWT> verifyAndDecodeToken(String token) {
        try {
            return Optional.of(verifier.verify(token));
        } catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }
}
