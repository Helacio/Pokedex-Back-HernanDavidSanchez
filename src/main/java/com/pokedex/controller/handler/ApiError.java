package com.pokedex.controller.handler;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        int status,
        String errorCode,
        String message,
        String path,
        LocalDateTime timestamp,
        List<FieldError> fieldErrors
) {
    // Record anidado para errores de validación campo a campo
    public record FieldError(String field, String message) {}
}
