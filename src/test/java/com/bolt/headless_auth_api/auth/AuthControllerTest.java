package com.bolt.headless_auth_api.auth;

import com.bolt.headless_auth_api.security.JwtService;
import com.bolt.headless_auth_api.security.RateLimitingService;

import com.bolt.headless_auth_api.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 1. Boot up ONLY the AuthController and the web layer (Validation, Exception Handlers)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 4. We mock the Service layer because we only want to test the Front Door!
    @MockitoBean
    private RateLimitingService rateLimitingService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private OtpService otpService;

    @MockitoBean
    private UserService userService;


    @Test
    void generateOtp_ValidEmail_ReturnsOk() throws Exception {
        // GIVEN: A perfectly valid request
        String testEmail = "abhinavsuper27@gmail.com";
        GenerateOtpRequest testRequest = new GenerateOtpRequest(testEmail);
        Bucket mockBucket = Mockito.mock(Bucket.class);
        when(rateLimitingService.resolveBucket(testEmail)).thenReturn(mockBucket);
        when(mockBucket.tryConsume(1)).thenReturn(true);
        // WHEN & THEN: Fire the request and assert the results
        mockMvc.perform(post("/api/v1/auth/generate-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest))) // Convert Java object to
                // JSON
                .andExpect(status().isOk()).andExpect(jsonPath("$").exists()).andExpect(jsonPath("$").value("OTP " +
                        "generated and sent successfully to " + testEmail));
        verify(otpService).generateAndSendOtp(testEmail);// Expect a 200 OK status
    }

    @Test
    void generateOtp_RateLimitExceeded_ReturnsTooManyRequests() throws Exception {
        // GIVEN: A perfectly valid request
        String testEmail = "abhinavsuper27@gmail.com";
        GenerateOtpRequest testRequest = new GenerateOtpRequest(testEmail);
        Bucket mockBucket = Mockito.mock(Bucket.class);
        when(rateLimitingService.resolveBucket(testEmail)).thenReturn(mockBucket);
        when(mockBucket.tryConsume(1)).thenReturn(false);
        // WHEN & THEN: Fire the request and assert the results
        mockMvc.perform(post("/api/v1/auth/generate-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testRequest))) // Convert Java object to
                // JSON
                .andExpect(status().isTooManyRequests()).andExpect(jsonPath("$").exists()).andExpect(jsonPath("$").value("Too many requests. Kindly wait 5 minutes before " +
                        "trying again."));
    }

    @Test
    void generateOtp_BlankEmail_ReturnsBadRequestAndValidationErrors() throws Exception {
        // GIVEN: An invalid request (empty email)
        String testEmail = "";
        GenerateOtpRequest request = new GenerateOtpRequest(testEmail);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/auth/generate-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()) // Assert 400 Status
                .andExpect(jsonPath("$.message").value("Payload validation failed")) // Assert Main Message
                .andExpect(jsonPath("$.validationErrors.email").value("Email address cannot be blank!"));
    }

    @Test
    void generateOtp_InvalidEmail_ReturnsBadRequestAndValidationErrors() throws Exception {
        // GIVEN: An invalid request (empty email)
        String testEmail = "abhinavsuper27@";
        GenerateOtpRequest request = new GenerateOtpRequest(testEmail);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/auth/generate-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()) // Assert 400 Status
                .andExpect(jsonPath("$.message").value("Payload validation failed")) // Assert Main Message
                .andExpect(jsonPath("$.validationErrors.email").value("Please provide a valid email address with a proper domain (e.g., user@domain.com)"));
    }

}
