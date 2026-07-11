package com.pokedex.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock private JwtService jwtService;
    @Mock private UserDetailsService userDetailsService;
    @Mock private FilterChain filterChain;
    @InjectMocks private JwtAuthFilter filter;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        userDetails = new User("ash@pokemon.com", "password",
                List.of(new SimpleGrantedAuthority("ROLE_TRAINER")));
    }

    @Nested
    @DisplayName("doFilterInternal")
    class DoFilterInternal {

        @Test
        @DisplayName("Dado que no hay header Authorization, cuando filtra, entonces continúa sin autenticar")
        void givenNoAuthHeader_whenFilter_thenContinuesWithoutAuth() throws ServletException, IOException {
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();

            filter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        }

        @Test
        @DisplayName("Dado un header sin prefijo Bearer, cuando filtra, entonces continúa sin autenticar")
        void givenNoBearerPrefix_whenFilter_thenContinuesWithoutAuth() throws ServletException, IOException {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Basic sometoken");
            MockHttpServletResponse response = new MockHttpServletResponse();

            filter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        }

        @Test
        @DisplayName("Dado un token válido, cuando filtra, entonces autentica al usuario en el contexto")
        void givenValidToken_whenFilter_thenSetsAuthentication() throws ServletException, IOException {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Bearer valid.token.here");
            MockHttpServletResponse response = new MockHttpServletResponse();

            when(jwtService.extractUsername("valid.token.here")).thenReturn("ash@pokemon.com");
            when(userDetailsService.loadUserByUsername("ash@pokemon.com")).thenReturn(userDetails);
            when(jwtService.isTokenValid("valid.token.here", userDetails)).thenReturn(true);

            filter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
            assertThat(SecurityContextHolder.getContext().getAuthentication().getName())
                    .isEqualTo("ash@pokemon.com");
        }

        @Test
        @DisplayName("Dado un token inválido, cuando filtra, entonces no autentica al usuario")
        void givenInvalidToken_whenFilter_thenDoesNotSetAuthentication() throws ServletException, IOException {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Authorization", "Bearer invalid.token");
            MockHttpServletResponse response = new MockHttpServletResponse();

            when(jwtService.extractUsername("invalid.token")).thenReturn("ash@pokemon.com");
            when(userDetailsService.loadUserByUsername("ash@pokemon.com")).thenReturn(userDetails);
            when(jwtService.isTokenValid("invalid.token", userDetails)).thenReturn(false);

            filter.doFilterInternal(request, response, filterChain);

            verify(filterChain).doFilter(request, response);
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        }
    }
}
