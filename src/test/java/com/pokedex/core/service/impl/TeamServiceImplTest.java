package com.pokedex.core.service.impl;

import com.pokedex.core.exception.BusinessException;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Team;
import com.pokedex.core.validator.TeamValidator;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.entity.relational.TeamEntity;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.repository.relational.PokemonJpaRepository;
import com.pokedex.persistence.repository.relational.TeamJpaRepository;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeamServiceImpl")
class TeamServiceImplTest {

    @Mock private TeamJpaRepository teamRepository;
    @Mock private UserJpaRepository userRepository;
    @Mock private PokemonJpaRepository pokemonRepository;
    @Mock private TeamValidator teamValidator;

    @InjectMocks private TeamServiceImpl teamService;

    private static final String USER_EMAIL = "ash@pokemon.com";
    private static final Long TEAM_ID = 1L;

    private UserEntity userEntity;
    private PokemonEntity pikachu;
    private TeamEntity teamEntity;
    private Team teamDomain;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(1L).username("ash").email(USER_EMAIL)
                .password("encoded").role(UserEntity.Role.TRAINER)
                .build();

        pikachu = PokemonEntity.builder()
                .id(25L).nationalNumber(25).name("Pikachu")
                .build();

        teamEntity = TeamEntity.builder()
                .id(TEAM_ID).name("Dream Team").user(userEntity)
                .pokemons(List.of(pikachu))
                .build();

