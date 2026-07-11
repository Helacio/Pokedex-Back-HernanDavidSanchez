package com.pokedex.controller.dto.request;

public record UpdateUserRequest(
        String role,    // Nuevo rol: GUEST, TRAINER o ADMIN
        Boolean active  // true = activo, false = desactivado
) {}
