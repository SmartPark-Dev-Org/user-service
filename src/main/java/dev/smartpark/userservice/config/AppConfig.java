package dev.smartpark.userservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Security and JPA auditing configuration.
 *
 * <p>Security is currently permissive (permit-all) — a JWT filter should be
 * wired here in a future iteration when authentication is implemented.
 */
@Configuration
@EnableJpaAuditing
public class AppConfig {
}
