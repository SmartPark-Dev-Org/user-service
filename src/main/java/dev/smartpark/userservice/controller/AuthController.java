package dev.smartpark.userservice.controller;

import dev.smartpark.userservice.dto.AuthResponseDTO;
import dev.smartpark.userservice.dto.LoginRequestDTO;
import dev.smartpark.userservice.dto.UserRequestDTO;
import dev.smartpark.userservice.dto.RefreshTokenRequestDTO;
import dev.smartpark.userservice.dto.UserResponseDTO;
import dev.smartpark.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return userService.login(request);
    }

    @PostMapping("/refresh-token")
    public AuthResponseDTO refreshToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        return userService.refreshToken(request.getRefreshToken());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@Valid @RequestBody UserRequestDTO request) {
        return userService.createUser(request);
    }
}
