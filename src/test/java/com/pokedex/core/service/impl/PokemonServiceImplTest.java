package com.pokedex.core.service.impl;

import com.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.model.PokemonStats;
import com.pokedex.core.port.PokemonPersistencePort;
import com.pokedex.core.service.interfaces.PokemonFilterCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    // ─── findAll ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findAll: debe retornar la página de Pokémon correctamente")
    void findAll_returnsPage() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Pokemon> page = new PageImpl<>(List.of(pikachu));
        when(pokemonPort.findAll(pageable)).thenReturn(page);

        Page<Pokemon> result = service.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Pikachu");
        verify(pokemonPort).findAll(pageable);
    }

    // ─── findById ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("findById: debe retornar el Pokémon cuando existe")
    void findById_whenExists_returnsPokemon() {
        when(pokemonPort.findById(1L)).thenReturn(Optional.of(pikachu));

        Pokemon result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Pikachu");
        verify(pokemonPort).findById(1L);
    }

    @Test
    @DisplayName("findById: debe lanzar ResourceNotFoundException cuando no existe")
    void findById_whenNotFound_throwsResourceNotFoundException() {
        when(pokemonPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
        verify(pokemonPort).findById(99L);
    }

    // ─── findByNationalNumber ────────────────────────────────────────────────

    @Test
    @DisplayName("findByNationalNumber: debe retornar el Pokémon cuando existe")
    void findByNationalNumber_whenExists_returnsPokemon() {
        when(pokemonPort.findByNationalNumber(25)).thenReturn(Optional.of(pikachu));

        Pokemon result = service.findByNationalNumber(25);

        assertThat(result).isNotNull();
        assertThat(result.getNationalNumber()).isEqualTo(25);
        verify(pokemonPort).findByNationalNumber(25);
    }

    @Test
    @DisplayName("findByNationalNumber: debe lanzar ResourceNotFoundException cuando no existe")
    void findByNationalNumber_whenNotFound_throwsResourceNotFoundException() {
        when(pokemonPort.findByNationalNumber(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.findByNationalNumber(999));
    }

    // ─── create ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("create: debe guardar y retornar el Pokémon cuando el número no existe")
    void create_whenValid_saveAndReturn() {
        when(pokemonPort.existsByNationalNumber(25)).thenReturn(false);
        when(pokemonPort.save(pikachu)).thenReturn(pikachu);

        Pokemon result = service.create(pikachu);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Pikachu");
        verify(pokemonPort).save(pikachu);
    }

    @Test
    @DisplayName("create: debe lanzar DuplicateResourceException si el número nacional ya existe")
    void create_whenDuplicate_throwsDuplicateResourceException() {
        when(pokemonPort.existsByNationalNumber(25)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.create(pikachu));
        verify(pokemonPort, never()).save(any());
    }

    // ─── update ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("update: debe actualizar y retornar el Pokémon cuando existe")
    void update_whenExists_updatesAndReturns() {
        Pokemon updated = pikachu.toBuilder().name("Raichu").build();
        when(pokemonPort.findById(1L)).thenReturn(Optional.of(pikachu));
        when(pokemonPort.save(any(Pokemon.class))).thenReturn(updated);

        Pokemon result = service.update(1L, pikachu);

        assertThat(result).isNotNull();
        verify(pokemonPort).findById(1L);
        verify(pokemonPort).save(any(Pokemon.class));
    }

    @Test
    @DisplayName("update: debe lanzar ResourceNotFoundException si el Pokémon no existe")
    void update_whenNotFound_throwsResourceNotFoundException() {
        when(pokemonPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.update(99L, pikachu));
        verify(pokemonPort, never()).save(any());
    }

    // ─── delete ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete: debe eliminar el Pokémon cuando existe")
    void delete_whenExists_callsDeleteById() {
        when(pokemonPort.findById(1L)).thenReturn(Optional.of(pikachu));

        service.delete(1L);

        verify(pokemonPort).deleteById(1L);
    }

    @Test
    @DisplayName("delete: debe lanzar ResourceNotFoundException si el Pokémon no existe")
    void delete_whenNotFound_throwsResourceNotFoundException() {
        when(pokemonPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
        verify(pokemonPort, never()).deleteById(any());
    }

    // ─── filterByCriteria ───────────────────────────────────────────────────

    @Test
    @DisplayName("filterByCriteria: debe retornar la lista filtrada del port")
    void filterByCriteria_returnsList() {
        PokemonFilterCriteria criteria = new PokemonFilterCriteria(
                List.of("Electric"), "Kanto", 1, false, null, null);
        when(pokemonPort.findByCriteria(criteria)).thenReturn(List.of(pikachu));

        List<Pokemon> result = service.filterByCriteria(criteria);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Pikachu");
        verify(pokemonPort).findByCriteria(criteria);
    }
}
