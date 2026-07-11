package com.pokedex.persistence.repository.relational;

import com.pokedex.persistence.entity.relational.TeamEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, Long> {

    // Trae todos los equipos de un usuario con sus Pokémon en una sola consulta
    @EntityGraph(attributePaths = {"pokemons", "user"})
    @Query("SELECT t FROM TeamEntity t WHERE t.user.email = :email")
    List<TeamEntity> findAllByUserEmail(@Param("email") String email);

    // Busca un equipo por ID cargando sus Pokémon y el usuario dueño
    @EntityGraph(attributePaths = {"pokemons", "user"})
    @Query("SELECT t FROM TeamEntity t WHERE t.id = :id")
    Optional<TeamEntity> findByIdWithDetails(@Param("id") Long id);
}
