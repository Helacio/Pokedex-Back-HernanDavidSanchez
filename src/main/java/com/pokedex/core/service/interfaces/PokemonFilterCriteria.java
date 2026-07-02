package com.pokedex.core.service.interfaces;

import java.util.List;

public record PokemonFilterCriteria(
    List<String> types,
    String region,
    Integer generation,
    Boolean hasMega,
    Integer minTotalStats,
    Integer maxTotalStats
) {}
