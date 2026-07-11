package com.pokedex.controller.api;

import com.pokedex.controller.dto.request.UpdateUserRequest;
import com.pokedex.controller.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin - Users", description = "Administración de usuarios (solo ADMIN)")
@RequestMapping("/v1/admin")
@SecurityRequirement(name = "Bearer Authentication")
public interface UserApi {

    @Operation(summary = "Listar todos los usuarios", description = "Paginado. Solo ADMIN.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de usuarios",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @GetMapping("/users")
    ResponseEntity<Page<UserResponse>> findAllUsers(
            @PageableDefault(size = 20, sort = "id") Pageable pageable);

    @Operation(summary = "Actualizar usuario", description = "Cambia rol o el estado activo de un usuario Solo ADMIN")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario actualizado",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/users/{id}")
    ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request);
}
