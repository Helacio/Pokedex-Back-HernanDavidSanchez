package com.pokedex.controller.mapper;

import com.pokedex.controller.dto.request.PokemonRequest;
import com.pokedex.controller.dto.response.PokemonResponse;
import com.pokedex.core.model.Pokemon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
 
 
@Mapper(componentModel = "spring") 
public interface PokemonDtoMapper { 
 
    // Pokemon (core) → PokemonResponse (DTO) 
    // MapStruct mapea automáticamente campos con el mismo nombre 
    @Mapping(source = "stats.specialAttack", target = "stats.specialAttack") 
    PokemonResponse toResponse(Pokemon pokemon); 
 
    // PokemonRequest (DTO) → Pokemon (core) 
    @Mapping(target = "id", ignore = true)           // id lo asigna la BD 
    @Mapping(target = "hasMega", constant = "false")  // valor por defecto 
    Pokemon toDomain(PokemonRequest request); 
 
    List<PokemonResponse> toResponseList(List<Pokemon> pokemons);

}