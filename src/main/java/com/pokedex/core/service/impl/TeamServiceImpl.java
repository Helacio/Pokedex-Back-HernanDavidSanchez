package com.pokedex.core.service.impl;

import com.pokedex.core.model.Team;
import com.pokedex.core.service.interfaces.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamServiceImpl implements TeamService {

    @Override
    public List<Team> findAllByUserEmail(String email) {
        log.debug("Obteniendo equipos del usuario: {}", email);
        return List.of();
    }

    @Override
    public Team create(String userEmail, Team team) {
        log.info("Creando equipo '{}' para usuario: {}", team.getName(), userEmail);
        return team;
    }
}
