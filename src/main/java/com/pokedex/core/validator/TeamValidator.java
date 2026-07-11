package com.pokedex.core.validator;

import com.pokedex.core.exception.BusinessException;
import com.pokedex.core.model.Team;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamValidator {

    private static final int MAX_TEAM_SIZE = 6;
    private static final int MIN_TEAM_SIZE = 1;

    public void validate(Team team) {
        validateName(team.getName());
        validatePokemonIds(team.getPokemonIds());
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(
                    "El nombre del equipo no puede estar vacío", "INVALID_TEAM_NAME");
        }
    }

    private void validatePokemonIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(
                    "El equipo debe tener al menos " + MIN_TEAM_SIZE + " Pokémon", "TEAM_TOO_SMALL");
        }
        if (ids.size() > MAX_TEAM_SIZE) {
            throw new BusinessException(
                    "El equipo no puede superar los " + MAX_TEAM_SIZE + " Pokémon", "TEAM_TOO_LARGE");
        }
        long uniqueCount = ids.stream().distinct().count();
        if (uniqueCount < ids.size()) {
            throw new BusinessException(
                    "El equipo no puede tener Pokémon duplicados", "DUPLICATE_POKEMON_IN_TEAM");
        }
    }
}
