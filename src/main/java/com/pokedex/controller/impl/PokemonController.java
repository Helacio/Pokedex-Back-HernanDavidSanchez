package com.pokedex.controller.impl;

import com.pokedex.controller.api.PokemonApi;
import com.pokedex.controller.dto.request.PokemonRequest;
import com.pokedex.controller.dto.response.PokemonResponse;
import com.pokedex.controller.mapper.PokemonDtoMapper;
import com.pokedex.core.model.Pokemon;
import com.pokedex.core.service.interfaces.PokemonFilterCriteria;
import com.pokedex.core.service.interfaces.PokemonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController 
@RequiredArgsConstructor 
public class PokemonController implements PokemonApi { 
 
    private final PokemonService pokemonService; 
    private final PokemonDtoMapper mapper; 
 
    @Override 
    public ResponseEntity<Page<PokemonResponse>> findAll(Pageable pageable) { 
        return 
ResponseEntity.ok(pokemonService.findAll(pageable).map(mapper::toResponse)); 
    } 
 
    @Override 
    public ResponseEntity<PokemonResponse> findById(Long id) { 
        return ResponseEntity.ok(mapper.toResponse(pokemonService.findById(id))); 
    } 
 
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PokemonResponse> create(PokemonRequest request) {
        Pokemon created = pokemonService.create(mapper.toDomain(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(mapper.toResponse(created));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PokemonResponse> update(Long id, PokemonRequest request) {
        return ResponseEntity.ok(mapper.toResponse(pokemonService.update(id, mapper.toDomain(request))));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(Long id) {
        pokemonService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<PokemonResponse>> filter(
            List<String> types, String region, Integer generation,
            Boolean hasMega, Integer minTotalStats, Integer maxTotalStats) {
        PokemonFilterCriteria criteria = new PokemonFilterCriteria(
                types, region, generation, hasMega, minTotalStats, maxTotalStats);
        List<PokemonResponse> result = pokemonService.filterByCriteria(criteria)
                .stream().map(mapper::toResponse).toList();
        return ResponseEntity.ok(result);
    }
}