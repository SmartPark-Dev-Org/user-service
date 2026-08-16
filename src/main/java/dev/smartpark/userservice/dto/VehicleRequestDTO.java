package dev.smartpark.userservice.dto;

import dev.smartpark.userservice.enums.VehicleType;
import jakarta.validation.constraints.*;

/**
 * Immutable DTO (Java Record) for registering a new vehicle.
 */
public record VehicleRequestDTO(

        @NotBlank(message = "License plate is required")
        @Size(max = 20, message = "License plate must not exceed 20 characters")
        @Pattern(regexp = "^[A-Za-z0-9\\-\\s]+$", message = "License plate contains invalid characters")
        String licensePlate,

        @NotBlank(message = "Vehicle make is required")
        @Size(max = 60)
        String make,

        @NotBlank(message = "Vehicle model is required")
        @Size(max = 60)
        String model,

        @Size(max = 30)
        String color,

        @Min(value = 1886, message = "Year must be 1886 or later")
        @Max(value = 2100, message = "Year is out of range")
        Integer year,

        VehicleType vehicleType

) {}
