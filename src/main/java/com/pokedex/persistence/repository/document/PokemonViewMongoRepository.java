package com.pokedex.persistence.repository.document;

import com.pokedex.persistence.entity.document.PokemonViewDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PokemonViewMongoRepository extends MongoRepository<PokemonViewDocument, String> {
    Optional<PokemonViewDocument> findByPokemonId(Long pokemonId);
}