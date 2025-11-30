package com.example.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AssignRoleRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Role is required")
    private String role;

    // Constructors
    public AssignRoleRequest() {
    }

    public AssignRoleRequest(Long userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
