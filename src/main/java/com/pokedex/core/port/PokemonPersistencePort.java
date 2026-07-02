package com.pokedex.core.port;

import com.pokedex.core.model.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pokedex.core.service.interfaces.PokemonFilterCriteria;

import java.util.List;
import java.util.Optional;


public interface PokemonPersistencePort {
    Optional<Pokemon> findById(Long id);
    Optional<Pokemon> findByNationalNumber(Integer number);
    Page<Pokemon> findAll(Pageable pageable);
    boolean existsByNationalNumber(Integer number);
    Pokemon save(Pokemon pokemon);
    void deleteById(Long id);
    List<Pokemon> findByCriteria(PokemonFilterCriteria criteria);
}