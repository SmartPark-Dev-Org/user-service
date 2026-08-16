package dev.smartpark.userservice.dto;

import dev.smartpark.userservice.enums.UserRole;

import java.time.LocalDateTime;

import lombok.Builder;

/**
 * Immutable DTO (Java Record) for API responses — password is intentionally excluded.
 */
@Builder
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
