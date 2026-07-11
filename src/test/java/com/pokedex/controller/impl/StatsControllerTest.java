package com.pokedex.controller.impl;

import com.pokedex.core.model.AdminMetrics;
import com.pokedex.core.model.PokemonView;
import com.pokedex.core.service.interfaces.StatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = StatsControllerTest.TestConfig.class)
@DisplayName("StatsController")
class StatsControllerTest {

    @Autowired private WebApplicationContext context;
    @Autowired private StatsService statsService;

    private MockMvc mockMvc;

    private final PokemonView pikachuView = PokemonView.builder()
            .pokemonId(25L)
            .pokemonName("Pikachu")
            .viewCount(100L)
            .lastViewed(LocalDateTime.of(2026, 1, 15, 10, 0))
            .build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Configuration
    @EnableWebMvc
    @EnableWebSecurity
    @EnableMethodSecurity(proxyTargetClass = true)
    @Import(StatsController.class)
    static class TestConfig {

        @Bean
        StatsService statsService() {
            return mock(StatsService.class);
        }

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                    .build();
        }
    }

    @Nested
    @DisplayName("GET /v1/stats/popular")
    class MostViewed {

        @Test
        @WithMockUser(username = "ash@pokemon.com", roles = "TRAINER")
        @DisplayName("Dado un usuario autenticado, cuando consulta popular, entonces retorna 200")
        void givenAuthenticatedUser_whenMostViewed_thenReturns200() throws Exception {
            when(statsService.mostViewed(10)).thenReturn(List.of(pikachuView));

            mockMvc.perform(get("/v1/stats/popular"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].pokemonName").value("Pikachu"))
                    .andExpect(jsonPath("$[0].viewCount").value(100));
        }

        @Test
        @DisplayName("Dado un usuario no autenticado, cuando consulta popular, entonces retorna 403")
        void givenAnonymousUser_whenMostViewed_thenReturns403() throws Exception {
            mockMvc.perform(get("/v1/stats/popular"))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("GET /v1/stats/admin")
    class AdminMetricsEndpoint {

        @Test
        @WithMockUser(username = "admin@pokedex.com", roles = "ADMIN")
        @DisplayName("Dado un usuario ADMIN, cuando consulta admin, entonces retorna 200")
        void givenAdmin_whenAdminMetrics_thenReturns200() throws Exception {
            AdminMetrics metrics = AdminMetrics.builder()
                    .totalConsultas(500L)
                    .pokemonDistintosConsultados(10L)
                    .topPokemon(List.of(pikachuView))
                    .build();
            when(statsService.getAdminMetrics()).thenReturn(metrics);

            mockMvc.perform(get("/v1/stats/admin"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalConsultas").value(500))
                    .andExpect(jsonPath("$.topPokemon[0].pokemonName").value("Pikachu"));
        }

        @Test
        @WithMockUser(username = "ash@pokemon.com", roles = "TRAINER")
        @DisplayName("Dado un usuario TRAINER, cuando consulta admin, entonces retorna 403")
        void givenTrainer_whenAdminMetrics_thenReturns403() throws Exception {
            mockMvc.perform(get("/v1/stats/admin"))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Dado un usuario no autenticado, cuando consulta admin, entonces retorna 403")
        void givenAnonymousUser_whenAdminMetrics_thenReturns403() throws Exception {
            mockMvc.perform(get("/v1/stats/admin"))
                    .andExpect(status().isForbidden());
        }
    }
}
