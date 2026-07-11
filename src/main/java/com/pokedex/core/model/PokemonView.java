package com.pokedex.core.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class PokemonView {
    Long pokemonId;
    String pokemonName;
    Long viewCount;
    LocalDateTime lastViewed;
}