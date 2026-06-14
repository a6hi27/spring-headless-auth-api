package com.bolt.headless_auth_api.auth;


import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@GroupSequence({GenerateOtpRequest.class, GenerateOtpRequest.FirstOrder.class, GenerateOtpRequest.SecondOrder.class})
public record GenerateOtpRequest(
        @NotBlank(message = "Email " +
                "address cannot be blank!", groups = FirstOrder.class)
        @Email(message = "Please provide a valid email address with a proper domain (e.g., user@domain.com)", regexp
                = "^[\\w.%+-]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,6}$", groups = SecondOrder.class) String email) {

    public GenerateOtpRequest {
        if (email() != null)
            email = email().trim().toLowerCase();
    }

    public interface FirstOrder {
    }

    public interface SecondOrder {
    }
}
