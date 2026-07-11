package com.pokedex.controller.api;

import com.pokedex.controller.dto.request.PokemonRequest;
import com.pokedex.controller.dto.response.PokemonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pokemon", description = "Gestión del catálogo de Pokémon") 
@RequestMapping("/v1/pokemon") 
public interface PokemonApi { 
 
    @Operation(summary = "Listar todos los Pokémon", 
               description = "Retorna lista paginada. Acceso público.") 
    @ApiResponses({ 
        @ApiResponse(responseCode = "200", description = "Lista obtenida", 
            content = @Content(schema = @Schema(implementation = 
PokemonResponse.class))), 
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos") 
    }) 
    @GetMapping 
    ResponseEntity<Page<PokemonResponse>> findAll( 
        @PageableDefault(size = 20, sort = "nationalNumber") Pageable pageable); 
 
    @Operation(summary = "Obtener Pokémon por ID") 
    @ApiResponse(responseCode = "404", description = "Pokémon no encontrado") 
    @GetMapping("/{id}") 
    ResponseEntity<PokemonResponse> findById(@PathVariable Long id); 
 
    @Operation(summary = "Crear Pokémon", description = "Solo ADMIN") 
    @SecurityRequirement(name = "Bearer Authentication") 
    @PostMapping 
    ResponseEntity<PokemonResponse> create(@Valid @RequestBody PokemonRequest request); 
 
    @Operation(summary = "Actualizar Pokémon", description = "Solo ADMIN") 
    @SecurityRequirement(name = "Bearer Authentication") 
    @PutMapping("/{id}") 
    ResponseEntity<PokemonResponse> update(@PathVariable Long id, 
                                           @Valid @RequestBody PokemonRequest request); 
 
    @Operation(summary = "Eliminar Pokémon", description = "Solo ADMIN")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);

    @Operation(summary = "Filtrar Pokémon",
               description = "Filtra por tipo, región, generación, hasMega y rango de stats. Acceso público.")
    @ApiResponse(responseCode = "200", description = "Lista filtrada")
    @GetMapping("/filter")
    ResponseEntity<List<PokemonResponse>> filter(
            @RequestParam(required = false) List<String> types,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer generation,
            @RequestParam(required = false) Boolean hasMega,
            @RequestParam(required = false) Integer minTotalStats,
            @RequestParam(required = false) Integer maxTotalStats);
}