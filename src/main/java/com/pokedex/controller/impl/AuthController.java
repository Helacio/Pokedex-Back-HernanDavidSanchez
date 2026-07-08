package com.pokedex.controller.impl;

import com.pokedex.controller.api.AuthApi;
import com.pokedex.controller.dto.request.LoginRequest;
import com.pokedex.controller.dto.request.RegisterRequest;
import com.pokedex.controller.dto.response.AuthResponse;
import com.pokedex.core.service.interfaces.AuthService;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.repository.relational.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final UserJpaRepository userRepository;

    @Override
    public ResponseEntity<AuthResponse> register(RegisterRequest request) {
        String token = authService.register(request.username(), request.email(), request.password());
        UserEntity user = userRepository.findByEmail(request.email()).orElseThrow();
        AuthResponse response = new AuthResponse(token, user.getEmail(), user.getRole().name());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<AuthResponse> login(LoginRequest request) {
        String token = authService.login(request.email(), request.password());
        UserEntity user = userRepository.findByEmail(request.email()).orElseThrow();
        AuthResponse response = new AuthResponse(token, user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> oauth2Google() {
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }
}
