package com.pokedex.controller.impl;

import com.pokedex.controller.api.TeamApi;
import com.pokedex.controller.dto.request.TeamRequest;
import com.pokedex.controller.dto.response.TeamResponse;
import com.pokedex.core.model.Team;
import com.pokedex.core.service.interfaces.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamController implements TeamApi {

    private final TeamService teamService;

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TeamResponse>> findMyTeams() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<TeamResponse> teams = teamService.findAllByUserEmail(email)
                .stream()
                .map(t -> new TeamResponse(t.getId(), t.getName(), t.getPokemonIds()))
                .toList();
        return ResponseEntity.ok(teams);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TeamResponse> create(TeamRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Team team = Team.builder()
                .name(request.name())
                .pokemonIds(request.pokemonIds())
                .build();
        Team created = teamService.create(email, team);
        TeamResponse response = new TeamResponse(created.getId(), created.getName(), created.getPokemonIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
