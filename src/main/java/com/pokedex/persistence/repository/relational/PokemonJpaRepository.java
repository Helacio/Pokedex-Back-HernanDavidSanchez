package com.pokedex.persistence.repository.relational;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pokedex.persistence.entity.relational.PokemonEntity;

public interface PokemonJpaRepository extends JpaRepository<PokemonEntity, Long> {
}
