package com.bolt.headless_auth_api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ValidateOtpRequest(@NotBlank(message = "Email " +
        "address cannot be blank!") @Email(message = "Please enter a valid email address!") String email,
                                 @NotBlank(message = "OTP cannot be blank!") @Size(message = "OTP should contain 6 digits!") @Pattern(regexp = "^\\d{6}$", message = "OTP must be exactly 6 numeric digits!") String otp) {
}
