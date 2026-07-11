package com.pokedex.core.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User")
class UserTest {

    @Nested
    @DisplayName("builder")
    class Builder {

        @Test
        @DisplayName("Dado todos los campos, cuando se construye, entonces los getters retornan los valores correctos")
        void givenAllFields_whenBuilt_thenGettersReturnCorrectValues() {
            LocalDateTime now = LocalDateTime.of(2026, 1, 1, 10, 0);

            User user = User.builder()
                    .id(1L)
                    .username("ash")
                    .email("ash@pokemon.com")
                    .role("TRAINER")
                    .createdAt(now)
                    .build();

            assertThat(user.getId()).isEqualTo(1L);
            assertThat(user.getUsername()).isEqualTo("ash");
            assertThat(user.getEmail()).isEqualTo("ash@pokemon.com");
            assertThat(user.getRole()).isEqualTo("TRAINER");
            assertThat(user.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Dado un usuario ADMIN, cuando se construye, entonces el rol es ADMIN")
        void givenAdminRole_whenBuilt_thenRoleIsAdmin() {
            User admin = User.builder()
                    .id(2L).username("giovanni").email("giovanni@rocket.com")
                    .role("ADMIN").build();

            assertThat(admin.getRole()).isEqualTo("ADMIN");
        }

        @Test
        @DisplayName("Dado dos instancias con los mismos valores, cuando se comparan, entonces son iguales")
        void givenTwoInstancesWithSameValues_whenCompared_thenAreEqual() {
            LocalDateTime now = LocalDateTime.of(2026, 1, 1, 10, 0);

            User u1 = User.builder().id(1L).username("ash").email("ash@pokemon.com")
                    .role("TRAINER").createdAt(now).build();
            User u2 = User.builder().id(1L).username("ash").email("ash@pokemon.com")
                    .role("TRAINER").createdAt(now).build();

            assertThat(u1).isEqualTo(u2);
            assertThat(u1.hashCode()).isEqualTo(u2.hashCode());
        }

        @Test
        @DisplayName("Dado createdAt nulo, cuando se construye, entonces la fecha es nula")
        void givenNullCreatedAt_whenBuilt_thenCreatedAtIsNull() {
            User user = User.builder().id(1L).username("ash").email("ash@pokemon.com")
                    .role("TRAINER").build();

            assertThat(user.getCreatedAt()).isNull();
        }
    }
}
