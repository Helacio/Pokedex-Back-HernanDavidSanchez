package com.pokedex.core.service.impl;

import com.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonStats;
import com.pokedex.core.port.PokemonPersistencePort;
import com.pokedex.core.service.interfaces.PokemonFilterCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonServiceImplTest {

    @Mock
    private PokemonPersistencePort pokemonPort;

    @InjectMocks
    private PokemonServiceImpl service;

    private Pokemon pikachu;

    @BeforeEach
    void setUp() {
        pikachu = Pokemon.builder()
                .id(1L)
                .nationalNumber(25)
                .name("Pikachu")
                .types(List.of("Electric"))
                .region("Kanto")
                .generation(1)
                .hasMega(false)
                .stats(PokemonStats.builder()
                        .hp(35).attack(55).defense(40)
                        .specialAttack(50).specialDefense(50).speed(90)
                        .build())
                .build();
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("Dado que existen Pokémon, cuando se listan, entonces retorna la página correctamente")
        void givenPokemonExist_whenFindAll_thenReturnsPage() {
            Pageable pageable = PageRequest.of(0, 20);
            Page<Pokemon> page = new PageImpl<>(List.of(pikachu));
            when(pokemonPort.findAll(pageable)).thenReturn(page);

            Page<Pokemon> result = service.findAll(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getName()).isEqualTo("Pikachu");
            verify(pokemonPort).findAll(pageable);
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("Dado un ID existente, cuando se busca, entonces retorna el Pokémon")
        void givenExistingId_whenFindById_thenReturnsPokemon() {
            when(pokemonPort.findById(1L)).thenReturn(Optional.of(pikachu));

            Pokemon result = service.findById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Pikachu");
            verify(pokemonPort).findById(1L);
        }

        @Test
        @DisplayName("Dado un ID inexistente, cuando se busca, entonces lanza ResourceNotFoundException")
        void givenNonExistingId_whenFindById_thenThrowsResourceNotFoundException() {
            when(pokemonPort.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
            verify(pokemonPort).findById(99L);
        }
    }

    @Nested
    @DisplayName("findByNationalNumber")
    class FindByNationalNumber {

        @Test
        @DisplayName("Dado un número nacional existente, cuando se busca, entonces retorna el Pokémon")
        void givenExistingNumber_whenFindByNationalNumber_thenReturnsPokemon() {
            when(pokemonPort.findByNationalNumber(25)).thenReturn(Optional.of(pikachu));

            Pokemon result = service.findByNationalNumber(25);

            assertThat(result).isNotNull();
            assertThat(result.getNationalNumber()).isEqualTo(25);
            verify(pokemonPort).findByNationalNumber(25);
        }

        @Test
        @DisplayName("Dado un número nacional inexistente, cuando se busca, entonces lanza ResourceNotFoundException")
        void givenNonExistingNumber_whenFindByNationalNumber_thenThrowsResourceNotFoundException() {
            when(pokemonPort.findByNationalNumber(999)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> service.findByNationalNumber(999));
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("Dado un Pokémon con número único, cuando se crea, entonces lo guarda y retorna")
        void givenUniqueNationalNumber_whenCreate_thenSavesAndReturns() {
            when(pokemonPort.existsByNationalNumber(25)).thenReturn(false);
            when(pokemonPort.save(pikachu)).thenReturn(pikachu);

            Pokemon result = service.create(pikachu);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Pikachu");
            verify(pokemonPort).save(pikachu);
        }

        @Test
        @DisplayName("Dado un número nacional duplicado, cuando se crea, entonces lanza DuplicateResourceException")
        void givenDuplicateNationalNumber_whenCreate_thenThrowsDuplicateResourceException() {
            when(pokemonPort.existsByNationalNumber(25)).thenReturn(true);

            assertThrows(DuplicateResourceException.class, () -> service.create(pikachu));
            verify(pokemonPort, never()).save(any());
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("Dado un ID existente y datos válidos, cuando se actualiza, entonces retorna el Pokémon actualizado")
        void givenExistingId_whenUpdate_thenUpdatesAndReturns() {
            Pokemon updated = pikachu.toBuilder().name("Raichu").build();
            when(pokemonPort.findById(1L)).thenReturn(Optional.of(pikachu));
            when(pokemonPort.save(any(Pokemon.class))).thenReturn(updated);

            Pokemon result = service.update(1L, pikachu);

            assertThat(result).isNotNull();
            verify(pokemonPort).findById(1L);
            verify(pokemonPort).save(any(Pokemon.class));
        }

        @Test
        @DisplayName("Dado un ID inexistente, cuando se actualiza, entonces lanza ResourceNotFoundException")
        void givenNonExistingId_whenUpdate_thenThrowsResourceNotFoundException() {
            when(pokemonPort.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> service.update(99L, pikachu));
            verify(pokemonPort, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("Dado un ID existente, cuando se elimina, entonces llama deleteById")
        void givenExistingId_whenDelete_thenCallsDeleteById() {
            when(pokemonPort.findById(1L)).thenReturn(Optional.of(pikachu));

            service.delete(1L);

            verify(pokemonPort).deleteById(1L);
        }

        @Test
        @DisplayName("Dado un ID inexistente, cuando se elimina, entonces lanza ResourceNotFoundException")
        void givenNonExistingId_whenDelete_thenThrowsResourceNotFoundException() {
            when(pokemonPort.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
            verify(pokemonPort, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("filterByCriteria")
    class FilterByCriteria {

        @Test
        @DisplayName("Dado criterios válidos, cuando se filtra, entonces retorna la lista filtrada")
        void givenValidCriteria_whenFilterByCriteria_thenReturnsFilteredList() {
            PokemonFilterCriteria criteria = new PokemonFilterCriteria(
                    List.of("Electric"), "Kanto", 1, false, null, null);
            when(pokemonPort.findByCriteria(criteria)).thenReturn(List.of(pikachu));

            List<Pokemon> result = service.filterByCriteria(criteria);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Pikachu");
            verify(pokemonPort).findByCriteria(criteria);
        }
    }
}
