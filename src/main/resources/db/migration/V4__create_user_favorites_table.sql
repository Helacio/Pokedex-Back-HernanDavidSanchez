-- ============================================================
-- V4__create_user_favorites_table.sql
-- Tabla de Pokémon favoritos por usuario (RF-11)
-- ============================================================

CREATE TABLE user_favorites (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    pokemon_id  BIGINT NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_user_favorites UNIQUE (user_id, pokemon_id),
    CONSTRAINT fk_fav_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_fav_pokemon
        FOREIGN KEY (pokemon_id) REFERENCES pokemon (id) ON DELETE CASCADE
);
