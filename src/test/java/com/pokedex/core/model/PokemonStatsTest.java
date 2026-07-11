package com.pokedex.core.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PokemonStats")
class PokemonStatsTest {

    @Nested
    @DisplayName("getTotal")
    class GetTotal {

        @Test
        @DisplayName("Dado estadísticas base de Pikachu, cuando se calcula el total, entonces retorna la suma correcta")
        void givenPikachuStats_whenGetTotal_thenReturnsSumOfAllStats() {
            PokemonStats stats = PokemonStats.builder()
                    .hp(35).attack(55).defense(40)
                    .specialAttack(50).specialDefense(50).speed(90)
                    .build();

            assertThat(stats.getTotal()).isEqualTo(320);
        }

        @Test
        @DisplayName("Dado estadísticas de Snorlax, cuando se calcula el total, entonces retorna la suma correcta")
        void givenSnorlaxStats_whenGetTotal_thenReturnsSumOfAllStats() {
            PokemonStats stats = PokemonStats.builder()
                    .hp(160).attack(110).defense(65)
                    .specialAttack(65).specialDefense(110).speed(30)
                    .build();

            assertThat(stats.getTotal()).isEqualTo(540);
        }

        @Test
        @DisplayName("Dado todas las estadísticas en cero, cuando se calcula el total, entonces retorna cero")
        void givenAllStatsZero_whenGetTotal_thenReturnsZero() {
            PokemonStats stats = PokemonStats.builder()
                    .hp(0).attack(0).defense(0)
                    .specialAttack(0).specialDefense(0).speed(0)
                    .build();

            assertThat(stats.getTotal()).isEqualTo(0);
        }

        @Test
        @DisplayName("Dado todas las estadísticas en uno, cuando se calcula el total, entonces retorna 6")
        void givenAllStatsOne_whenGetTotal_thenReturnsSix() {
            PokemonStats stats = PokemonStats.builder()
                    .hp(1).attack(1).defense(1)
                    .specialAttack(1).specialDefense(1).speed(1)
                    .build();

            assertThat(stats.getTotal()).isEqualTo(6);
        }

        @Test
        @DisplayName("Dado estadísticas máximas (255 cada una), cuando se calcula el total, entonces retorna 1530")
        void givenMaxStats_whenGetTotal_thenReturnsMaxTotal() {
            PokemonStats stats = PokemonStats.builder()
                    .hp(255).attack(255).defense(255)
                    .specialAttack(255).specialDefense(255).speed(255)
                    .build();

            assertThat(stats.getTotal()).isEqualTo(1530);
        }

        @Test
        @DisplayName("Dado que hp y speed son distintos, cuando se calcula el total, entonces todos los campos contribuyen por separado")
        void givenDistinctStats_whenGetTotal_thenEachFieldContributesIndependently() {
            PokemonStats stats = PokemonStats.builder()
                    .hp(10).attack(20).defense(30)
                    .specialAttack(40).specialDefense(50).speed(60)
                    .build();

            assertThat(stats.getTotal())
                    .isEqualTo(stats.getHp() + stats.getAttack() + stats.getDefense()
                            + stats.getSpecialAttack() + stats.getSpecialDefense() + stats.getSpeed());
        }
    }

    @Nested
    @DisplayName("builder")
    class Builder {

        @Test
        @DisplayName("Dado todos los campos definidos, cuando se construye, entonces cada getter retorna el valor correcto")
        void givenAllFields_whenBuilt_thenGettersReturnCorrectValues() {
            PokemonStats stats = PokemonStats.builder()
                    .hp(45).attack(49).defense(49)
                    .specialAttack(65).specialDefense(65).speed(45)
                    .build();

            assertThat(stats.getHp()).isEqualTo(45);
            assertThat(stats.getAttack()).isEqualTo(49);
            assertThat(stats.getDefense()).isEqualTo(49);
            assertThat(stats.getSpecialAttack()).isEqualTo(65);
            assertThat(stats.getSpecialDefense()).isEqualTo(65);
            assertThat(stats.getSpeed()).isEqualTo(45);
        }

        @Test
        @DisplayName("Dado dos instancias con los mismos valores, cuando se comparan, entonces son iguales")
        void givenTwoInstancesWithSameValues_whenCompared_thenAreEqual() {
            PokemonStats stats1 = PokemonStats.builder()
                    .hp(35).attack(55).defense(40)
                    .specialAttack(50).specialDefense(50).speed(90)
                    .build();
            PokemonStats stats2 = PokemonStats.builder()
                    .hp(35).attack(55).defense(40)
                    .specialAttack(50).specialDefense(50).speed(90)
                    .build();

            assertThat(stats1).isEqualTo(stats2);
            assertThat(stats1.hashCode()).isEqualTo(stats2.hashCode());
        }
    }
}
