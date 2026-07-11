-- ============================================================
-- V1__init_schema.sql
-- Esquema inicial: region, type, pokemon, pokemon_type, pokemon_stats
-- Corresponde a las entidades JPA: RegionEntity, TypeEntity,
-- PokemonEntity, PokemonStatsEntity
-- ============================================================

-- 1. REGION 
CREATE TABLE region (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE
);

-- 2. TYPE
CREATE TABLE type (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE
);

-- 3. POKEMON (depende de region)
CREATE TABLE pokemon (
    id                  BIGSERIAL PRIMARY KEY,
    national_number     INTEGER NOT NULL UNIQUE,
    name                VARCHAR(100) NOT NULL,
    region_id           BIGINT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pokemon_region
        FOREIGN KEY (region_id) REFERENCES region (id)
);

-- Índice explícito declarado en @Index de PokemonEntity
CREATE INDEX idx_pokemon_number ON pokemon (national_number);

-- 4. POKEMON_TYPE (tabla de unión ManyToMany: pokemon <-> type)
CREATE TABLE pokemon_type (
    pokemon_id  BIGINT NOT NULL,
    type_id     BIGINT NOT NULL,
    PRIMARY KEY (pokemon_id, type_id),
    CONSTRAINT fk_pokemon_type_pokemon
        FOREIGN KEY (pokemon_id) REFERENCES pokemon (id) ON DELETE CASCADE,
    CONSTRAINT fk_pokemon_type_type
        FOREIGN KEY (type_id) REFERENCES type (id) ON DELETE CASCADE
);

-- 5. POKEMON_STATS (OneToOne, dueño de la relación: pokemon_id es FK y UNIQUE)
CREATE TABLE pokemon_stats (
    id                  BIGSERIAL PRIMARY KEY,
    pokemon_id          BIGINT NOT NULL UNIQUE,
    hp                  INTEGER NOT NULL,
    attack              INTEGER NOT NULL,
    defense             INTEGER NOT NULL,
    special_attack      INTEGER NOT NULL,
    special_defense     INTEGER NOT NULL,
    speed               INTEGER NOT NULL,
    CONSTRAINT fk_pokemon_stats_pokemon
        FOREIGN KEY (pokemon_id) REFERENCES pokemon (id) ON DELETE CASCADE
);