package com.bolt.headless_auth_api.auth;

import com.bolt.headless_auth_api.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private String token;
    private Instant expiryDate;
    private boolean revoked;

    public RefreshToken(User user, String token, Instant expiryDate, boolean revoked) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
        this.revoked = revoked;
    }
}
