package com.pokedex.core.model;

import java.util.List;
import lombok.Value;
import lombok.Builder;

 
@Value           // Lombok: clase inmutable — todos los campos final, solo getters, no setters 
@Builder(toBuilder = true)   // toBuilder permite crear copias modificadas 
public class Pokemon { 
    Long id; 
    Integer nationalNumber; 
    String name; 
    String description; 
    String imageUrl; 
    List<String> types;   // Solo los nombres, no referencias a otras entidades 
    String region;
        Integer generation; 
    Boolean hasMega; 
    PokemonStats stats; 
} 