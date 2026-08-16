package dev.smartpark.userservice.service.impl;

import dev.smartpark.userservice.dto.AuthResponseDTO;
import dev.smartpark.userservice.dto.LoginRequestDTO;
import dev.smartpark.userservice.dto.UserRequestDTO;
import dev.smartpark.userservice.dto.UserResponseDTO;
import dev.smartpark.userservice.entity.User;
import dev.smartpark.userservice.enums.UserRole;
import dev.smartpark.userservice.exception.DuplicateResourceException;
import dev.smartpark.userservice.exception.ResourceNotFoundException;
import dev.smartpark.userservice.repository.UserRepository;
import dev.smartpark.userservice.service.UserService;
import dev.smartpark.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getRole().name());

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(toResponse(user))
                .build();
    }

    @Override
    public AuthResponseDTO refreshToken(String refreshToken) {
        if (!jwtUtil.isRefreshTokenMathematicallyValid(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String username = jwtUtil.extractUsernameFromRefreshToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isActive()) {
            throw new IllegalArgumentException("User is not active");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole().name());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getRole().name());

        return AuthResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .user(toResponse(user))
                .build();
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already exists: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already registered: " + request.email());
        }

        UserRole role = request.role() != null ? request.role() : UserRole.ROLE_USER;

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .fullName(request.fullName())
                .phoneNumber(request.phoneNumber())
                .role(role)
                .build();

        User saved = userRepository.save(user);
        log.info("User created: id={}, username={}, role={}", saved.getId(), saved.getUsername(), saved.getRole());
        return toResponse(saved);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        return toResponse(findUserOrThrow(id));
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        return toResponse(userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username)));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<UserResponseDTO> getActiveUsers() {
        return userRepository.findAllByActive(true).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO request) {
        User user = findUserOrThrow(id);

        if (!user.getUsername().equals(request.username()) && userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already taken: " + request.username());
        }
        if (!user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already registered: " + request.email());
        }

        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setFullName(request.fullName());
        user.setPhoneNumber(request.phoneNumber());
        if (request.role() != null) user.setRole(request.role());

        return toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        User user = findUserOrThrow(id);
        user.setActive(false);
        userRepository.save(user);
        log.info("User deactivated: id={}", id);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        findUserOrThrow(id); // ensure exists
        userRepository.deleteById(id);
        log.info("User deleted: id={}", id);
    }

    // ── Private Helpers ────────────────────────────────────

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
