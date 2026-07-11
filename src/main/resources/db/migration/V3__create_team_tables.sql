-- ============================================================
-- V3__create_team_tables.sql
-- Tabla team y tabla de relación team_pokemon
-- Corresponde a TeamEntity con relación ManyToMany a PokemonEntity
-- ============================================================

-- 1. TEAM (depende de users)
CREATE TABLE team (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    user_id     BIGINT NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_team_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- 2. TEAM_POKEMON (tabla de unión ManyToMany: team <-> pokemon)
CREATE TABLE team_pokemon (
    team_id     BIGINT NOT NULL,
    pokemon_id  BIGINT NOT NULL,
    PRIMARY KEY (team_id, pokemon_id),
    CONSTRAINT fk_team_pokemon_team
        FOREIGN KEY (team_id) REFERENCES team (id) ON DELETE CASCADE,
    CONSTRAINT fk_team_pokemon_pokemon
        FOREIGN KEY (pokemon_id) REFERENCES pokemon (id) ON DELETE CASCADE
);
