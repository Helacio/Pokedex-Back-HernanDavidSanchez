package com.pokedex.core.validator;

import com.pokedex.core.exception.BusinessException;
import com.pokedex.core.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("TeamValidator")
class TeamValidatorTest {

    private TeamValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TeamValidator();
    }

    @Nested
    @DisplayName("validate — casos válidos")
    class ValidCases {

        @Test
        @DisplayName("Dado un equipo con nombre y 1 Pokémon, cuando se valida, entonces no lanza excepción")
        void givenTeamWithOnePoemon_whenValidate_thenNoException() {
            Team team = Team.builder()
                    .name("Solo Team").userId(1L).pokemonIds(List.of(25L)).build();

            assertDoesNotThrow(() -> validator.validate(team));
        }

        @Test
        @DisplayName("Dado un equipo con 6 Pokémon distintos, cuando se valida, entonces no lanza excepción")
        void givenTeamWithSixDistinctPokemon_whenValidate_thenNoException() {
            Team team = Team.builder()
                    .name("Full Team").userId(1L)
                    .pokemonIds(List.of(1L, 4L, 7L, 25L, 39L, 54L))
                    .build();

            assertDoesNotThrow(() -> validator.validate(team));
        }
    }

    @Nested
    @DisplayName("validate — nombre inválido")
    class InvalidName {

        @Test
        @DisplayName("Dado nombre nulo, cuando se valida, entonces lanza BusinessException con código INVALID_TEAM_NAME")
        void givenNullName_whenValidate_thenThrowsBusinessExceptionWithCode() {
            Team team = Team.builder()
                    .name(null).userId(1L).pokemonIds(List.of(25L)).build();

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> validator.validate(team));

            assertThat(ex.getErrorCode()).isEqualTo("INVALID_TEAM_NAME");
        }

        @Test
        @DisplayName("Dado nombre vacío, cuando se valida, entonces lanza BusinessException con código INVALID_TEAM_NAME")
        void givenBlankName_whenValidate_thenThrowsBusinessExceptionWithCode() {
            Team team = Team.builder()
                    .name("   ").userId(1L).pokemonIds(List.of(25L)).build();

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> validator.validate(team));

            assertThat(ex.getErrorCode()).isEqualTo("INVALID_TEAM_NAME");
        }
    }

    @Nested
    @DisplayName("validate — tamaño del equipo")
    class TeamSize {

        @Test
        @DisplayName("Dado lista de Pokémon vacía, cuando se valida, entonces lanza BusinessException con código TEAM_TOO_SMALL")
        void givenEmptyPokemonList_whenValidate_thenThrowsTeamTooSmall() {
            Team team = Team.builder()
                    .name("Empty Team").userId(1L).pokemonIds(List.of()).build();

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> validator.validate(team));

            assertThat(ex.getErrorCode()).isEqualTo("TEAM_TOO_SMALL");
        }

        @Test
        @DisplayName("Dado lista de Pokémon nula, cuando se valida, entonces lanza BusinessException con código TEAM_TOO_SMALL")
        void givenNullPokemonList_whenValidate_thenThrowsTeamTooSmall() {
            Team team = Team.builder()
                    .name("Null Team").userId(1L).pokemonIds(null).build();

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> validator.validate(team));

            assertThat(ex.getErrorCode()).isEqualTo("TEAM_TOO_SMALL");
        }

        @Test
        @DisplayName("Dado 7 Pokémon, cuando se valida, entonces lanza BusinessException con código TEAM_TOO_LARGE")
        void givenSevenPokemon_whenValidate_thenThrowsTeamTooLarge() {
            Team team = Team.builder()
                    .name("Too Big").userId(1L)
                    .pokemonIds(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L))
                    .build();

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> validator.validate(team));

            assertThat(ex.getErrorCode()).isEqualTo("TEAM_TOO_LARGE");
            assertThat(ex.getMessage()).contains("6");
        }
    }

    @Nested
    @DisplayName("validate — Pokémon duplicados")
    class DuplicatePokemon {

        @Test
        @DisplayName("Dado Pokémon duplicado en la lista, cuando se valida, entonces lanza BusinessException con código DUPLICATE_POKEMON_IN_TEAM")
        void givenDuplicatePokemonId_whenValidate_thenThrowsDuplicatePokemon() {
            Team team = Team.builder()
                    .name("Duplicate Team").userId(1L)
                    .pokemonIds(List.of(25L, 1L, 25L))
                    .build();

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> validator.validate(team));

            assertThat(ex.getErrorCode()).isEqualTo("DUPLICATE_POKEMON_IN_TEAM");
        }

        @Test
        @DisplayName("Dado todos los Pokémon iguales, cuando se valida, entonces lanza BusinessException con código DUPLICATE_POKEMON_IN_TEAM")
        void givenAllSamePokemon_whenValidate_thenThrowsDuplicatePokemon() {
            Team team = Team.builder()
                    .name("Mono Team").userId(1L)
                    .pokemonIds(List.of(25L, 25L, 25L))
                    .build();

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> validator.validate(team));

            assertThat(ex.getErrorCode()).isEqualTo("DUPLICATE_POKEMON_IN_TEAM");
        }
    }
}
