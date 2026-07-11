package com.pokedex.core.service.interfaces;

import com.pokedex.core.model.Team;

import java.util.List;

public interface TeamService {

    List<Team> findAllByUserEmail(String email);

    Team create(String userEmail, Team team);

    Team update(Long teamId, String userEmail, Team team);

    void delete(Long teamId, String userEmail);
}
