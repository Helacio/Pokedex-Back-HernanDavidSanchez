package com.pokedex.controller.api;

import com.pokedex.controller.dto.response.AdminMetricsResponse;
import com.pokedex.controller.dto.response.PokemonPopularityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Stats", description = "Estadísticas de uso de la Pokédex")
@RequestMapping("/v1/stats")
@SecurityRequirement(name = "Bearer Authentication")
public interface StatsApi {

    @Operation(summary = "Pokémon más consultados",
               description = "Top de Pokémon con más vistas. Requiere estar autenticado.")
    @GetMapping("/popular")
    ResponseEntity<List<PokemonPopularityResponse>> mostViewed(@RequestParam(defaultValue = "10") int limit);

    @Operation(summary = "Métricas administrativas",
               description = "Total de consultas registradas en la Pokédex. Solo ADMIN.")
    @GetMapping("/admin")
    ResponseEntity<AdminMetricsResponse> adminMetrics();
}