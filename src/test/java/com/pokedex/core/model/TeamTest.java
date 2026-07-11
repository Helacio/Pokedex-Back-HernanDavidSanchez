package com.pokedex.core.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Team")
class TeamTest {

    @Nested
    @DisplayName("builder")
    class Builder {

        @Test
        @DisplayName("Dado todos los campos, cuando se construye, entonces los getters retornan los valores correctos")
        void givenAllFields_whenBuilt_thenGettersReturnCorrectValues() {
            Team team = Team.builder()
                    .id(1L)
                    .name("Team Rocket")
                    .userId(42L)
                    .pokemonIds(List.of(52L, 88L, 109L))
                    .build();

            assertThat(team.getId()).isEqualTo(1L);
            assertThat(team.getName()).isEqualTo("Team Rocket");
            assertThat(team.getUserId()).isEqualTo(42L);
            assertThat(team.getPokemonIds()).containsExactly(52L, 88L, 109L);
        }

        @Test
        @DisplayName("Dado un equipo con 6 Pokémon, cuando se construye, entonces la lista contiene exactamente 6 elementos")
        void givenSixPokemon_whenBuilt_thenListHasSixElements() {
            Team team = Team.builder()
                    .id(2L)
                    .name("Dream Team")
                    .userId(1L)
                    .pokemonIds(List.of(1L, 4L, 7L, 25L, 39L, 54L))
                    .build();

            assertThat(team.getPokemonIds()).hasSize(6);
        }

        @Test
        @DisplayName("Dado dos instancias con los mismos valores, cuando se comparan, entonces son iguales")
        void givenTwoInstancesWithSameValues_whenCompared_thenAreEqual() {
            Team team1 = Team.builder().id(1L).name("Ash Team").userId(1L)
                    .pokemonIds(List.of(25L)).build();
            Team team2 = Team.builder().id(1L).name("Ash Team").userId(1L)
                    .pokemonIds(List.of(25L)).build();

            assertThat(team1).isEqualTo(team2);
            assertThat(team1.hashCode()).isEqualTo(team2.hashCode());
        }

        @Test
        @DisplayName("Dado pokemonIds nulo, cuando se construye, entonces el campo es nulo")
        void givenNullPokemonIds_whenBuilt_thenPokemonIdsIsNull() {
            Team team = Team.builder().id(1L).name("Empty").userId(1L).build();

            assertThat(team.getPokemonIds()).isNull();
        }
    }
}
