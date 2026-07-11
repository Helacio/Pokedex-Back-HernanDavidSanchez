package com.pokedex.core.service.interfaces;

import java.util.List;

public interface FavoriteService {

    // Agrega un Pokémon a la lista de favoritos del usuario
    void addFavorite(String userEmail, Long pokemonId);

    // Quita un Pokémon de la lista de favoritos del usuario
    void removeFavorite(String userEmail, Long pokemonId);

    // Retorna los IDs de los Pokémon favoritos del usuario
    List<Long> findFavoritesByUserEmail(String userEmail);
}
