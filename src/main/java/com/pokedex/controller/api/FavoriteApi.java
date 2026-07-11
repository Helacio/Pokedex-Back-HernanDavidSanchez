package com.pokedex.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Favorites", description = "Gestión de Pokémon favoritos del usuario")
@RequestMapping("/v1/favorites")
@SecurityRequirement(name = "Bearer Authentication")
public interface FavoriteApi {

    @Operation(summary = "Ver mis favoritos", description = "Retorna los IDs de los Pokémon favoritos del usuario autenticado.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de IDs de Pokémon favoritos"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping
    ResponseEntity<List<Long>> findMyFavorites();

    @Operation(summary = "Agregar favorito", description = "Agrega un Pokémon a la lista de favoritos del usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pokémon agregado a favoritos"),
        @ApiResponse(responseCode = "400", description = "El Pokémon ya está en favoritos"),
        @ApiResponse(responseCode = "404", description = "Pokémon no encontrado")
    })
    @PostMapping("/{pokemonId}")
    ResponseEntity<Void> addFavorite(@PathVariable Long pokemonId);

    @Operation(summary = "Quitar favorito", description = "Quita un Pokémon de la lista de favoritos del usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Pokémon quitado de favoritos"),
        @ApiResponse(responseCode = "404", description = "El Pokémon no está en favoritos")
    })
    @DeleteMapping("/{pokemonId}")
    ResponseEntity<Void> removeFavorite(@PathVariable Long pokemonId);
}
