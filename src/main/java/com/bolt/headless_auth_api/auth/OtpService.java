package com.bolt.headless_auth_api.auth;

import com.bolt.headless_auth_api.util.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final CacheManager cacheManager;

    // We will use a SecureRandom instance
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a 6-digit OTP, hashes it, caches it, and emails the raw version.
     */
    public void generateAndSendOtp(String email) {
        // 1. Generate a 6-digit string using secureRandom
        String rawOtp = String.valueOf(secureRandom.nextInt(100000, 1000000));
        // 2. Hash the raw OTP using passwordEncoder.encode()
        String hashedOtp = passwordEncoder.encode(rawOtp);
        // 3. Save to temporaryCache (Key: email, Value: hashed OTP)
        Cache otpCache = Objects.requireNonNull(cacheManager.getCache("otpCache"), "CRITICAL: 'otpCache' is missing!");
        otpCache.put(email, hashedOtp);
        // 4. Call emailService.sendOtpEmail(email, rawOtp)
        emailService.sendOtpEmail(email, rawOtp);
    }

    public boolean validateOtp(String email, String otp) {
        Cache otpCache = Objects.requireNonNull(cacheManager.getCache("otpCache"), "CRITICAL: 'otpCache' is missing!");

        String hashedOtp = otpCache.get(email, String.class);
        if (hashedOtp == null)
            return false;
        boolean isMatch = passwordEncoder.matches(otp, hashedOtp);

        if (isMatch) {
            otpCache.evict(email);
        }
        return isMatch;
    }
}
