package dev.smartpark.userservice.dto;

import dev.smartpark.userservice.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Immutable DTO (Java Record) for creating or updating a user.
 * Validation annotations are enforced at the Controller layer via {@code @Valid}.
 */
public record UserRequestDTO(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
        String password,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Full name is required")
        @Size(max = 100, message = "Full name must not exceed 100 characters")
        String fullName,

        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number format")
        String phoneNumber,

        UserRole role

) {}
