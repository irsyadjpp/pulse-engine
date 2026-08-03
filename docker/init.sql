-- Initialize PostgreSQL database for Pulse Engine
-- Runs automatically when the PostgreSQL container starts (POSTGRES_DB=pulse)

-- Orchestrator schema (Kogito persistence, Flyway history: flyway_orchestrator_history)
CREATE SCHEMA IF NOT EXISTS pulse;

-- Engine schema (Quarkus insight store, Flyway history: flyway_engine_history)
CREATE SCHEMA IF NOT EXISTS pulse_engine;

-- Grant permissions
GRANT ALL ON SCHEMA pulse TO postgres;
GRANT ALL ON ALL TABLES IN SCHEMA pulse TO postgres;
GRANT ALL ON ALL SEQUENCES IN SCHEMA pulse TO postgres;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA pulse TO postgres;

GRANT ALL ON SCHEMA pulse_engine TO postgres;
GRANT ALL ON ALL TABLES IN SCHEMA pulse_engine TO postgres;
GRANT ALL ON ALL SEQUENCES IN SCHEMA pulse_engine TO postgres;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA pulse_engine TO postgres;

-- Set default search path for postgres
ALTER USER postgres SET search_path TO pulse, pulse_engine, public;

-- Log initialization
DO $$
BEGIN
    RAISE NOTICE 'Pulse Engine database and schemas (pulse, pulse_engine) initialized successfully';
END $$;