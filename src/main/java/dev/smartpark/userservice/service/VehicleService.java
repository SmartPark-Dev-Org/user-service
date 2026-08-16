package dev.smartpark.userservice.service;

import dev.smartpark.userservice.dto.VehicleRequestDTO;
import dev.smartpark.userservice.dto.VehicleResponseDTO;

import java.util.List;

/**
 * Contract for vehicle registration and management operations.
 * Vehicles are always scoped to an owning user.
 */
public interface VehicleService {

    /**
     * Registers a new vehicle under the given user.
     *
     * @throws dev.smartpark.userservice.exception.ResourceNotFoundException if userId not found
     * @throws dev.smartpark.userservice.exception.DuplicateResourceException if licensePlate already exists
     */
    VehicleResponseDTO registerVehicle(Long userId, VehicleRequestDTO request);

    /**
     * Returns all active vehicles belonging to a user.
     */
    List<VehicleResponseDTO> getVehiclesByUser(Long userId);

    /**
     * Returns a single vehicle by its ID, verifying it belongs to the given user.
     */
    VehicleResponseDTO getVehicleById(Long userId, Long vehicleId);

    /**
     * Soft-deletes (deactivates) a vehicle.
     */
    void deactivateVehicle(Long userId, Long vehicleId);
}