        teamDomain = Team.builder()
                .id(TEAM_ID).name("Dream Team").userId(1L)
                .pokemonIds(List.of(25L))
                .build();
    }

    @Nested
    @DisplayName("findAllByUserEmail")
    class FindAllByUserEmail {

        @Test
        @DisplayName("Dado un usuario con equipos, cuando se buscan, entonces retorna la lista mapeada a dominio")
        void givenUserWithTeams_whenFindAll_thenReturnsMappedList() {
            when(teamRepository.findAllByUserEmail(USER_EMAIL)).thenReturn(List.of(teamEntity));

            List<Team> result = teamService.findAllByUserEmail(USER_EMAIL);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Dream Team");
            assertThat(result.get(0).getPokemonIds()).containsExactly(25L);
            assertThat(result.get(0).getUserId()).isEqualTo(1L);
            verify(teamRepository).findAllByUserEmail(USER_EMAIL);
        }

        @Test
        @DisplayName("Dado un usuario sin equipos, cuando se buscan, entonces retorna lista vacía")
        void givenUserWithNoTeams_whenFindAll_thenReturnsEmptyList() {
            when(teamRepository.findAllByUserEmail(USER_EMAIL)).thenReturn(List.of());

            List<Team> result = teamService.findAllByUserEmail(USER_EMAIL);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("Dado un equipo válido, cuando se crea, entonces guarda y retorna el modelo de dominio")
        void givenValidTeam_whenCreate_thenSavesAndReturnsDomain() {
            when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(userEntity));
            when(pokemonRepository.findAllById(List.of(25L))).thenReturn(List.of(pikachu));
            when(teamRepository.save(any(TeamEntity.class))).thenReturn(teamEntity);

            Team result = teamService.create(USER_EMAIL, teamDomain);

            assertThat(result.getName()).isEqualTo("Dream Team");
            assertThat(result.getPokemonIds()).containsExactly(25L);
            verify(teamValidator).validate(teamDomain);
            verify(teamRepository).save(any(TeamEntity.class));
        }

        @Test
        @DisplayName("Dado que la validación falla, cuando se crea, entonces propaga BusinessException sin guardar")
        void givenInvalidTeam_whenCreate_thenThrowsBusinessExceptionWithoutSaving() {
            doThrow(new BusinessException("Nombre vacío", "INVALID_TEAM_NAME"))
                    .when(teamValidator).validate(teamDomain);

            assertThrows(BusinessException.class, () -> teamService.create(USER_EMAIL, teamDomain));

            verify(teamRepository, never()).save(any());
        }

        @Test
        @DisplayName("Dado un usuario inexistente, cuando se crea, entonces lanza ResourceNotFoundException")
        void givenNonExistingUser_whenCreate_thenThrowsResourceNotFoundException() {
            when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> teamService.create(USER_EMAIL, teamDomain));

            verify(teamRepository, never()).save(any());
        }

        @Test
        @DisplayName("Dado Pokémon inexistente en la lista, cuando se crea, entonces lanza ResourceNotFoundException")
        void givenMissingPokemon_whenCreate_thenThrowsResourceNotFoundException() {
            when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(userEntity));
            when(pokemonRepository.findAllById(List.of(25L))).thenReturn(List.of());

            assertThrows(ResourceNotFoundException.class,
                    () -> teamService.create(USER_EMAIL, teamDomain));

            verify(teamRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("Dado un equipo existente y propietario correcto, cuando se actualiza, entonces retorna el equipo actualizado")
        void givenValidOwner_whenUpdate_thenReturnsUpdatedTeam() {
            Team updatedDomain = Team.builder()
                    .id(TEAM_ID).name("New Name").userId(1L).pokemonIds(List.of(25L)).build();
            TeamEntity updatedEntity = TeamEntity.builder()
                    .id(TEAM_ID).name("New Name").user(userEntity)
                    .pokemons(List.of(pikachu))
                    .build();

            when(teamRepository.findByIdWithDetails(TEAM_ID)).thenReturn(Optional.of(teamEntity));
            when(pokemonRepository.findAllById(List.of(25L))).thenReturn(List.of(pikachu));
            when(teamRepository.save(teamEntity)).thenReturn(updatedEntity);

            Team result = teamService.update(TEAM_ID, USER_EMAIL, updatedDomain);

            assertThat(result.getName()).isEqualTo("New Name");
            verify(teamValidator).validate(updatedDomain);
            verify(teamRepository).save(teamEntity);
        }

        @Test
        @DisplayName("Dado un equipo inexistente, cuando se actualiza, entonces lanza ResourceNotFoundException")
        void givenNonExistingTeam_whenUpdate_thenThrowsResourceNotFoundException() {
            when(teamRepository.findByIdWithDetails(TEAM_ID)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> teamService.update(TEAM_ID, USER_EMAIL, teamDomain));

            verify(teamRepository, never()).save(any());
        }

        @Test
        @DisplayName("Dado un usuario no propietario, cuando se actualiza, entonces lanza BusinessException con código FORBIDDEN_TEAM_ACCESS")
        void givenNonOwner_whenUpdate_thenThrowsForbiddenTeamAccess() {
            when(teamRepository.findByIdWithDetails(TEAM_ID)).thenReturn(Optional.of(teamEntity));

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> teamService.update(TEAM_ID, "misty@pokemon.com", teamDomain));

            assertThat(ex.getErrorCode()).isEqualTo("FORBIDDEN_TEAM_ACCESS");
            verify(teamRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("Dado un equipo existente y propietario correcto, cuando se elimina, entonces llama delete en el repositorio")
        void givenValidOwner_whenDelete_thenCallsRepositoryDelete() {
            when(teamRepository.findByIdWithDetails(TEAM_ID)).thenReturn(Optional.of(teamEntity));

            teamService.delete(TEAM_ID, USER_EMAIL);

            verify(teamRepository).delete(teamEntity);
        }

        @Test
        @DisplayName("Dado un equipo inexistente, cuando se elimina, entonces lanza ResourceNotFoundException")
        void givenNonExistingTeam_whenDelete_thenThrowsResourceNotFoundException() {
            when(teamRepository.findByIdWithDetails(TEAM_ID)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> teamService.delete(TEAM_ID, USER_EMAIL));

            verify(teamRepository, never()).delete(any());
        }

        @Test
        @DisplayName("Dado un usuario no propietario, cuando se elimina, entonces lanza BusinessException con código FORBIDDEN_TEAM_ACCESS")
        void givenNonOwner_whenDelete_thenThrowsForbiddenTeamAccess() {
            when(teamRepository.findByIdWithDetails(TEAM_ID)).thenReturn(Optional.of(teamEntity));

            BusinessException ex = assertThrows(BusinessException.class,
                    () -> teamService.delete(TEAM_ID, "misty@pokemon.com"));

            assertThat(ex.getErrorCode()).isEqualTo("FORBIDDEN_TEAM_ACCESS");
            verify(teamRepository, never()).delete(any());
        }
    }
}
