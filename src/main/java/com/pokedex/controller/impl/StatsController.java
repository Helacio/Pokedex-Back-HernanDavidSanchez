package com.pokedex.controller.impl;

import com.pokedex.controller.api.StatsApi;
import com.pokedex.controller.dto.response.AdminMetricsResponse;
import com.pokedex.controller.dto.response.PokemonPopularityResponse;
import com.pokedex.core.model.AdminMetrics;
import com.pokedex.core.model.PokemonView;
import com.pokedex.core.service.interfaces.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController implements StatsApi {

    private final StatsService statsService;

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PokemonPopularityResponse>> mostViewed(int limit) {
        List<PokemonPopularityResponse> response = statsService.mostViewed(limit).stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminMetricsResponse> adminMetrics() {
        AdminMetrics metrics = statsService.getAdminMetrics();
        AdminMetricsResponse response = new AdminMetricsResponse(
                metrics.getTotalConsultas(),
                metrics.getPokemonDistintosConsultados(),
                metrics.getTopPokemon().stream().map(this::toResponse).toList()
        );
        return ResponseEntity.ok(response);
    }

    private PokemonPopularityResponse toResponse(PokemonView view) {
        return new PokemonPopularityResponse(
                view.getPokemonId(), view.getPokemonName(), view.getViewCount(), view.getLastViewed());
    }
}