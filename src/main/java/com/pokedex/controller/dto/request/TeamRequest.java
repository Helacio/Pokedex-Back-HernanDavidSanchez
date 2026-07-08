package com.pokedex.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TeamRequest(
        @NotBlank(message = "El nombre del equipo es obligatorio")
        @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
        String name,

        @NotEmpty(message = "El equipo debe tener al menos un Pokémon")
        @Size(max = 6, message = "Un equipo puede tener máximo 6 Pokémon")
        List<Long> pokemonIds
) {}
