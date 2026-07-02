package com.pokedex.persistence.mapper;

import com.pokedex.core.model.Pokemon;
import com.pokedex.persistence.entity.relational.PokemonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PokemonPersistenceMapper {

    @Mapping(target = "types", expression = "java(entity.getTypes().stream().map(t -> t.getName()).toList())")
    @Mapping(target = "region", expression = "java(entity.getRegion() != null ? entity.getRegion().getName() : null)")
    @Mapping(target = "generation", ignore = true)
    @Mapping(target = "hasMega", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "stats", ignore = true)
    Pokemon toDomain(PokemonEntity entity);

    @Mapping(target = "types", ignore = true)
    @Mapping(target = "stats", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PokemonEntity toEntity(Pokemon pokemon);
}