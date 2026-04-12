package com.bolt.headless_auth_api.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GenerateOtpRequest(
        @Email(message = "Please provide a valid email address with a proper domain (e.g., user@domain.com)", regexp = "^[\\w.%+-]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,6}$") @NotBlank(message = "Email " +
                "address cannot be blank!") String email) {

    public GenerateOtpRequest {
        if (email() != null)
            email = email().trim().toLowerCase();
    }
}
