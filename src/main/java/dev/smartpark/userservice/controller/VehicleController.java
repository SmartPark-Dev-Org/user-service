package dev.smartpark.userservice.controller;

import java.util.List;

import dev.smartpark.userservice.dto.VehicleRequestDTO;
import dev.smartpark.userservice.dto.VehicleResponseDTO;
import dev.smartpark.userservice.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for vehicle registration and retrieval.
 *
 * <p>All endpoints are scoped to a user via {@code /api/v1/users/{userId}/vehicles}.
 * Ownership is verified inside the service layer — the userId path variable is not
 * taken on trust; it is cross-checked against the stored vehicle record.
 */
@RestController
@RequestMapping("/users/{userId}/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * POST /api/v1/users/{userId}/vehicles
     * Register a new vehicle for the specified user.
     */
    @PostMapping
    public ResponseEntity<VehicleResponseDTO> registerVehicle(
            @PathVariable Long userId,
            @Valid @RequestBody VehicleRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehicleService.registerVehicle(userId, request));
    }

    /**
     * GET /api/v1/users/{userId}/vehicles
     * List all active vehicles owned by a user.
     */
    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> getVehiclesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(vehicleService.getVehiclesByUser(userId));
    }

    /**
     * GET /api/v1/users/{userId}/vehicles/{vehicleId}
     * Retrieve a specific vehicle (with ownership check).
     */
    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDTO> getVehicleById(
            @PathVariable Long userId,
            @PathVariable Long vehicleId) {
        return ResponseEntity.ok(vehicleService.getVehicleById(userId, vehicleId));
    }

    /**
     * PATCH /api/v1/users/{userId}/vehicles/{vehicleId}/deactivate
     * Soft-delete a vehicle (with ownership check).
     */
    @PatchMapping("/{vehicleId}/deactivate")
    public ResponseEntity<Void> deactivateVehicle(
            @PathVariable Long userId,
            @PathVariable Long vehicleId) {
        vehicleService.deactivateVehicle(userId, vehicleId);
        return ResponseEntity.noContent().build();
    }
}
