package com.pokedex.persistence.repository.relational;

import com.pokedex.persistence.entity.relational.PokemonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PokemonJpaRepository
        extends JpaRepository<PokemonEntity, Long>, JpaSpecificationExecutor<PokemonEntity> {

    // Detalle de 1 Pokémon: carga TODOS los atributos en una sola query
    @EntityGraph(attributePaths = {"types", "stats", "region"})
    @Query("SELECT p FROM PokemonEntity p WHERE p.id = :id")
    Optional<PokemonEntity> findByIdWithTypesAndStats(@Param("id") Long id);

    // Detalle por número nacional: mismo nivel de detalle que por ID
    @EntityGraph(attributePaths = {"types", "stats", "region"})
    @Query("SELECT p FROM PokemonEntity p WHERE p.nationalNumber = :number")
    Optional<PokemonEntity> findByNationalNumberWithDetails(@Param("number") Integer number);

    // Listado paginado: solo carga tipos (evita N+1 sin traer stats innecesarios)
    @EntityGraph(attributePaths = {"types"})
    @Query("SELECT p FROM PokemonEntity p")
    Page<PokemonEntity> findAllWithTypes(Pageable pageable);

    // Filtros con Specification: carga tipos y región para los resultados del WHERE
    @EntityGraph(attributePaths = {"types", "region"})
    List<PokemonEntity> findAll(Specification<PokemonEntity> spec);

    boolean existsByNationalNumber(Integer nationalNumber);
}