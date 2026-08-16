package dev.smartpark.userservice.dto;

import dev.smartpark.userservice.enums.VehicleType;

import java.time.LocalDateTime;

/**
 * Immutable DTO (Java Record) for vehicle API responses.
 * Contains the owner's userId for cross-service convenience.
 */
public record VehicleResponseDTO(
        Long id,
        Long userId,
        String licensePlate,
        String make,
        String model,
        String color,
        Integer year,
        VehicleType vehicleType,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
