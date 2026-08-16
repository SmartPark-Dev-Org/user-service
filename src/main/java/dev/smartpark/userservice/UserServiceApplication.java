package dev.smartpark.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SmartPark :: User Service
 *
 * <p>Manages user accounts, roles (ROOT / ADMIN / USER), and authentication data.
 * Registers with Eureka and fetches centralized config from the Config Server.
 *
 * <p>Startup order: Config Server → Eureka Server → API Gateway → User Service
 *
 * @author SmartPark Platform Team
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
