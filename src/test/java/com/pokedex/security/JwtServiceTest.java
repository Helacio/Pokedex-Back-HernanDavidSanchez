package com.pokedex.security;

import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        String secret = Encoders.BASE64.encode(Keys.hmacShaKeyFor(new byte[32]).getEncoded());
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        ReflectionTestUtils.setField(jwtService, "expirationMs", 86400000L);

        userDetails = new User("ash@pokemon.com", "password",
                List.of(new SimpleGrantedAuthority("ROLE_TRAINER")));
    }

    @Nested
    @DisplayName("generateToken")
    class GenerateToken {

        @Test
        @DisplayName("Dado un usuario válido, cuando se genera el token, entonces no es nulo")
        void givenValidUser_whenGenerateToken_thenTokenIsNotNull() {
            String token = jwtService.generateToken(userDetails);

            assertThat(token).isNotNull().isNotBlank();
        }

        @Test
        @DisplayName("Dado un usuario válido, cuando se genera el token, entonces contiene el username")
        void givenValidUser_whenGenerateToken_thenContainsUsername() {
            String token = jwtService.generateToken(userDetails);

            assertThat(jwtService.extractUsername(token)).isEqualTo("ash@pokemon.com");
        }
    }

    @Nested
    @DisplayName("isTokenValid")
    class IsTokenValid {

        @Test
        @DisplayName("Dado un token válido y el mismo usuario, cuando se valida, entonces retorna true")
        void givenValidToken_whenValidate_thenReturnsTrue() {
            String token = jwtService.generateToken(userDetails);

            assertThat(jwtService.isTokenValid(token, userDetails)).isTrue();
        }

        @Test
        @DisplayName("Dado un token válido y otro usuario, cuando se valida, entonces retorna false")
        void givenValidToken_whenValidateWithDifferentUser_thenReturnsFalse() {
            String token = jwtService.generateToken(userDetails);
            UserDetails otherUser = new User("misty@pokemon.com", "password", List.of());

            assertThat(jwtService.isTokenValid(token, otherUser)).isFalse();
        }

        @Test
        @DisplayName("Dado un token expirado, cuando se valida, entonces lanza ExpiredJwtException")
        void givenExpiredToken_whenValidate_thenThrowsExpiredJwtException() {
            ReflectionTestUtils.setField(jwtService, "expirationMs", -1000L);
            String token = jwtService.generateToken(userDetails);

            assertThrows(io.jsonwebtoken.ExpiredJwtException.class,
                    () -> jwtService.isTokenValid(token, userDetails));
        }
    }

    @Nested
    @DisplayName("extractUsername")
    class ExtractUsername {

        @Test
        @DisplayName("Dado un token válido, cuando se extrae el username, entonces retorna el email correcto")
        void givenValidToken_whenExtractUsername_thenReturnsEmail() {
            String token = jwtService.generateToken(userDetails);

            assertThat(jwtService.extractUsername(token)).isEqualTo("ash@pokemon.com");
        }
    }
}
