package com.pokedex.core.service.interfaces;

import com.pokedex.core.model.AdminMetrics;
import com.pokedex.core.model.PokemonView;

import java.util.List;

public interface StatsService {
    void registerView(Long pokemonId, String pokemonName);
    List<PokemonView> mostViewed(int limit);
    AdminMetrics getAdminMetrics();
}