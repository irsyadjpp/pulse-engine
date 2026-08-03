-- =============================================================================
-- Pulse Orchestrator — Database Schema (Kogito Aligned)
-- =============================================================================

-- Create process instances table in pulse schema
-- Note: This migration is designed to work alongside Engine migrations
-- and uses separate Flyway history table configuration

CREATE TABLE IF NOT EXISTS orchestrator.process_instance (
    process_id VARCHAR(255) PRIMARY KEY,
    business_key VARCHAR(255),
    customer_id VARCHAR(255),
    order_id VARCHAR(255),
    payment_id VARCHAR(255),
    inventory_reservation_id VARCHAR(255),
    kyc_reference VARCHAR(255),
    payment_method VARCHAR(50),
    total_amount DECIMAL(19,2),
    currency VARCHAR(3) DEFAULT 'IDR',
    status VARCHAR(50),
    workflow_status VARCHAR(50),
    current_node VARCHAR(255),
    current_step VARCHAR(255),
    retry_count INTEGER DEFAULT 0,
    correlation_id VARCHAR(255),
    idempotency_key VARCHAR(255),
    decision VARCHAR(50),
    decision_reason VARCHAR(255),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on status for efficient querying
CREATE INDEX IF NOT EXISTS idx_process_status ON orchestrator.process_instance(status);

-- Create index on business_key for BPMN lookups
CREATE INDEX IF NOT EXISTS idx_process_business_key ON orchestrator.process_instance(business_key);

-- Create index on order_id for lookups
CREATE INDEX IF NOT EXISTS idx_process_order ON orchestrator.process_instance(order_id);

-- Create index on customer_id for customer-related queries
CREATE INDEX IF NOT EXISTS idx_process_customer ON orchestrator.process_instance(customer_id);

-- Create index on order_id for order lookups
CREATE INDEX IF NOT EXISTS idx_process_order_id ON orchestrator.process_instance(order_id);

-- Create index on payment_method for payment filtering
CREATE INDEX IF NOT EXISTS idx_process_payment_method ON orchestrator.process_instance(payment_method);

-- Create index on workflow_status for workflow filtering
CREATE INDEX IF NOT EXISTS idx_process_workflow_status ON orchestrator.process_instance(workflow_status);

-- Create index on correlation_id for distributed tracing
CREATE INDEX IF NOT EXISTS idx_process_correlation ON orchestrator.process_instance(correlation_id);

-- Create index on idempotency_key for duplicate prevention
CREATE INDEX IF NOT EXISTS idx_process_idempotency ON orchestrator.process_instance(idempotency_key);

-- Create table for process audit logs in pulse schema
CREATE TABLE IF NOT EXISTS orchestrator.process_audit_log (
    id SERIAL PRIMARY KEY,
    process_id VARCHAR(255) NOT NULL,
    node_name VARCHAR(255),
    event_type VARCHAR(100) NOT NULL,
    correlation_id VARCHAR(255),
    event_data TEXT,
    event_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (process_id) REFERENCES orchestrator.process_instance(process_id) ON DELETE CASCADE
);

-- Create index on process_id for audit log queries
CREATE INDEX IF NOT EXISTS idx_audit_process ON orchestrator.process_audit_log(process_id);

-- Create index on correlation_id for distributed tracing
CREATE INDEX IF NOT EXISTS idx_audit_correlation ON orchestrator.process_audit_log(correlation_id);

-- Create process_message table for Kafka event tracking
CREATE TABLE IF NOT EXISTS orchestrator.process_message (
    id SERIAL PRIMARY KEY,
    process_id VARCHAR(255) NOT NULL,
    event_id VARCHAR(255),
    topic VARCHAR(255) NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    payload TEXT,
    status VARCHAR(50) NOT NULL,
    retry_count INTEGER DEFAULT 0,
    published_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (process_id) REFERENCES orchestrator.process_instance(process_id) ON DELETE CASCADE
);

-- Create index on process_id for message queries
CREATE INDEX IF NOT EXISTS idx_message_process ON orchestrator.process_message(process_id);

-- Create index on status for message status tracking
CREATE INDEX IF NOT EXISTS idx_message_status ON orchestrator.process_message(status);

-- Create index on published_at for message delivery tracking
CREATE INDEX IF NOT EXISTS idx_message_published ON orchestrator.process_message(published_at);
