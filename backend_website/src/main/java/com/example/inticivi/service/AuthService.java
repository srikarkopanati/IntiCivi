package com.example.inticivi.service;

import com.example.inticivi.dto.RegisterRequest;
import com.example.inticivi.entity.Role;
import com.example.inticivi.entity.User;
import com.example.inticivi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request, Role role) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role != null ? role : Role.USER);
        return userRepository.save(user);
    }

    public String login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return jwtService.generateToken(user.getEmail(), user.getRole().name());
            }
        }
        throw new RuntimeException("Invalid credentials");
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}