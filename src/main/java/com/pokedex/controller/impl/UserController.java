package com.pokedex.controller.impl;

import com.pokedex.controller.api.UserApi;
import com.pokedex.controller.dto.response.UserResponse;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.repository.relational.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserJpaRepository userRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> findAllUsers(Pageable pageable) {
        Page<UserResponse> users = userRepository.findAll(pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(users);
    }

    private UserResponse toResponse(UserEntity entity) {
        return new UserResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRole().name(),
                entity.getCreatedAt()
        );
    }
}
