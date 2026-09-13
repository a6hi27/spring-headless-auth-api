package com.bolt.headless_auth_api.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank(message = "refreshToken field cannot be blank!") String refreshToken) {
}
