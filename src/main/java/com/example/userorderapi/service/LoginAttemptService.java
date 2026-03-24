package com.example.userorderapi.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoginAttemptService {
    private static final Logger log = LoggerFactory.getLogger(LoginAttemptService.class);
    private final int maxFailedAttempts;
    private final Duration blockDuration;
    private final Map<String, AttemptState> attempts = new ConcurrentHashMap<>();

    public LoginAttemptService(
            @Value("${app.auth.login.max-failed-attempts:5}") int maxFailedAttempts,
            @Value("${app.auth.login.block-seconds:300}") long blockSeconds
    ) {
        this.maxFailedAttempts = maxFailedAttempts;
        this.blockDuration = Duration.ofSeconds(blockSeconds);
    }

    public void checkAllowed(String key) {
        AttemptState state = attempts.get(key);
        if (state == null) {
            return;
        }
        if (state.blockedUntil != null && state.blockedUntil.isAfter(Instant.now())) {
            log.warn("security event=login_throttled key={} blockedUntil={}", key, state.blockedUntil);
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many login attempts");
        }
    }

    public void recordFailure(String key) {
        attempts.compute(key, (k, oldState) -> {
            Instant now = Instant.now();
            AttemptState state = oldState == null ? new AttemptState() : oldState;
            if (state.blockedUntil != null && state.blockedUntil.isBefore(now)) {
                state.failedCount = 0;
                state.blockedUntil = null;
            }
            state.failedCount++;
            if (state.failedCount >= maxFailedAttempts) {
                state.blockedUntil = now.plus(blockDuration);
                state.failedCount = 0;
                log.warn("security event=login_blocked key={} blockedUntil={}", key, state.blockedUntil);
            }
            return state;
        });
    }

    public void recordSuccess(String key) {
        attempts.remove(key);
    }

    private static class AttemptState {
        private int failedCount;
        private Instant blockedUntil;
    }
}
