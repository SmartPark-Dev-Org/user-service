package dev.smartpark.userservice.entity;

import dev.smartpark.userservice.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * JPA entity representing a vehicle registered to a SmartPark user.
 *
 * <p>A user may own multiple vehicles. The {@code licensePlate} must be globally unique
 * within the platform. Cross-service references (e.g., in reservation-billing-service)
 * use {@code id} as a plain Long identifier — no JPA join across services.
 */
@Entity
@Table(
    name = "vehicles",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_vehicles_license_plate", columnNames = "license_plate")
    }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many vehicles belong to one user.
     * LAZY fetch avoids N+1 on bulk queries.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Unique license plate number, e.g., "ABC-1234". */
    @Column(name = "license_plate", nullable = false, length = 20)
    private String licensePlate;

    @Column(nullable = false, length = 60)
    private String make;          // e.g., "Toyota"

    @Column(nullable = false, length = 60)
    private String model;         // e.g., "Corolla"

    @Column(length = 30)
    private String color;

    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private VehicleType vehicleType = VehicleType.CAR;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
