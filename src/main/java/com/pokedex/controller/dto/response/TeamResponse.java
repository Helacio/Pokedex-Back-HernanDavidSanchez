package com.pokedex.controller.dto.response;

import java.util.List;

public record TeamResponse(
        Long id,
        String name,
        List<Long> pokemonIds
) {}
