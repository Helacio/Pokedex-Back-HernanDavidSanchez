package com.pokedex.core.service.impl;

import com.pokedex.core.exception.BusinessException;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.persistence.entity.relational.FavoriteEntity;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.repository.relational.FavoriteJpaRepository;
import com.pokedex.persistence.repository.relational.PokemonJpaRepository;
import com.pokedex.persistence.repository.relational.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FavoriteServiceImpl")
class FavoriteServiceImplTest {

    @Mock private FavoriteJpaRepository favoriteRepository;
    @Mock private UserJpaRepository userRepository;
    @Mock private PokemonJpaRepository pokemonRepository;

    @InjectMocks private FavoriteServiceImpl service;

    private static final String USER_EMAIL = "ash@pokemon.com";
    private static final Long POKEMON_ID = 25L;

    private UserEntity userEntity;
    private PokemonEntity pokemonEntity;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(1L).username("ash").email(USER_EMAIL)
                .password("encoded").role(UserEntity.Role.TRAINER)
                .build();

        pokemonEntity = PokemonEntity.builder()
                .id(POKEMON_ID).nationalNumber(25).name("Pikachu")
                .build();
    }

    @Nested
    @DisplayName("addFavorite")
    class AddFavorite {

        @Test
        @DisplayName("Dado un Pokémon ya en favoritos, cuando se agrega, entonces lanza BusinessException con código DUPLICATE_FAVORITE")
        void givenAlreadyFavorite_whenAdd_thenThrowsDuplicateFavorite() {
            when(favoriteRepository.existsByUserEmailAndPokemonId(USER_EMAIL, POKEMON_ID)).thenReturn(true);

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> service.addFavorite(USER_EMAIL, POKEMON_ID));

            assertThat(ex.getErrorCode()).isEqualTo("DUPLICATE_FAVORITE");
            verify(favoriteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Dado un usuario inexistente, cuando se agrega favorito, entonces lanza ResourceNotFoundException")
        void givenNonExistingUser_whenAdd_thenThrowsResourceNotFoundException() {
            when(favoriteRepository.existsByUserEmailAndPokemonId(USER_EMAIL, POKEMON_ID)).thenReturn(false);
            when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> service.addFavorite(USER_EMAIL, POKEMON_ID));

            verify(favoriteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Dado un Pokémon inexistente, cuando se agrega favorito, entonces lanza ResourceNotFoundException")
        void givenNonExistingPokemon_whenAdd_thenThrowsResourceNotFoundException() {
            when(favoriteRepository.existsByUserEmailAndPokemonId(USER_EMAIL, POKEMON_ID)).thenReturn(false);
            when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(userEntity));
            when(pokemonRepository.findById(POKEMON_ID)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> service.addFavorite(USER_EMAIL, POKEMON_ID));

            verify(favoriteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Dado usuario y Pokémon válidos, cuando se agrega favorito, entonces guarda la entidad")
        void givenValidUserAndPokemon_whenAdd_thenSavesFavorite() {
            when(favoriteRepository.existsByUserEmailAndPokemonId(USER_EMAIL, POKEMON_ID)).thenReturn(false);
            when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(userEntity));
            when(pokemonRepository.findById(POKEMON_ID)).thenReturn(Optional.of(pokemonEntity));

            service.addFavorite(USER_EMAIL, POKEMON_ID);

            verify(favoriteRepository).save(any(FavoriteEntity.class));
        }
    }

    @Nested
    @DisplayName("removeFavorite")
    class RemoveFavorite {

        @Test
        @DisplayName("Dado un favorito inexistente, cuando se quita, entonces lanza ResourceNotFoundException")
        void givenNonExistingFavorite_whenRemove_thenThrowsResourceNotFoundException() {
            when(favoriteRepository.findByUserEmailAndPokemonId(USER_EMAIL, POKEMON_ID))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> service.removeFavorite(USER_EMAIL, POKEMON_ID));

            verify(favoriteRepository, never()).delete(any());
        }

        @Test
        @DisplayName("Dado un favorito existente, cuando se quita, entonces llama delete en el repositorio")
        void givenExistingFavorite_whenRemove_thenDeletesFavorite() {
            FavoriteEntity favorite = FavoriteEntity.builder()
                    .user(userEntity).pokemon(pokemonEntity).build();
            when(favoriteRepository.findByUserEmailAndPokemonId(USER_EMAIL, POKEMON_ID))
                    .thenReturn(Optional.of(favorite));

            service.removeFavorite(USER_EMAIL, POKEMON_ID);

            verify(favoriteRepository).delete(favorite);
        }
    }

    @Nested
    @DisplayName("findFavoritesByUserEmail")
    class FindFavoritesByUserEmail {

        @Test
        @DisplayName("Dado un usuario con favoritos, cuando se buscan, entonces retorna los IDs de los Pokémon")
        void givenUserWithFavorites_whenFind_thenReturnsPokemonIds() {
            PokemonEntity bulbasaur = PokemonEntity.builder()
                    .id(1L).nationalNumber(1).name("Bulbasaur").build();
            FavoriteEntity fav1 = FavoriteEntity.builder().user(userEntity).pokemon(pokemonEntity).build();
            FavoriteEntity fav2 = FavoriteEntity.builder().user(userEntity).pokemon(bulbasaur).build();
            when(favoriteRepository.findAllByUserEmail(USER_EMAIL)).thenReturn(List.of(fav1, fav2));

            List<Long> result = service.findFavoritesByUserEmail(USER_EMAIL);

            assertThat(result).containsExactly(POKEMON_ID, 1L);
        }

        @Test
        @DisplayName("Dado un usuario sin favoritos, cuando se buscan, entonces retorna lista vacía")
        void givenUserWithNoFavorites_whenFind_thenReturnsEmptyList() {
            when(favoriteRepository.findAllByUserEmail(USER_EMAIL)).thenReturn(List.of());

            List<Long> result = service.findFavoritesByUserEmail(USER_EMAIL);

            assertThat(result).isEmpty();
        }
    }
}
