package com.pokedex.controller.impl;

import com.pokedex.controller.dto.request.UpdateUserRequest;
import com.pokedex.controller.dto.response.UserResponse;
import com.pokedex.core.exception.BusinessException;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.repository.relational.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController")
class UserControllerTest {

    @Mock private UserJpaRepository userRepository;

    @InjectMocks private UserController controller;

    private UserEntity adminUser;
    private UserEntity trainerUser;

    @BeforeEach
    void setUp() {
        adminUser = UserEntity.builder()
                .id(1L).username("admin").email("admin@pokedex.com")
                .password("encoded").role(UserEntity.Role.ADMIN)
                .build();

        trainerUser = UserEntity.builder()
                .id(2L).username("ash").email("ash@pokemon.com")
                .password("encoded").role(UserEntity.Role.TRAINER)
                .build();
    }

    @Nested
    @DisplayName("GET /v1/admin/users")
    class FindAllUsers {

        @Test
        @DisplayName("Dado que existen usuarios, cuando se listan, entonces retorna 200 con la página de respuestas")
        void givenUsersExist_whenFindAll_thenReturns200WithPage() {
            Pageable pageable = PageRequest.of(0, 20);
            Page<UserEntity> entityPage = new PageImpl<>(List.of(adminUser, trainerUser));
            when(userRepository.findAll(pageable)).thenReturn(entityPage);

            ResponseEntity<Page<UserResponse>> response = controller.findAllUsers(pageable);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getContent()).hasSize(2);
            assertThat(response.getBody().getContent().get(0).email()).isEqualTo("admin@pokedex.com");
            assertThat(response.getBody().getContent().get(0).role()).isEqualTo("ADMIN");
            assertThat(response.getBody().getContent().get(1).email()).isEqualTo("ash@pokemon.com");
        }

        @Test
        @DisplayName("Dado que no existen usuarios, cuando se listan, entonces retorna 200 con página vacía")
        void givenNoUsers_whenFindAll_thenReturns200WithEmptyPage() {
            Pageable pageable = PageRequest.of(0, 20);
            when(userRepository.findAll(pageable)).thenReturn(Page.empty());

            ResponseEntity<Page<UserResponse>> response = controller.findAllUsers(pageable);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getContent()).isEmpty();
        }
    }

    @Nested
    @DisplayName("PUT /v1/admin/users/{id}")
    class UpdateUser {

        @Test
        @DisplayName("Dado un cambio de rol válido para un TRAINER, cuando se actualiza, entonces retorna 200 con el usuario actualizado")
        void givenValidRoleChange_whenUpdate_thenReturns200() {
            UpdateUserRequest request = new UpdateUserRequest("TRAINER", null);
            when(userRepository.findById(2L)).thenReturn(Optional.of(trainerUser));
            when(userRepository.save(trainerUser)).thenReturn(trainerUser);

            ResponseEntity<UserResponse> response = controller.updateUser(2L, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().email()).isEqualTo("ash@pokemon.com");
            assertThat(response.getBody().role()).isEqualTo("TRAINER");
            verify(userRepository).save(trainerUser);
        }

        @Test
        @DisplayName("Dado un cambio de estado activo para un TRAINER, cuando se actualiza, entonces retorna 200")
        void givenActiveChangeForTrainer_whenUpdate_thenReturns200() {
            UpdateUserRequest request = new UpdateUserRequest(null, false);
            when(userRepository.findById(2L)).thenReturn(Optional.of(trainerUser));
            when(userRepository.save(trainerUser)).thenReturn(trainerUser);

            ResponseEntity<UserResponse> response = controller.updateUser(2L, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(userRepository).save(trainerUser);
        }

        @Test
        @DisplayName("Dado múltiples ADMINs activos, cuando se cambia rol a uno de ellos, entonces retorna 200")
        void givenMultipleAdmins_whenChangeRole_thenReturns200() {
            UpdateUserRequest request = new UpdateUserRequest("TRAINER", null);
            when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
            when(userRepository.countByRole(UserEntity.Role.ADMIN)).thenReturn(2L);
            when(userRepository.save(adminUser)).thenReturn(adminUser);

            ResponseEntity<UserResponse> response = controller.updateUser(1L, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        @DisplayName("Dado un usuario inexistente, cuando se actualiza, entonces lanza ResourceNotFoundException")
        void givenNonExistingUser_whenUpdate_thenThrowsResourceNotFoundException() {
            UpdateUserRequest request = new UpdateUserRequest("TRAINER", null);
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> controller.updateUser(99L, request));
        }

        @Test
        @DisplayName("Dado un rol inválido, cuando se actualiza, entonces lanza BusinessException con código INVALID_ROLE")
        void givenInvalidRole_whenUpdate_thenThrowsInvalidRoleException() {
            UpdateUserRequest request = new UpdateUserRequest("SUPERUSER", null);
            when(userRepository.findById(2L)).thenReturn(Optional.of(trainerUser));

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> controller.updateUser(2L, request));

            assertThat(ex.getErrorCode()).isEqualTo("INVALID_ROLE");
        }

        @Test
        @DisplayName("Dado el único ADMIN, cuando se le cambia el rol, entonces lanza BusinessException con código LAST_ADMIN (RN-40)")
        void givenLastAdmin_whenChangeRole_thenThrowsLastAdminException() {
            UpdateUserRequest request = new UpdateUserRequest("TRAINER", null);
            when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
            when(userRepository.countByRole(UserEntity.Role.ADMIN)).thenReturn(1L);

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> controller.updateUser(1L, request));

            assertThat(ex.getErrorCode()).isEqualTo("LAST_ADMIN");
        }

        @Test
        @DisplayName("Dado el único ADMIN activo, cuando se desactiva, entonces lanza BusinessException con código LAST_ACTIVE_ADMIN (RN-38)")
        void givenLastActiveAdmin_whenDeactivate_thenThrowsLastActiveAdminException() {
            UpdateUserRequest request = new UpdateUserRequest(null, false);
            when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
            when(userRepository.countByRoleAndActive(UserEntity.Role.ADMIN, true)).thenReturn(1L);

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> controller.updateUser(1L, request));

            assertThat(ex.getErrorCode()).isEqualTo("LAST_ACTIVE_ADMIN");
        }
    }
}
