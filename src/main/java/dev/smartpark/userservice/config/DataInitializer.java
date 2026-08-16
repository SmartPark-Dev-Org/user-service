package dev.smartpark.userservice.config;

import dev.smartpark.userservice.entity.User;
import dev.smartpark.userservice.enums.UserRole;
import dev.smartpark.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Data seeder that runs at application startup (after JPA schema is ready).
 *
 * <p>Seeds:
 * <ul>
 *   <li>1 root user  — username: {@code root},   password: {@code Manu2006}</li>
 *   <li>2 admin users — username: {@code admin1}, {@code admin2}, password: {@code Admin@2006}</li>
 * </ul>
 *
 * <p>This is idempotent — skips seeding if any user already exists.
 */
@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            log.info("DataInitializer: database already seeded — skipping.");
            return;
        }

        log.info("DataInitializer: seeding initial users...");

        List<User> seedUsers = List.of(
            User.builder()
                .username("root")
                .password(passwordEncoder.encode("Manu2006"))
                .email("root@smartpark.dev")
                .fullName("System Root")
                .phoneNumber("+94771000000")
                .role(UserRole.ROLE_ROOT)
                .active(true)
                .build(),

            User.builder()
                .username("admin1")
                .password(passwordEncoder.encode("Admin@2006"))
                .email("admin1@smartpark.dev")
                .fullName("Admin User One")
                .phoneNumber("+94771000001")
                .role(UserRole.ROLE_ADMIN)
                .active(true)
                .build(),

            User.builder()
                .username("admin2")
                .password(passwordEncoder.encode("Admin@2006"))
                .email("admin2@smartpark.dev")
                .fullName("Admin User Two")
                .phoneNumber("+94771000002")
                .role(UserRole.ROLE_ADMIN)
                .active(true)
                .build()
        );

        userRepository.saveAll(seedUsers);
        log.info("DataInitializer: seeded {} users (root + 2 admins).", seedUsers.size());
    }
}
