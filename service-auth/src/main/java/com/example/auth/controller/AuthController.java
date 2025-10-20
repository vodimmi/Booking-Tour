package com.example.auth.controller;

import com.example.auth.dto.*;
import com.example.auth.entity.User;
import com.example.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or email already exists")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return tokens")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "400", description = "Invalid credentials")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Obtain new token pair using refresh token")
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid refresh token")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile", description = "Retrieve authenticated user's profile")
    @ApiResponse(responseCode = "200", description = "Profile retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<UserProfileResponse> getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserProfileResponse response = authService.getCurrentUserProfile(user);
        return ResponseEntity.ok(response);
    }
}