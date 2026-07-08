package com.pokedex.core.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Team {
    Long id;
    String name;
    Long userId;
    List<Long> pokemonIds;
}
