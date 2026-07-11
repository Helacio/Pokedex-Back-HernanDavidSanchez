package com.pokedex.persistence.repository.relational;

import com.pokedex.persistence.entity.relational.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteJpaRepository extends JpaRepository<FavoriteEntity, Long> {

    // Todos los favoritos del usuario con los datos del Pokémon cargados
    @Query("SELECT f FROM FavoriteEntity f JOIN FETCH f.pokemon WHERE f.user.email = :email")
    List<FavoriteEntity> findAllByUserEmail(@Param("email") String email);

    // Busca si ya existe el favorito para evitar duplicados
    @Query("SELECT f FROM FavoriteEntity f WHERE f.user.email = :email AND f.pokemon.id = :pokemonId")
    Optional<FavoriteEntity> findByUserEmailAndPokemonId(
            @Param("email") String email,
            @Param("pokemonId") Long pokemonId);

    boolean existsByUserEmailAndPokemonId(String userEmail, Long pokemonId);
}
