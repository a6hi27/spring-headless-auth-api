package com.bolt.headless_auth_api.auth;

import com.bolt.headless_auth_api.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // We will use a SecureRandom instance
    private final SecureRandom secureRandom = new SecureRandom();

    // TODO: For Day 3, we will simulate the Caffeine cache using a simple ConcurrentHashMap.
    // We will wire up the actual Spring @Cacheable annotations later to keep today focused on logic.
    private final Map<String, String> temporaryCache = new ConcurrentHashMap<>();

    /**
     * Generates a 6-digit OTP, hashes it, caches it, and emails the raw version.
     */
    public void generateAndSendOtp(String email) {
        // 1. Generate a 6-digit string using secureRandom
        String rawOtp = String.valueOf(secureRandom.nextInt(100000, 1000000));
        // 2. Hash the raw OTP using passwordEncoder.encode()
        String hashedOtp = passwordEncoder.encode(rawOtp);
        // 3. Save to temporaryCache (Key: email, Value: hashed OTP)
        temporaryCache.put(email, hashedOtp);
        // 4. Call emailService.sendOtpEmail(email, rawOtp)
        emailService.sendOtpEmail(email, rawOtp);
    }

    public boolean validateOtp(String email, String otp) {
        String hashedOtp = temporaryCache.get(email);

        if (hashedOtp == null)
            return false;

        boolean isMatch = passwordEncoder.matches(otp, hashedOtp);

        if (isMatch) {
            temporaryCache.remove(email);
        }
        return isMatch;
    }
}
