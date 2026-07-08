package com.pokedex.controller.api;

import com.pokedex.controller.dto.request.TeamRequest;
import com.pokedex.controller.dto.response.TeamResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Teams", description = "Gestión de equipos Pokémon del entrenador")
@RequestMapping("/v1/teams")
@SecurityRequirement(name = "Bearer Authentication")
public interface TeamApi {

    @Operation(summary = "Ver mis equipos", description = "Retorna todos los equipos del usuario autenticado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipos obtenidos",
            content = @Content(schema = @Schema(implementation = TeamResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping
    ResponseEntity<List<TeamResponse>> findMyTeams();

    @Operation(summary = "Crear equipo", description = "Crea un nuevo equipo Pokémon para el usuario autenticado.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Equipo creado",
            content = @Content(schema = @Schema(implementation = TeamResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @PostMapping
    ResponseEntity<TeamResponse> create(@Valid @RequestBody TeamRequest request);
}
