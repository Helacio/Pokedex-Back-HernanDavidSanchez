package com.pokedex.core.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class User {
    Long id;
    String username;
    String email;
    String role;
    LocalDateTime createdAt;
}
