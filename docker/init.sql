-- Initialize PostgreSQL database for Pulse Engine
-- Runs automatically when the PostgreSQL container starts (POSTGRES_DB=pulse)
-- Hibernate ORM (database.generation=update) creates tables at app startup,
-- so this script only creates the schemas.

-- Orchestrator schema (Kogito BPMN persistence)
CREATE SCHEMA IF NOT EXISTS orchestrator;

-- Engine schema (Quarkus insight store)
CREATE SCHEMA IF NOT EXISTS pulse_engine;

-- Grant permissions
GRANT ALL ON SCHEMA orchestrator TO postgres;
GRANT ALL ON ALL TABLES IN SCHEMA orchestrator TO postgres;
GRANT ALL ON ALL SEQUENCES IN SCHEMA orchestrator TO postgres;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA orchestrator TO postgres;

GRANT ALL ON SCHEMA pulse_engine TO postgres;
GRANT ALL ON ALL TABLES IN SCHEMA pulse_engine TO postgres;
GRANT ALL ON ALL SEQUENCES IN SCHEMA pulse_engine TO postgres;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA pulse_engine TO postgres;

-- Set default search path for postgres
ALTER USER postgres SET search_path TO orchestrator, pulse_engine, public;

-- Log initialization
DO $$
BEGIN
    RAISE NOTICE 'Pulse Engine database and schemas (orchestrator, pulse_engine) initialized successfully';
END $$;
