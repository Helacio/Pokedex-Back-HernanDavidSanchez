package com.pokedex.controller.impl;

import com.pokedex.controller.api.UserApi;
import com.pokedex.controller.dto.request.UpdateUserRequest;
import com.pokedex.controller.dto.response.UserResponse;
import com.pokedex.core.exception.BusinessException;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.repository.relational.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<UserResponse> updateUser(Long id, UpdateUserRequest request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));

        // Cambia el rol si viene en el request
        if (request.role() != null) {
            UserEntity.Role nuevoRol = parseRole(request.role());

            // RN-40: No permitir que el único ADMIN pierda su rol
            if (user.getRole() == UserEntity.Role.ADMIN && nuevoRol != UserEntity.Role.ADMIN) {
                long totalAdmins = userRepository.countByRole(UserEntity.Role.ADMIN);
                if (totalAdmins <= 1) {
                    throw new BusinessException(
                            "No se puede cambiar el rol del único administrador activo", "LAST_ADMIN");
                }
            }
            user.updateRole(nuevoRol);
        }

        // Cambia el estado activo si viene en el request
        if (request.active() != null) {

            // RN-38: No desactivar al único administrador activo
            if (!request.active() && user.getRole() == UserEntity.Role.ADMIN) {
                long adminsActivos = userRepository.countByRoleAndActive(UserEntity.Role.ADMIN, true);
                if (adminsActivos <= 1) {
                    throw new BusinessException(
                            "No se puede desactivar al único administrador activo", "LAST_ACTIVE_ADMIN");
                }
            }
            user.updateActive(request.active());
        }

        UserEntity saved = userRepository.save(user);
        return ResponseEntity.ok(toResponse(saved));
    }

    private UserEntity.Role parseRole(String role) {
        try {
            return UserEntity.Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(
                    "Rol inválido: " + role + ". Los valores válidos son: GUEST, TRAINER, ADMIN",
                    "INVALID_ROLE");
        }
    }

    private UserResponse toResponse(UserEntity entity) {
        return new UserResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRole().name(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }
}
