package com.pokedex.core.service.impl;

import com.pokedex.core.exception.BusinessException;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.service.interfaces.FavoriteService;
import com.pokedex.persistence.entity.relational.FavoriteEntity;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.entity.relational.UserEntity;
import com.pokedex.persistence.repository.relational.FavoriteJpaRepository;
import com.pokedex.persistence.repository.relational.PokemonJpaRepository;
import com.pokedex.persistence.repository.relational.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteJpaRepository favoriteRepository;
    private final UserJpaRepository userRepository;
    private final PokemonJpaRepository pokemonRepository;

    @Override
    @Transactional
    public void addFavorite(String userEmail, Long pokemonId) {
        log.info("Agregando Pokémon {} a favoritos del usuario: {}", pokemonId, userEmail);

        // Valida que el Pokémon no esté ya en favoritos (RN-23)
        if (favoriteRepository.existsByUserEmailAndPokemonId(userEmail, pokemonId)) {
            throw new BusinessException(
                    "El Pokémon ya está en tu lista de favoritos", "DUPLICATE_FAVORITE");
        }

        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", userEmail));

        PokemonEntity pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> new ResourceNotFoundException("Pokémon", "id", pokemonId));

        FavoriteEntity favorite = FavoriteEntity.builder()
                .user(user)
                .pokemon(pokemon)
                .build();

        favoriteRepository.save(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(String userEmail, Long pokemonId) {
        log.info("Quitando Pokémon {} de favoritos del usuario: {}", pokemonId, userEmail);

        FavoriteEntity favorite = favoriteRepository
                .findByUserEmailAndPokemonId(userEmail, pokemonId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorito", "pokemonId", pokemonId));

        favoriteRepository.delete(favorite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findFavoritesByUserEmail(String userEmail) {
        log.debug("Obteniendo favoritos del usuario: {}", userEmail);
        return favoriteRepository.findAllByUserEmail(userEmail)
                .stream()
                .map(f -> f.getPokemon().getId())
                .toList();
    }
}
