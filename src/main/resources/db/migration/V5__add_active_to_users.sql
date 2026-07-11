-- ============================================================
-- V5__add_active_to_users.sql
-- Campo active en la tabla users para que el admin pueda
-- activar o desactivar perfiles (RF-18)
-- ============================================================

ALTER TABLE users ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;
