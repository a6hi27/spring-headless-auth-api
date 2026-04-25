package com.bolt.headless_auth_api.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserProfileResponse_UserExists_ReturnsProfile() {
        String testEmail = "abhinavsuper27@gmail.com";
        String testRole = "USER";
        User mockUser = new User();
        mockUser.setEmail(testEmail);
        mockUser.setRole(testRole);

        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(mockUser));

        UserProfileResponse userProfileResponse = userService.getUserProfileResponse(testEmail);
        assertNotNull(userProfileResponse);
        assertEquals(testEmail, userProfileResponse.email());
        assertEquals(testRole, userProfileResponse.role());

        verify(userRepository).findByEmail(testEmail);
    }

    @Test
    void getUserProfileResponse_UserNotFound_ThrowsException() {
        String testEmail = "abhinavsuper27@gmail.com";
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserProfileResponse(testEmail);
        });
        assertEquals("User not found in database", exception.getMessage());
        verify(userRepository).findByEmail(testEmail);
    }
}
