package com.example.auth.controller;

import com.example.auth.dto.*;
import com.example.auth.entity.User;
import com.example.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Profile retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<UserProfileResponse> getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserProfileResponse response = authService.getCurrentUserProfile(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Change password for authenticated user")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Password changed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid old password")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<MessageResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        authService.changePassword(user, request);
        return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Request password reset link via email")
    @ApiResponse(responseCode = "200", description = "Password reset email sent (if email exists)")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(new MessageResponse("If the email exists, a password reset link has been sent"));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset password using token from email")
    @ApiResponse(responseCode = "200", description = "Password reset successfully")
    @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(new MessageResponse("Password reset successfully"));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Revoke current refresh token")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Logged out successfully")
    public ResponseEntity<MessageResponse> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(new MessageResponse("Logged out successfully"));
    }

    @PostMapping("/logout-all")
    @Operation(summary = "Logout from all sessions", description = "Revoke all refresh tokens for current user")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Logged out from all sessions")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<MessageResponse> logoutAll(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        authService.logoutAll(user);
        return ResponseEntity.ok(new MessageResponse("Logged out from all sessions"));
    }

    @PutMapping("/me")
    @Operation(summary = "Update profile", description = "Update current user's profile")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "Profile updated successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserProfileResponse response = authService.updateProfile(user, request);
        return ResponseEntity.ok(response);
    }
}