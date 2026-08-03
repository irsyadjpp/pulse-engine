-- =============================================================================
-- Pulse Orchestrator — Schema Fixes (V2)
-- =============================================================================

-- Create sequences first before altering columns
DO $$
BEGIN
    CREATE SEQUENCE IF NOT EXISTS orchestrator.process_audit_log_id_seq OWNED BY orchestrator.process_audit_log.id;
EXCEPTION
    WHEN duplicate_object THEN NULL;
END $$;

DO $$
BEGIN
    CREATE SEQUENCE IF NOT EXISTS orchestrator.process_message_id_seq OWNED BY orchestrator.process_message.id;
EXCEPTION
    WHEN duplicate_object THEN NULL;
END $$;

-- Fix process_audit_log.id type from varchar(36) to bigint to match entity mapping
ALTER TABLE orchestrator.process_audit_log ALTER COLUMN id DROP DEFAULT;
ALTER TABLE orchestrator.process_audit_log ALTER COLUMN id TYPE BIGINT USING id::BIGINT;
ALTER TABLE orchestrator.process_audit_log ALTER COLUMN id SET NOT NULL;
ALTER TABLE orchestrator.process_audit_log ALTER COLUMN id SET DEFAULT nextval('orchestrator.process_audit_log_id_seq'::regclass);

-- Fix process_message.id type from varchar(36) to bigint to match entity mapping
ALTER TABLE orchestrator.process_message ALTER COLUMN id DROP DEFAULT;
ALTER TABLE orchestrator.process_message ALTER COLUMN id TYPE BIGINT USING id::BIGINT;
ALTER TABLE orchestrator.process_message ALTER COLUMN id SET NOT NULL;
ALTER TABLE orchestrator.process_message ALTER COLUMN id SET DEFAULT nextval('orchestrator.process_message_id_seq'::regclass);

-- Add event_id column to process_message if not exists (for idempotency)
ALTER TABLE orchestrator.process_message ADD COLUMN IF NOT EXISTS event_id VARCHAR(255);

-- Create index on event_id for idempotency lookups
CREATE INDEX IF NOT EXISTS idx_message_event_id ON orchestrator.process_message(event_id);
