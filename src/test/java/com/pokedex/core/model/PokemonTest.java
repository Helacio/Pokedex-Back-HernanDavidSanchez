package com.pokedex.core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Pokemon")
class PokemonTest {

    private PokemonStats pikachuStats;

    @BeforeEach
    void setUp() {
        pikachuStats = PokemonStats.builder()
                .hp(35).attack(55).defense(40)
                .specialAttack(50).specialDefense(50).speed(90)
                .build();
    }

    @Nested
    @DisplayName("builder")
    class Builder {

        @Test
        @DisplayName("Dado todos los campos, cuando se construye, entonces cada getter retorna el valor correcto")
        void givenAllFields_whenBuilt_thenGettersReturnCorrectValues() {
            Pokemon pokemon = Pokemon.builder()
                    .id(1L)
                    .nationalNumber(25)
                    .name("Pikachu")
                    .description("El ratón eléctrico")
                    .imageUrl("https://example.com/pikachu.png")
                    .types(List.of("Electric"))
                    .region("Kanto")
                    .generation(1)
                    .hasMega(false)
                    .stats(pikachuStats)
                    .build();

            assertThat(pokemon.getId()).isEqualTo(1L);
            assertThat(pokemon.getNationalNumber()).isEqualTo(25);
            assertThat(pokemon.getName()).isEqualTo("Pikachu");
            assertThat(pokemon.getDescription()).isEqualTo("El ratón eléctrico");
            assertThat(pokemon.getImageUrl()).isEqualTo("https://example.com/pikachu.png");
            assertThat(pokemon.getTypes()).containsExactly("Electric");
            assertThat(pokemon.getRegion()).isEqualTo("Kanto");
            assertThat(pokemon.getGeneration()).isEqualTo(1);
            assertThat(pokemon.getHasMega()).isFalse();
            assertThat(pokemon.getStats()).isEqualTo(pikachuStats);
        }

        @Test
        @DisplayName("Dado un Pokémon con dos tipos, cuando se construye, entonces la lista de tipos se preserva completa")
        void givenDualType_whenBuilt_thenTypesListIsPreserved() {
            Pokemon pokemon = Pokemon.builder()
                    .id(6L).nationalNumber(6).name("Charizard")
                    .types(List.of("Fire", "Flying"))
                    .region("Kanto").generation(1).hasMega(true)
                    .build();

            assertThat(pokemon.getTypes()).hasSize(2).containsExactly("Fire", "Flying");
            assertThat(pokemon.getHasMega()).isTrue();
        }

        @Test
        @DisplayName("Dado campos opcionales nulos, cuando se construye, entonces stats e imageUrl son nulos")
        void givenNullOptionalFields_whenBuilt_thenNullableFieldsAreNull() {
            Pokemon pokemon = Pokemon.builder()
                    .id(1L).nationalNumber(25).name("Pikachu")
                    .types(List.of("Electric")).region("Kanto").generation(1).hasMega(false)
                    .build();

            assertThat(pokemon.getStats()).isNull();
            assertThat(pokemon.getDescription()).isNull();
            assertThat(pokemon.getImageUrl()).isNull();
        }

        @Test
        @DisplayName("Dado dos instancias con los mismos valores, cuando se comparan, entonces son iguales")
        void givenTwoInstancesWithSameValues_whenCompared_thenAreEqual() {
            Pokemon p1 = Pokemon.builder()
                    .id(1L).nationalNumber(25).name("Pikachu")
                    .types(List.of("Electric")).region("Kanto").generation(1).hasMega(false)
                    .stats(pikachuStats).build();
            Pokemon p2 = Pokemon.builder()
                    .id(1L).nationalNumber(25).name("Pikachu")
                    .types(List.of("Electric")).region("Kanto").generation(1).hasMega(false)
                    .stats(pikachuStats).build();

            assertThat(p1).isEqualTo(p2);
            assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
        }
    }

    @Nested
    @DisplayName("toBuilder (inmutabilidad)")
    class ToBuilder {

        @Test
        @DisplayName("Dado un Pokémon existente, cuando se usa toBuilder para cambiar el nombre, entonces el nuevo objeto tiene el nombre actualizado")
        void givenExistingPokemon_whenToBuilderChangesName_thenNewInstanceHasUpdatedName() {
            Pokemon pikachu = Pokemon.builder()
                    .id(1L).nationalNumber(25).name("Pikachu")
                    .types(List.of("Electric")).region("Kanto").generation(1).hasMega(false)
                    .stats(pikachuStats).build();

            Pokemon raichu = pikachu.toBuilder().name("Raichu").nationalNumber(26).build();

            assertThat(raichu.getName()).isEqualTo("Raichu");
            assertThat(raichu.getNationalNumber()).isEqualTo(26);
        }

        @Test
        @DisplayName("Dado un Pokémon existente, cuando se usa toBuilder, entonces el original no se modifica")
        void givenExistingPokemon_whenToBuilderIsUsed_thenOriginalRemainsUnchanged() {
            Pokemon pikachu = Pokemon.builder()
                    .id(1L).nationalNumber(25).name("Pikachu")
                    .types(List.of("Electric")).region("Kanto").generation(1).hasMega(false)
                    .stats(pikachuStats).build();

            pikachu.toBuilder().name("Raichu").nationalNumber(26).build();

            assertThat(pikachu.getName()).isEqualTo("Pikachu");
            assertThat(pikachu.getNationalNumber()).isEqualTo(25);
        }

        @Test
        @DisplayName("Dado un Pokémon existente, cuando se usa toBuilder sin cambios, entonces la copia es igual al original")
        void givenExistingPokemon_whenToBuilderWithNoChanges_thenCopyEqualsOriginal() {
            Pokemon pikachu = Pokemon.builder()
                    .id(1L).nationalNumber(25).name("Pikachu")
                    .types(List.of("Electric")).region("Kanto").generation(1).hasMega(false)
                    .stats(pikachuStats).build();

            Pokemon copy = pikachu.toBuilder().build();

            assertThat(copy).isEqualTo(pikachu);
        }

        @Test
        @DisplayName("Dado un Pokémon sin mega, cuando se usa toBuilder para activar hasMega, entonces el nuevo objeto lo refleja")
        void givenPokemonWithoutMega_whenToBuilderEnablesMega_thenNewInstanceHasMegaTrue() {
            Pokemon charizard = Pokemon.builder()
                    .id(6L).nationalNumber(6).name("Charizard")
                    .types(List.of("Fire", "Flying")).region("Kanto").generation(1).hasMega(false)
                    .build();

            Pokemon megaCharizard = charizard.toBuilder().hasMega(true).build();

            assertThat(megaCharizard.getHasMega()).isTrue();
            assertThat(charizard.getHasMega()).isFalse();
        }
    }
}
