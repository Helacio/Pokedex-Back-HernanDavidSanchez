package com.pokedex.controller.api;

import com.pokedex.controller.dto.request.LoginRequest;
import com.pokedex.controller.dto.request.RegisterRequest;
import com.pokedex.controller.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Auth", description = "Autenticación y registro de usuarios")
@RequestMapping("/v1/auth")
public interface AuthApi {

    @Operation(summary = "Registrar nuevo usuario", description = "Crea una cuenta y retorna JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario registrado",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "409", description = "Email ya registrado")
    })
    @PostMapping("/register")
    ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request);

    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y retorna JWT.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login exitoso",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    @PostMapping("/login")
    ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request);

    @Operation(summary = "Login con Google OAuth2",
               description = "Redirige al proveedor OAuth2 de Google. Acceso público.")
    @ApiResponse(responseCode = "302", description = "Redirección a Google")
    @GetMapping("/oauth2/google")
    ResponseEntity<Void> oauth2Google();
}
