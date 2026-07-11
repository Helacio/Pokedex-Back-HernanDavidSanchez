package com.pokedex.security;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock private UserJpaRepository userRepository;
    @InjectMocks private UserDetailsServiceImpl userDetailsService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .username("ash")
                .email("ash@pokemon.com")
                .password("encodedPassword")
                .role(UserEntity.Role.TRAINER)
                .build();
    }

    @Nested
    @DisplayName("loadUserByUsername")
    class LoadUserByUsername {

        @Test
        @DisplayName("Dado un email existente, cuando se carga el usuario, entonces retorna UserDetails con rol correcto")
        void givenExistingEmail_whenLoad_thenReturnsUserDetailsWithRole() {
            when(userRepository.findByEmail("ash@pokemon.com")).thenReturn(Optional.of(userEntity));

            UserDetails result = userDetailsService.loadUserByUsername("ash@pokemon.com");

            assertThat(result.getUsername()).isEqualTo("ash@pokemon.com");
            assertThat(result.getPassword()).isEqualTo("encodedPassword");
            assertThat(result.getAuthorities()).anyMatch(a -> a.getAuthority().equals("ROLE_TRAINER"));
        }

        @Test
        @DisplayName("Dado un email de ADMIN, cuando se carga el usuario, entonces retorna rol ROLE_ADMIN")
        void givenAdminEmail_whenLoad_thenReturnsAdminRole() {
            UserEntity admin = UserEntity.builder()
                    .username("giovanni")
                    .email("giovanni@rocket.com")
                    .password("encodedPassword")
                    .role(UserEntity.Role.ADMIN)
                    .build();
            when(userRepository.findByEmail("giovanni@rocket.com")).thenReturn(Optional.of(admin));

            UserDetails result = userDetailsService.loadUserByUsername("giovanni@rocket.com");

            assertThat(result.getAuthorities()).anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }

        @Test
        @DisplayName("Dado un email inexistente, cuando se carga el usuario, entonces lanza UsernameNotFoundException")
        void givenNonExistingEmail_whenLoad_thenThrowsUsernameNotFoundException() {
            when(userRepository.findByEmail("unknown@pokemon.com")).thenReturn(Optional.empty());

            assertThrows(UsernameNotFoundException.class,
                    () -> userDetailsService.loadUserByUsername("unknown@pokemon.com"));
        }
    }
}
