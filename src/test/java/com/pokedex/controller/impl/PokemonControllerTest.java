package com.pokedex.controller.impl;

import com.pokedex.controller.dto.request.PokemonRequest;
import com.pokedex.controller.dto.response.PokemonResponse;
import com.pokedex.controller.mapper.PokemonDtoMapper;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.service.interfaces.PokemonService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonControllerTest {

    @Mock private PokemonService pokemonService;
    @Mock private PokemonDtoMapper mapper;
    @InjectMocks private PokemonController controller;

    private Pokemon pikachu;
    private PokemonResponse pikachuResponse;
    private PokemonRequest pikachuRequest;

    @BeforeEach
    void setUp() {
        pikachu = Pokemon.builder()
                .id(1L).nationalNumber(25).name("Pikachu")
                .types(List.of("Electric")).region("Kanto").generation(1).hasMega(false)
                .build();
        pikachuResponse = new PokemonResponse(1L, 25, "Pikachu", null, List.of("Electric"), "Kanto", 1, false, null);
        pikachuRequest = new PokemonRequest(25, "Pikachu", "url", List.of("Electric"), 1L, 1);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/pokemon");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Nested
    @DisplayName("GET /v1/pokemon")
    class FindAll {

        @Test
        @DisplayName("Dado que existen Pokémon, cuando se listan, entonces retorna 200 con la página")
        void givenPokemonExist_whenFindAll_thenReturns200WithPage() {
            Page<Pokemon> page = new PageImpl<>(List.of(pikachu));
            when(pokemonService.findAll(any())).thenReturn(page);
            when(mapper.toResponse(pikachu)).thenReturn(pikachuResponse);

            ResponseEntity<Page<PokemonResponse>> response = controller.findAll(PageRequest.of(0, 20));

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getContent()).hasSize(1);
            assertThat(response.getBody().getContent().get(0).name()).isEqualTo("Pikachu");
        }

        @Test
        @DisplayName("Dado que no existen Pokémon, cuando se listan, entonces retorna página vacía")
        void givenNoPokemon_whenFindAll_thenReturnsEmptyPage() {
            when(pokemonService.findAll(any())).thenReturn(Page.empty());

            ResponseEntity<Page<PokemonResponse>> response = controller.findAll(PageRequest.of(0, 20));

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getContent()).isEmpty();
        }
    }

    @Nested
    @DisplayName("GET /v1/pokemon/{id}")
    class FindById {

        @Test
        @DisplayName("Dado un ID existente, cuando se consulta, entonces retorna 200 con el Pokémon")
        void givenExistingId_whenFindById_thenReturns200() {
            when(pokemonService.findById(1L)).thenReturn(pikachu);
            when(mapper.toResponse(pikachu)).thenReturn(pikachuResponse);

            ResponseEntity<PokemonResponse> response = controller.findById(1L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().name()).isEqualTo("Pikachu");
        }

        @Test
        @DisplayName("Dado un ID inexistente, cuando se consulta, entonces lanza ResourceNotFoundException")
        void givenNonExistingId_whenFindById_thenThrowsNotFound() {
            when(pokemonService.findById(99L)).thenThrow(new ResourceNotFoundException("Pokemon", "id", 99L));

            assertThrows(ResourceNotFoundException.class, () -> controller.findById(99L));
        }
    }

    @Nested
    @DisplayName("POST /v1/pokemon")
    class Create {

        @Test
        @DisplayName("Dado un request válido, cuando se crea, entonces retorna 201 con location")
        void givenValidRequest_whenCreate_thenReturns201WithLocation() {
            when(mapper.toDomain(pikachuRequest)).thenReturn(pikachu);
            when(pokemonService.create(pikachu)).thenReturn(pikachu);
            when(mapper.toResponse(pikachu)).thenReturn(pikachuResponse);

            ResponseEntity<PokemonResponse> response = controller.create(pikachuRequest);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getHeaders().getLocation()).isNotNull();
            assertThat(response.getBody().name()).isEqualTo("Pikachu");
        }
    }

    @Nested
    @DisplayName("PUT /v1/pokemon/{id}")
    class Update {

        @Test
        @DisplayName("Dado un ID existente y request válido, cuando se actualiza, entonces retorna 200")
        void givenExistingId_whenUpdate_thenReturns200() {
            when(mapper.toDomain(pikachuRequest)).thenReturn(pikachu);
            when(pokemonService.update(eq(1L), any())).thenReturn(pikachu);
            when(mapper.toResponse(pikachu)).thenReturn(pikachuResponse);

            ResponseEntity<PokemonResponse> response = controller.update(1L, pikachuRequest);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().name()).isEqualTo("Pikachu");
        }

        @Test
        @DisplayName("Dado un ID inexistente, cuando se actualiza, entonces lanza ResourceNotFoundException")
        void givenNonExistingId_whenUpdate_thenThrowsNotFound() {
            when(mapper.toDomain(pikachuRequest)).thenReturn(pikachu);
            when(pokemonService.update(eq(99L), any()))
                    .thenThrow(new ResourceNotFoundException("Pokemon", "id", 99L));

            assertThrows(ResourceNotFoundException.class, () -> controller.update(99L, pikachuRequest));
        }
    }

    @Nested
    @DisplayName("DELETE /v1/pokemon/{id}")
    class Delete {

        @Test
        @DisplayName("Dado un ID existente, cuando se elimina, entonces retorna 204")
        void givenExistingId_whenDelete_thenReturns204() {
            doNothing().when(pokemonService).delete(1L);

            ResponseEntity<Void> response = controller.delete(1L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            verify(pokemonService).delete(1L);
        }

        @Test
        @DisplayName("Dado un ID inexistente, cuando se elimina, entonces lanza ResourceNotFoundException")
        void givenNonExistingId_whenDelete_thenThrowsNotFound() {
            doThrow(new ResourceNotFoundException("Pokemon", "id", 99L)).when(pokemonService).delete(99L);

            assertThrows(ResourceNotFoundException.class, () -> controller.delete(99L));
        }
    }
}
