package com.bolt.headless_auth_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Service
public class JwtService {

    @Value("${api.security.jwt.secret-key}")
    private String secretKey;

    @Value("${api.security.jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
            return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        String email = Objects.requireNonNull(claims.getSubject(), "The 'sub' Claim of the JWT is null (The email is null");
        return email;
    }


    public String generateToken(String email) {
        return Jwts.builder().subject(email).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSecretKey())
                .compact();
    }

    public boolean isTokenValid(String token, String userEmail) {
        try {
            String userEmailInJwt = extractEmail(token);
            return userEmailInJwt.equals(userEmail);
        } catch (Exception e) {
            return false;
        }
    }
}
