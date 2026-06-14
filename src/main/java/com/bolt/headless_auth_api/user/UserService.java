package com.bolt.headless_auth_api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public void getOrCreateUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            User newUser = User.builder().email(email).build();
            userRepository.save(newUser);
        }
    }

    public UserProfileResponse getUserProfileResponse(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> new RuntimeException("User not found in database"));
        return new UserProfileResponse(user.getEmail(), user.getRole());
    }
}
