package dev.smartpark.userservice.dto;

import dev.smartpark.userservice.enums.UserRole;

import java.time.LocalDateTime;

/**
 * Immutable DTO (Java Record) for API responses — password is intentionally excluded.
 */
public record UserResponseDTO(
        Long id,
        String username,
        String email,
        String fullName,
        String phoneNumber,
        UserRole role,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
