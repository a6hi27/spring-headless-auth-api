package com.bolt.headless_auth_api.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String reasonPhrase,
        String message,
        Map<String, String> validationErrors,
        String path
) {
}
