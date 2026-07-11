package com.pokedex.core.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AdminMetrics {
    long totalConsultas;
    long pokemonDistintosConsultados;
    List<PokemonView> topPokemon;
}

