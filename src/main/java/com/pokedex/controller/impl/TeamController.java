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
        String email = getEmailFromContext();
        List<TeamResponse> teams = teamService.findAllByUserEmail(email)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(teams);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TeamResponse> create(TeamRequest request) {
        String email = getEmailFromContext();
        Team team = Team.builder()
                .name(request.name())
                .pokemonIds(request.pokemonIds())
                .build();
        Team created = teamService.create(email, team);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TeamResponse> update(Long id, TeamRequest request) {
        String email = getEmailFromContext();
        Team team = Team.builder()
                .name(request.name())
                .pokemonIds(request.pokemonIds())
                .build();
        Team updated = teamService.update(id, email, team);
        return ResponseEntity.ok(toResponse(updated));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(Long id) {
        String email = getEmailFromContext();
        teamService.delete(id, email);
        return ResponseEntity.noContent().build();
    }

    private String getEmailFromContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private TeamResponse toResponse(Team team) {
        return new TeamResponse(team.getId(), team.getName(), team.getPokemonIds());
    }
}
