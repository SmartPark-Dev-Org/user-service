package dev.smartpark.userservice.service.impl;

import java.util.List;

import dev.smartpark.userservice.dto.VehicleRequestDTO;
import dev.smartpark.userservice.dto.VehicleResponseDTO;
import dev.smartpark.userservice.entity.User;
import dev.smartpark.userservice.entity.Vehicle;
import dev.smartpark.userservice.enums.VehicleType;
import dev.smartpark.userservice.exception.DuplicateResourceException;
import dev.smartpark.userservice.exception.ResourceNotFoundException;
import dev.smartpark.userservice.repository.UserRepository;
import dev.smartpark.userservice.repository.VehicleRepository;
import dev.smartpark.userservice.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link VehicleService}.
 *
 * <p>Every operation validates user ownership to prevent cross-user data leakage.
 * All read operations run in a read-only transaction for optimal Hibernate performance.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public VehicleResponseDTO registerVehicle(Long userId, VehicleRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        if (vehicleRepository.existsByLicensePlate(request.licensePlate())) {
            throw new DuplicateResourceException(
                    "License plate already registered: " + request.licensePlate());
        }

        VehicleType type = request.vehicleType() != null ? request.vehicleType() : VehicleType.CAR;

        Vehicle vehicle = Vehicle.builder()
                .user(user)
                .licensePlate(request.licensePlate().toUpperCase().trim())
                .make(request.make())
                .model(request.model())
                .color(request.color())
                .year(request.year())
                .vehicleType(type)
                .build();

        Vehicle saved = vehicleRepository.save(vehicle);
        log.info("Vehicle registered: id={}, plate={}, owner={}", saved.getId(), saved.getLicensePlate(), userId);
        return toResponse(saved);
    }

    @Override
    public List<VehicleResponseDTO> getVehiclesByUser(Long userId) {
        // Verify user exists before returning empty list
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found: " + userId);
        }
        return vehicleRepository.findAllByUserIdAndActiveTrue(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public VehicleResponseDTO getVehicleById(Long userId, Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + vehicleId));

        if (!vehicle.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Vehicle " + vehicleId + " does not belong to user " + userId);
        }
        return toResponse(vehicle);
    }

    @Override
    @Transactional
    public void deactivateVehicle(Long userId, Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + vehicleId));

        if (!vehicle.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Vehicle " + vehicleId + " does not belong to user " + userId);
        }
        vehicle.setActive(false);
        vehicleRepository.save(vehicle);
        log.info("Vehicle deactivated: id={}", vehicleId);
    }

    // ── Private Helpers ────────────────────────────────────

    private VehicleResponseDTO toResponse(Vehicle v) {
        return new VehicleResponseDTO(
                v.getId(),
                v.getUser().getId(),
                v.getLicensePlate(),
                v.getMake(),
                v.getModel(),
                v.getColor(),
                v.getYear(),
                v.getVehicleType(),
                v.isActive(),
                v.getCreatedAt(),
                v.getUpdatedAt()
        );
    }
}
