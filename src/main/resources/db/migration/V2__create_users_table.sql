-- ============================================================
-- V2__create_users_table.sql
-- Tabla users, correspondiente a la entidad JPA UserEntity
-- ============================================================
 
CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(50) NOT NULL UNIQUE,
    email           VARCHAR(150) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    role            VARCHAR(20) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_users_role CHECK (role IN ('GUEST', 'TRAINER', 'ADMIN'))
);
 