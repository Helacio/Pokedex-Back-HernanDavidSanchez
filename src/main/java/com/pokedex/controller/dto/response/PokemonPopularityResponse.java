package com.pokedex.controller.dto.response;

import java.time.LocalDateTime;

public record PokemonPopularityResponse(
        Long pokemonId,
        String pokemonName,
        Long viewCount,
        LocalDateTime lastViewed
) {}