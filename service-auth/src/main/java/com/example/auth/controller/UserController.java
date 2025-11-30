package com.example.auth.controller;

import com.example.auth.dto.UserListResponse;
import com.example.auth.dto.UserProfileResponse;
import com.example.auth.entity.User;
import com.example.auth.service.AuthService;
import com.example.auth.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User management APIs (ADMIN only)")
public class UserController {

    private final UserManagementService userManagementService;
    private final AuthService authService;

    public UserController(UserManagementService userManagementService, AuthService authService) {
        this.userManagementService = userManagementService;
        this.authService = authService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "List all users (ADMIN only)", description = "Get paginated list of all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    public ResponseEntity<Page<UserListResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort) {
        Page<UserListResponse> users = userManagementService.getAllUsers(page, size, sort);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve user profile by ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable("id") Long id) {
        User user = userManagementService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserProfileResponse response = authService.getCurrentUserProfile(user);
        return ResponseEntity.ok(response);
    }
}