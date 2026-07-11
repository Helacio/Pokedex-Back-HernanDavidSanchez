package com.pokedex.controller.impl;

import com.pokedex.controller.api.FavoriteApi;
import com.pokedex.core.service.interfaces.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController implements FavoriteApi {

    private final FavoriteService favoriteService;

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Long>> findMyFavorites() {
        String email = getEmailFromContext();
        List<Long> favorites = favoriteService.findFavoritesByUserEmail(email);
        return ResponseEntity.ok(favorites);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addFavorite(Long pokemonId) {
        String email = getEmailFromContext();
        favoriteService.addFavorite(email, pokemonId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeFavorite(Long pokemonId) {
        String email = getEmailFromContext();
        favoriteService.removeFavorite(email, pokemonId);
        return ResponseEntity.noContent().build();
    }

    private String getEmailFromContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
