package com.pokedex.controller.dto.response;

public record AuthResponse(
        String token,
        String email,
        String role
) {}
