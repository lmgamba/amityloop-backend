package com.lmgamba.amityloop.domain.user;

import com.lmgamba.amityloop.domain.user.dto.LoginRequest;
import com.lmgamba.amityloop.domain.user.dto.RegisterRequest;
import com.lmgamba.amityloop.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Constructor injection of all dependencies
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // Registers a new user and returns a JWT token
    public String register(RegisterRequest request) {

        // Check if email is already taken
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        // Build the User entity — never store the raw password
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        // BCrypt hashes the password before saving to the database
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Persist the user to the database
        userRepository.save(user);

        // Generate and return a JWT token so the user is logged in immediately after registering
        return jwtService.generateToken(user.getEmail());
    }

    // Validates credentials and returns a JWT token
    public String login(LoginRequest request) {

        // Find the user by email — throw exception if not found
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        // Compare the raw password against the stored BCrypt hash
        // Never compare raw passwords directly
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Credentials are valid — generate and return a JWT token
        return jwtService.generateToken(user.getEmail());
    }
}