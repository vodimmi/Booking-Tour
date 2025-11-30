package com.example.auth.controller;

import com.example.auth.dto.AssignRoleRequest;
import com.example.auth.dto.MessageResponse;
import com.example.auth.entity.Role;
import com.example.auth.repository.RoleRepository;
import com.example.auth.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/roles")
@Tag(name = "Role Management", description = "Role management APIs")
public class RoleController {

    private final RoleRepository roleRepository;
    private final UserManagementService userManagementService;

    public RoleController(RoleRepository roleRepository, UserManagementService userManagementService) {
        this.roleRepository = roleRepository;
        this.userManagementService = userManagementService;
    }

    @GetMapping
    @Operation(summary = "List all roles", description = "Get list of all available roles")
    @ApiResponse(responseCode = "200", description = "Roles retrieved successfully")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Assign role to user (ADMIN only)", description = "Assign a role to a specific user")
    @ApiResponse(responseCode = "200", description = "Role assigned successfully")
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    @ApiResponse(responseCode = "404", description = "User or role not found")
    public ResponseEntity<MessageResponse> assignRole(@Valid @RequestBody AssignRoleRequest request) {
        userManagementService.assignRole(request.getUserId(), request.getRole());
        return ResponseEntity.ok(new MessageResponse("Role assigned successfully"));
    }
}
