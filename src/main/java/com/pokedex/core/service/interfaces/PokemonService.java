package com.pokedex.core.service.interfaces;

import com.pokedex.core.model.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Interface for Pokemon service
 */
public interface PokemonService { 
    Page<Pokemon> findAll(Pageable pageable); 
    Pokemon findById(Long id);
    Pokemon findByNationalNumber(Integer number); 
    Pokemon create(Pokemon pokemon); 
    Pokemon update(Long id, Pokemon pokemon); 
    void delete(Long id); 
    List<Pokemon> filterByCriteria(PokemonFilterCriteria criteria); 
}

