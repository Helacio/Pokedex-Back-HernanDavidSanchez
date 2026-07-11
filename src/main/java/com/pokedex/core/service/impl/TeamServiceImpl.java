package com.pokedex.core.service.impl;

import com.pokedex.core.exception.BusinessException;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Team;
import com.pokedex.core.service.interfaces.TeamService;
import com.pokedex.core.validator.TeamValidator;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.entity.relational.TeamEntity;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.repository.relational.PokemonJpaRepository;
import com.pokedex.persistence.repository.relational.TeamJpaRepository;
import com.pokedex.persistence.repository.relational.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamServiceImpl implements TeamService {

    private final TeamJpaRepository teamRepository;
    private final UserJpaRepository userRepository;
    private final PokemonJpaRepository pokemonRepository;
    private final TeamValidator teamValidator;

    @Override
    @Transactional(readOnly = true)
    public List<Team> findAllByUserEmail(String email) {
        log.debug("Obteniendo equipos del usuario: {}", email);
        return teamRepository.findAllByUserEmail(email)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public Team create(String userEmail, Team team) {
        log.info("Creando equipo '{}' para usuario: {}", team.getName(), userEmail);

        teamValidator.validate(team);

        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", userEmail));

        List<PokemonEntity> pokemons = obtenerPokemons(team.getPokemonIds());

        TeamEntity entity = TeamEntity.builder()
                .name(team.getName())
                .user(user)
                .pokemons(pokemons)
                .build();

        TeamEntity saved = teamRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public Team update(Long teamId, String userEmail, Team team) {
        log.info("Actualizando equipo {} del usuario: {}", teamId, userEmail);

        teamValidator.validate(team);

        TeamEntity entity = teamRepository.findByIdWithDetails(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo", "id", teamId));

        verificarPropietario(entity, userEmail);

        List<PokemonEntity> pokemons = obtenerPokemons(team.getPokemonIds());
        entity.updateName(team.getName());
        entity.updatePokemons(pokemons);

        TeamEntity saved = teamRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public void delete(Long teamId, String userEmail) {
        log.info("Eliminando equipo {} del usuario: {}", teamId, userEmail);

        TeamEntity entity = teamRepository.findByIdWithDetails(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipo", "id", teamId));

        verificarPropietario(entity, userEmail);
        teamRepository.delete(entity);
    }

    // Verifica que el equipo pertenezca al usuario que hace la petición
    private void verificarPropietario(TeamEntity team, String userEmail) {
        if (!team.getUser().getEmail().equals(userEmail)) {
            throw new BusinessException(
                    "No tienes permiso para modificar este equipo", "FORBIDDEN_TEAM_ACCESS");
        }
    }

    // Obtiene las entidades Pokémon validando que existan
    private List<PokemonEntity> obtenerPokemons(List<Long> pokemonIds) {
        List<PokemonEntity> pokemons = pokemonRepository.findAllById(pokemonIds);
        if (pokemons.size() != pokemonIds.size()) {
            throw new ResourceNotFoundException("Pokémon", "ids", pokemonIds);
        }
        return pokemons;
    }

    // Convierte TeamEntity al modelo de dominio Team
    private Team toDomain(TeamEntity entity) {
        List<Long> pokemonIds = entity.getPokemons()
                .stream()
                .map(PokemonEntity::getId)
                .toList();

        return Team.builder()
                .id(entity.getId())
                .name(entity.getName())
                .userId(entity.getUser().getId())
                .pokemonIds(pokemonIds)
                .build();
    }
}
