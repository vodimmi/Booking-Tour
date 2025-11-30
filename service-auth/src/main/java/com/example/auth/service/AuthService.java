package com.example.auth.service;

import com.example.auth.dto.*;
import com.example.auth.entity.PasswordResetToken;
import com.example.auth.entity.RefreshToken;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.repository.RoleRepository;
import com.example.auth.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenService passwordResetTokenService;
    private final NotificationService notificationService;

    public AuthService(UserService userService,
            RefreshTokenService refreshTokenService,
            RoleRepository roleRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            PasswordResetTokenService passwordResetTokenService,
            NotificationService notificationService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenService = passwordResetTokenService;
        this.notificationService = notificationService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());

        // Assign default USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(Set.of(userRole));

        user = userService.save(user);

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user == null || !user.isActive()) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Revoke existing refresh tokens
        refreshTokenService.revokeAllUserTokens(user);

        // Generate new tokens
        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());

        if (refreshToken == null || !refreshToken.isValid()) {
            throw new RuntimeException("Invalid refresh token");
        }

        User user = refreshToken.getUser();
        if (!user.isActive()) {
            throw new RuntimeException("User is not active");
        }

        // Revoke the old refresh token
        refreshTokenService.revokeToken(refreshToken);

        // Generate new tokens
        String newAccessToken = jwtService.generateAccessToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(newAccessToken, newRefreshToken.getToken());
    }

    public UserProfileResponse getCurrentUserProfile(User user) {
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                roles);
    }

    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userService.save(user);

        notificationService.sendPasswordChangedNotification(user.getEmail());
        logger.info("Password changed for user: {}", user.getEmail());
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user == null) {
            // Don't reveal if email exists or not (security best practice)
            logger.warn("Password reset requested for non-existent email: {}", request.getEmail());
            return;
        }

        String resetToken = passwordResetTokenService.createPasswordResetToken(user);
        notificationService.sendPasswordResetEmail(user.getEmail(), resetToken);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenService.validateAndGetToken(request.getResetToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        User user = resetToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userService.save(user);

        passwordResetTokenService.markTokenAsUsed(resetToken);
        notificationService.sendPasswordChangedNotification(user.getEmail());

        logger.info("Password reset for user: {}", user.getEmail());
    }

    @Transactional
    public void logout(String refreshTokenString) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenString);
        if (refreshToken != null) {
            refreshTokenService.revokeToken(refreshToken);
            logger.info("User logged out: {}", refreshToken.getUser().getEmail());
        }
    }

    @Transactional
    public void logoutAll(User user) {
        refreshTokenService.revokeAllUserTokens(user);
        logger.info("All sessions logged out for user: {}", user.getEmail());
    }

    @Transactional
    public UserProfileResponse updateProfile(User user, UpdateProfileRequest request) {
        user.setFullName(request.getFullName());
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        userService.save(user);

        logger.info("Profile updated for user: {}", user.getEmail());
        return getCurrentUserProfile(user);
    }
}