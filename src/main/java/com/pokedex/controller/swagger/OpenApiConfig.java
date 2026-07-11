package com.pokedex.controller.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pokédex API")
                        .description("API REST para gestión del catálogo Pokémon. " +
                                "Los endpoints públicos no requieren token. " +
                                "Los endpoints ADMIN requieren autenticarse primero en /v1/auth/login " +
                                "y luego usar el token con el botón 'Authorize'.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Hernán David Sánchez")
                                .email("hdher@dosw.com")))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingresa el token JWT obtenido en POST /v1/auth/login")));
    }
}
