package com.bolt.headless_auth_api.auth;

import com.bolt.headless_auth_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final OtpService otpService;
    private final JwtService jwtService;

    @PostMapping("/generate-otp")
    public ResponseEntity<String> generateOtp(
            @Valid @RequestBody GenerateOtpRequest request) {

        // Call the OtpService to generate and send the OTP using the email from the request
        String email = request.email();
        otpService.generateAndSendOtp(email);

        // Return a 200 OK success message
        return ResponseEntity.ok("OTP generated and sent successfully to " + request.email());
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(
            @Valid @RequestBody ValidateOtpRequest request) {

        // Call the OtpService to validate the OTP.
        String email = request.email();
        String otp = request.otp();
        boolean isValid = otpService.validateOtp(email, otp);

        // If it returns true, return a 200 OK ("OTP is valid").
        if (isValid) {
            AuthResponse authResponse = new AuthResponse(jwtService.generateToken(email));
            return ResponseEntity.ok(authResponse);
        }
        // If it returns false, return a 401 Unauthorized ("Invalid or expired OTP").
        return new ResponseEntity<>("Invalid or expired OTP", HttpStatus.UNAUTHORIZED);
    }
}
