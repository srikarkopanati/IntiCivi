package com.example.inticivi.controller;

import com.example.inticivi.dto.AuthResponse;
import com.example.inticivi.dto.LoginRequest;
import com.example.inticivi.dto.RegisterRequest;
import com.example.inticivi.dto.UserDto;
import com.example.inticivi.entity.Role;
import com.example.inticivi.entity.User;
import com.example.inticivi.service.AuthService;
import com.example.inticivi.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        Role role = Role.USER;
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
            role = Role.ADMIN;
        }
        User user = authService.register(request, role);
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        UserDto dto = new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        User user = authService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        UserDto dto = new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token, dto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = authService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDto dto = new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(dto);
    }
}