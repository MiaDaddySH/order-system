package com.example.userorderapi.service;

import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        if (jwtSecret == null || jwtSecret.length() < 32) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "JWT secret is too weak");
        }
        this.algorithm = Algorithm.HMAC256(jwtSecret);
        this.verifier = JWT.require(this.algorithm)
                .withIssuer("user-order-api")
                .build();
        if (expirationSeconds <= 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "JWT expiration must be positive");
        }
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(int userId, String email, String passwordHash) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expirationSeconds);
        return JWT.create()
                .withIssuer("user-order-api")
                .withSubject(String.valueOf(userId))
                .withClaim("email", email)
                .withClaim("pwdv", passwordVersion(passwordHash))
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

    public boolean isTokenValidForUser(DecodedJWT decodedJWT, String email, String passwordHash) {
        String tokenEmail = decodedJWT.getClaim("email").asString();
        String tokenPasswordVersion = decodedJWT.getClaim("pwdv").asString();
        return email.equals(tokenEmail) && passwordVersion(passwordHash).equals(tokenPasswordVersion);
    }

    private String passwordVersion(String passwordHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((passwordHash == null ? "" : passwordHash).getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash).substring(0, 16);
        } catch (NoSuchAlgorithmException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to initialize token digest");
        }
    }
}
