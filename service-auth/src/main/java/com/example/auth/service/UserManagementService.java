package com.example.auth.service;

import com.example.auth.dto.UserListResponse;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserManagementService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserManagementService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Page<UserListResponse> getAllUsers(int page, int size, String sort) {
        Sort sortObj = Sort.by(Sort.Direction.DESC, sort != null ? sort : "createdAt");
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::mapToUserListResponse);
    }

    public UserListResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return mapToUserListResponse(user);
    }

    @Transactional
    public void assignRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        Set<Role> roles = new HashSet<>(user.getRoles());
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }

    private UserListResponse mapToUserListResponse(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserListResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.isActive(),
                roles,
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
