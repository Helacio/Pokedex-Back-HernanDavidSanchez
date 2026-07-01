package com.pokedex.persistence.repository.document;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.pokedex.persistence.entity.document.PokemonViewDocument;

public interface PokemonViewMongoRepository extends MongoRepository<PokemonViewDocument, String> {
}
