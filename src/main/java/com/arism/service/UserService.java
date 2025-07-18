package com.arism.service;

import com.arism.exception.ResourceNotFoundException;
import com.arism.model.User;
import com.arism.repository.UserRepository;
import com.arism.dto.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Register User
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("User already exists");
        }
        user.setName(user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.USER);
        return userRepository.save(user);
    }

    // Get user object by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        User user = getUserByEmail(email);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
