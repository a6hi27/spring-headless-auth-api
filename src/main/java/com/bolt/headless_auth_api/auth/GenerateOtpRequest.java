package com.bolt.headless_auth_api.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record GenerateOtpRequest(@Email(message = "Please provide a valid email!") @NotBlank(message = "Email " +
        "address cannot be blank!") String email) {

    public GenerateOtpRequest {
        if (email() != null)
            email = email().trim().toLowerCase();
    }
}
