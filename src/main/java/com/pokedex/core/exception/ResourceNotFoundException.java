package com.pokedex.core.exception;

// Excepción específica — usa Java 21 Record para compactness 
public class ResourceNotFoundException extends BusinessException { 
    public ResourceNotFoundException(String resource, String field, Object value) { 
        super(resource + " con " + field + "=" + value + " no encontrado", "NOT_FOUND"); 
    } 
} 