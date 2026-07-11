package com.pokedex.core.service.impl;

import com.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.repository.relational.UserJpaRepository;
import com.pokedex.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImpl")
class AuthServiceImplTest {

    @Mock private UserJpaRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private UserDetailsService userDetailsService;
    @Mock private AuthenticationManager authenticationManager;
    @InjectMocks private AuthServiceImpl authService;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = new User("ash@pokemon.com", "encodedPassword",
                List.of(new SimpleGrantedAuthority("ROLE_TRAINER")));
    }

    @Nested
    @DisplayName("register")
    class Register {

        @Test
        @DisplayName("Dado un email nuevo, cuando se registra, entonces guarda el usuario y retorna un JWT")
        void givenNewEmail_whenRegister_thenSavesUserAndReturnsJwt() {
            when(userRepository.findByEmail("ash@pokemon.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
            when(userRepository.save(any(UserEntity.class))).thenReturn(mock(UserEntity.class));
            when(userDetailsService.loadUserByUsername("ash@pokemon.com")).thenReturn(userDetails);
            when(jwtService.generateToken(userDetails)).thenReturn("jwt.token.here");

            String token = authService.register("ash", "ash@pokemon.com", "password123");

            assertThat(token).isEqualTo("jwt.token.here");
            verify(userRepository).save(any(UserEntity.class));
            verify(passwordEncoder).encode("password123");
            verify(jwtService).generateToken(userDetails);
        }

        @Test
        @DisplayName("Dado un email ya existente, cuando se registra, entonces lanza DuplicateResourceException")
        void givenExistingEmail_whenRegister_thenThrowsDuplicateResourceException() {
            UserEntity existing = UserEntity.builder()
                    .username("ash").email("ash@pokemon.com")
                    .password("encoded").role(UserEntity.Role.TRAINER).build();
            when(userRepository.findByEmail("ash@pokemon.com")).thenReturn(Optional.of(existing));

            assertThrows(DuplicateResourceException.class,
                    () -> authService.register("ash", "ash@pokemon.com", "password123"));

            verify(userRepository, never()).save(any());
            verify(jwtService, never()).generateToken(any());
        }

        @Test
        @DisplayName("Dado un email nuevo, cuando se registra, entonces el rol asignado es TRAINER")
        void givenNewEmail_whenRegister_thenAssignedRoleIsTrainer() {
            when(userRepository.findByEmail("ash@pokemon.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode(any())).thenReturn("encoded");
            when(userDetailsService.loadUserByUsername("ash@pokemon.com")).thenReturn(userDetails);
            when(jwtService.generateToken(any())).thenReturn("token");

            authService.register("ash", "ash@pokemon.com", "password123");

            verify(userRepository).save(argThat(entity ->
                    entity.getRole() == UserEntity.Role.TRAINER
                            && entity.getEmail().equals("ash@pokemon.com")
                            && entity.getUsername().equals("ash")));
        }
    }

    @Nested
    @DisplayName("login")
    class Login {

        @Test
        @DisplayName("Dado credenciales válidas, cuando se hace login, entonces retorna un JWT")
        void givenValidCredentials_whenLogin_thenReturnsJwt() {
            when(userDetailsService.loadUserByUsername("ash@pokemon.com")).thenReturn(userDetails);
            when(jwtService.generateToken(userDetails)).thenReturn("jwt.token.here");

            String token = authService.login("ash@pokemon.com", "password123");

            assertThat(token).isEqualTo("jwt.token.here");
            verify(authenticationManager).authenticate(
                    new UsernamePasswordAuthenticationToken("ash@pokemon.com", "password123"));
            verify(jwtService).generateToken(userDetails);
        }

        @Test
        @DisplayName("Dado credenciales inválidas, cuando se hace login, entonces lanza BadCredentialsException")
        void givenInvalidCredentials_whenLogin_thenThrowsBadCredentialsException() {
            doThrow(new BadCredentialsException("Credenciales incorrectas"))
                    .when(authenticationManager)
                    .authenticate(any(UsernamePasswordAuthenticationToken.class));

            assertThrows(BadCredentialsException.class,
                    () -> authService.login("ash@pokemon.com", "wrong"));

            verify(jwtService, never()).generateToken(any());
        }
    }
}
