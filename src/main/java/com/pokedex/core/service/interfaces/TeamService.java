package com.pokedex.core.service.interfaces;

import com.pokedex.core.model.Team;

import java.util.List;

public interface TeamService {

    List<Team> findAllByUserEmail(String email);

    Team create(String userEmail, Team team);
}
