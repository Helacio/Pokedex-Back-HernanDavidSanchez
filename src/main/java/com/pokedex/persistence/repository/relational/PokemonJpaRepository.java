package com.pokedex.persistence.repository.relational;

import com.pokedex.persistence.entity.relational.PokemonEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PokemonJpaRepository extends JpaRepository<PokemonEntity, Long>, JpaSpecificationExecutor<PokemonEntity> {

    @EntityGraph(attributePaths = {"types", "stats", "region"})
    @Query("SELECT p FROM PokemonEntity p WHERE p.id = :id")
    Optional<PokemonEntity> findByIdWithTypesAndStats(@Param("id") Long id);

    boolean existsByNationalNumber(Integer nationalNumber);

    Optional<PokemonEntity> findByNationalNumber(Integer nationalNumber);
}