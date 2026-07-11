package com.pokedex.core.service.impl;

import com.pokedex.core.exception.DuplicateResourceException;
import com.pokedex.core.exception.ResourceNotFoundException;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.port.PokemonPersistencePort;
import com.pokedex.core.service.interfaces.PokemonFilterCriteria;
import com.pokedex.core.service.interfaces.PokemonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PokemonServiceImpl implements PokemonService {

    private final PokemonPersistencePort pokemonPort;

    @Override
    public Page<Pokemon> findAll(Pageable pageable) {
        log.debug("Obteniendo listado de Pokemon, página: {}", pageable.getPageNumber());
        return pokemonPort.findAll(pageable);
    }

    @Override
    public Pokemon findById(Long id) {
        log.debug("Buscando Pokemon con id: {}", id);
        return pokemonPort.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", id));
    }

    @Override
    public Pokemon findByNationalNumber(Integer number) {
        log.debug("Buscando Pokemon con número nacional: {}", number);
        return pokemonPort.findByNationalNumber(number)
            .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "nationalNumber", number));
    }

    @Override
    @Transactional
    public Pokemon create(Pokemon pokemon) {
        if (pokemonPort.existsByNationalNumber(pokemon.getNationalNumber())) {
            throw new DuplicateResourceException("Pokemon", "nationalNumber", pokemon.getNationalNumber());
        }
        return pokemonPort.save(pokemon);
    }

    @Override
    @Transactional
    public Pokemon update(Long id, Pokemon pokemon) {
        findById(id);
        Pokemon updated = pokemon.toBuilder().id(id).build();
        return pokemonPort.save(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findById(id);
        pokemonPort.deleteById(id);
    }

    @Override
    public List<Pokemon> filterByCriteria(PokemonFilterCriteria criteria) {
        log.debug("Filtrando Pokemon por criterios: {}", criteria);
        return pokemonPort.findByCriteria(criteria);
    }
}
