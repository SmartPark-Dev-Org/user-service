package dev.smartpark.userservice.service;

import dev.smartpark.userservice.dto.AuthResponseDTO;
import dev.smartpark.userservice.dto.LoginRequestDTO;
import dev.smartpark.userservice.dto.UserRequestDTO;
import dev.smartpark.userservice.dto.UserResponseDTO;

import java.util.List;

/**
 * Contract for all user management operations.
 * Implementations must ensure passwords are never included in responses.
 */
public interface UserService {

    AuthResponseDTO login(LoginRequestDTO request);

    AuthResponseDTO refreshToken(String refreshToken);

    UserResponseDTO createUser(UserRequestDTO request);

    UserResponseDTO getUserById(Long id);

    UserResponseDTO getUserByUsername(String username);

    List<UserResponseDTO> getAllUsers();

    List<UserResponseDTO> getActiveUsers();

    UserResponseDTO updateUser(Long id, UserRequestDTO request);

    void deactivateUser(Long id);

    void deleteUser(Long id);
}
