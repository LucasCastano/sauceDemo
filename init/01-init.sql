-- PostgreSQL initialization script for Healenium
-- Creates the healenium schema required by Liquibase migrations.

-- Create the healenium schema
CREATE SCHEMA IF NOT EXISTS healenium AUTHORIZATION healenium_user;

-- Ensure healenium_user has full access to both schemas
GRANT ALL ON SCHEMA public TO healenium_user;
GRANT ALL ON SCHEMA healenium TO healenium_user;

ALTER SCHEMA public OWNER TO healenium_user;
ALTER SCHEMA healenium OWNER TO healenium_user;

-- Set default search_path
ALTER USER healenium_user SET search_path TO healenium, public;
ALTER DATABASE healenium SET search_path TO healenium, public;
