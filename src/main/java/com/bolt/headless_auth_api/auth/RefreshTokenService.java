package com.bolt.headless_auth_api.auth;

import com.bolt.headless_auth_api.exception.TokenRefreshException;
import com.bolt.headless_auth_api.user.User;
import com.bolt.headless_auth_api.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Value("${api.security.jwt.refresh-token.expiration}")
    private long expirationDuration;

    public RefreshToken createRefreshToken(String email) {
        User user = userService.getUserByEmail(email);
        String token = UUID.randomUUID().toString();
        Instant expiryDate = Instant.now().plusMillis(expirationDuration);
        boolean revoked = false;

        RefreshToken refreshToken = new RefreshToken(user, token, expiryDate, revoked);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        Instant expiryDate = token.getExpiryDate();
        if (Instant.now().isAfter(expiryDate)) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("The refresh token has expired! Please login again!");
        }
        if (token.isRevoked())
            throw new TokenRefreshException("The refresh token is revoked!");
        return token;
    }
}
