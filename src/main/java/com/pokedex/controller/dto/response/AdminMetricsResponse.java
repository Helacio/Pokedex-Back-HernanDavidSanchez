package com.pokedex.controller.dto.response;

import java.util.List;

public record AdminMetricsResponse(
        long totalConsultas,
        long pokemonDistintosConsultados,
        List<PokemonPopularityResponse> topPokemon
) {}