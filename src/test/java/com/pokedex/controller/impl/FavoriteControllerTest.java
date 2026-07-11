package com.pokedex.controller.impl;

import com.pokedex.core.exception.BusinessException;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.service.interfaces.FavoriteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FavoriteController")
class FavoriteControllerTest {

    @Mock private FavoriteService favoriteService;

    @InjectMocks private FavoriteController controller;

    private static final String USER_EMAIL = "ash@pokemon.com";

    @BeforeEach
    void setUpSecurityContext() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(USER_EMAIL, null, List.of()));
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("GET /v1/favorites")
    class FindMyFavorites {

        @Test
        @DisplayName("Dado un usuario con favoritos, cuando se consultan, entonces retorna 200 con la lista de IDs")
        void givenUserWithFavorites_whenFindMyFavorites_thenReturns200WithIds() {
            when(favoriteService.findFavoritesByUserEmail(USER_EMAIL)).thenReturn(List.of(25L, 1L, 4L));

            ResponseEntity<List<Long>> response = controller.findMyFavorites();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).containsExactly(25L, 1L, 4L);
        }

        @Test
        @DisplayName("Dado un usuario sin favoritos, cuando se consultan, entonces retorna 200 con lista vacía")
        void givenUserWithNoFavorites_whenFindMyFavorites_thenReturns200WithEmptyList() {
            when(favoriteService.findFavoritesByUserEmail(USER_EMAIL)).thenReturn(List.of());

            ResponseEntity<List<Long>> response = controller.findMyFavorites();

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();
        }
    }

    @Nested
    @DisplayName("POST /v1/favorites/{pokemonId}")
    class AddFavorite {

        @Test
        @DisplayName("Dado un Pokémon válido, cuando se agrega a favoritos, entonces retorna 204")
        void givenValidPokemon_whenAddFavorite_thenReturns204() {
            doNothing().when(favoriteService).addFavorite(USER_EMAIL, 25L);

            ResponseEntity<Void> response = controller.addFavorite(25L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            verify(favoriteService).addFavorite(USER_EMAIL, 25L);
        }

        @Test
        @DisplayName("Dado un Pokémon ya en favoritos, cuando se agrega, entonces propaga BusinessException")
        void givenAlreadyFavoritePokemon_whenAdd_thenThrowsBusinessException() {
            doThrow(new BusinessException("Ya en favoritos", "DUPLICATE_FAVORITE"))
                    .when(favoriteService).addFavorite(USER_EMAIL, 25L);

            assertThrows(BusinessException.class, () -> controller.addFavorite(25L));
        }

        @Test
        @DisplayName("Dado un Pokémon inexistente, cuando se agrega, entonces propaga ResourceNotFoundException")
        void givenNonExistingPokemon_whenAdd_thenThrowsResourceNotFoundException() {
            doThrow(new ResourceNotFoundException("Pokémon", "id", 99L))
                    .when(favoriteService).addFavorite(USER_EMAIL, 99L);

            assertThrows(ResourceNotFoundException.class, () -> controller.addFavorite(99L));
        }
    }

    @Nested
    @DisplayName("DELETE /v1/favorites/{pokemonId}")
    class RemoveFavorite {

        @Test
        @DisplayName("Dado un Pokémon en favoritos, cuando se quita, entonces retorna 204")
        void givenFavoritePokemon_whenRemoveFavorite_thenReturns204() {
            doNothing().when(favoriteService).removeFavorite(USER_EMAIL, 25L);

            ResponseEntity<Void> response = controller.removeFavorite(25L);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            verify(favoriteService).removeFavorite(USER_EMAIL, 25L);
        }

        @Test
        @DisplayName("Dado un Pokémon no en favoritos, cuando se quita, entonces propaga ResourceNotFoundException")
        void givenNonFavoritePokemon_whenRemove_thenThrowsResourceNotFoundException() {
            doThrow(new ResourceNotFoundException("Favorito", "pokemonId", 99L))
                    .when(favoriteService).removeFavorite(USER_EMAIL, 99L);

            assertThrows(ResourceNotFoundException.class, () -> controller.removeFavorite(99L));
        }
    }
}
