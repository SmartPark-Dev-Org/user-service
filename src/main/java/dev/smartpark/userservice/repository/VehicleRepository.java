package dev.smartpark.userservice.repository;

import dev.smartpark.userservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findAllByUserId(Long userId);

    List<Vehicle> findAllByUserIdAndActiveTrue(Long userId);

    boolean existsByLicensePlate(String licensePlate);

    boolean existsByLicensePlateAndIdNot(String licensePlate, Long excludeId);
}
