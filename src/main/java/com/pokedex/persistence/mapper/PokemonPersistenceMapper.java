package com.pokedex.persistence.mapper;

import com.pokedex.core.model.Pokemon;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import com.pokedex.persistence.entity.relational.TypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

 
@Mapper(componentModel = "spring", imports = TypeEntity.class)
public interface PokemonPersistenceMapper { 
 
    // PokemonEntity (JPA) → Pokemon (core model) 
    @Mapping(source = "region.name", target = "region") 
    @Mapping(expression = 
"java(entity.getTypes().stream().map(TypeEntity::getName).toList())", 
             target = "types") 
    Pokemon toDomain(PokemonEntity entity); 
 
    // Pokemon (core model) → PokemonEntity (JPA) 
    @Mapping(target = "region", ignore = true)    // se resuelve en el adapter 
    @Mapping(target = "types",  ignore = true)    // se resuelve en el adapter 
    PokemonEntity toEntity(Pokemon pokemon); 
}