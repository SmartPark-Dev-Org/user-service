package dev.smartpark.userservice.controller;

import dev.smartpark.userservice.dto.UserRequestDTO;
import dev.smartpark.userservice.dto.UserResponseDTO;
import dev.smartpark.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user management.
 * Base path: {@code /api/v1/users}
 *
 * <p>Semantic aliases:
 * <ul>
 *   <li>POST /register  — public-facing registration alias for POST /</li>
 *   <li>GET  /profile/{userId} — semantic profile fetch alias for GET /{id}</li>
 * </ul>
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** POST /api/v1/users — create user (internal / admin use). */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    /**
     * POST /api/v1/users/register
     * Public-facing user self-registration endpoint.
     * Defaults role to ROLE_USER regardless of what is supplied in the payload.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO request) {
        // Force ROLE_USER on self-registration — role elevation must go through admin path
        UserRequestDTO safeRequest = new UserRequestDTO(
                request.username(),
                request.password(),
                request.email(),
                request.fullName(),
                request.phoneNumber(),
                dev.smartpark.userservice.enums.UserRole.ROLE_USER
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(safeRequest));
    }

    /**
     * GET /api/v1/users/profile/{userId}
     * Semantic profile endpoint — used by frontend and other services.
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserResponseDTO> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
