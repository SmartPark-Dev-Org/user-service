package dev.smartpark.userservice.enums;

/**
 * User roles within the SmartPark platform.
 *
 * <ul>
 *   <li>{@code ROLE_ROOT}  — System superuser; full access to all operations.</li>
 *   <li>{@code ROLE_ADMIN} — Administrative users; manage facilities and reservations.</li>
 *   <li>{@code ROLE_USER}  — Regular end users; can reserve parking slots.</li>
 * </ul>
 */
public enum UserRole {
    ROLE_ROOT,
    ROLE_ADMIN,
    ROLE_USER
}
