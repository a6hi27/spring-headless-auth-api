package com.bolt.headless_auth_api.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BandwidthBuilder;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    // This map holds a unique bucket for every email address
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    // 1. The Bucket Factory
    public Bucket resolveBucket(String email) {
        String cleanEmail = email.trim().toLowerCase();
        return cache.computeIfAbsent(cleanEmail, this::newBucket);
    }

    // 2. The Engine that defines the rules
    private Bucket newBucket(String email) {
        // RULE: Refill 1 token every 5 minutes
        Bandwidth bandwidth = BandwidthBuilder.builder().capacity(4).refillIntervally(1,
                Duration.ofMinutes(5)).build();

        // RULE: The bucket can hold a maximum of 3 tokens at any time
        return Bucket.builder()
                .addLimit(bandwidth)
                .build();
    }
}
