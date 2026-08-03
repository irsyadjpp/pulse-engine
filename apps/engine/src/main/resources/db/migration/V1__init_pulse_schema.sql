-- ============================================================================
-- Pulse Engine — Database Schema
-- Description: Read Model + Knowledge Store for Pulse Engine
-- Design: 4 main tables representing the 6 capabilities.
-- Note: This migration uses separate Flyway history table (flyway_engine_history)
-- to avoid conflicts with Orchestrator migrations in the same schema.
-- ============================================================================

CREATE SCHEMA IF NOT EXISTS pulse_engine;

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ============================================================================
-- 1. checkout_insight
-- Final insight result from Pulse Engine.
-- ============================================================================
CREATE TABLE pulse_engine.checkout_insight
(
    checkout_id          VARCHAR(100) PRIMARY KEY,
    process_id           VARCHAR(100) NOT NULL,
    event_id             VARCHAR(100) UNIQUE,  -- For idempotency check
    
    customer_id          VARCHAR(100) NOT NULL,
    order_id             VARCHAR(100) NOT NULL,

    decision             VARCHAR(20) NOT NULL,
    confidence           VARCHAR(20) NOT NULL,
    risk_level           VARCHAR(20) NOT NULL,

    explainability_score FLOAT,

    total_amount         NUMERIC(18,2),

    processed_at         TIMESTAMP NOT NULL,

    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_checkout_insight_customer
ON pulse_engine.checkout_insight(customer_id);

CREATE INDEX idx_checkout_insight_decision
ON pulse_engine.checkout_insight(decision);

CREATE INDEX idx_checkout_insight_processed
ON pulse_engine.checkout_insight(processed_at);

-- ============================================================================
-- 2. checkout_timeline
-- Tracks the execution of Engine capabilities for a checkout.
-- ============================================================================
CREATE TABLE pulse_engine.checkout_timeline
(
    id                  BIGSERIAL PRIMARY KEY,

    checkout_id         VARCHAR(100) NOT NULL,

    capability          VARCHAR(50) NOT NULL,

    status              VARCHAR(20) NOT NULL,

    message             VARCHAR(500),

    processing_time_ms  INTEGER,

    event_time          TIMESTAMP NOT NULL,

    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_timeline_checkout
ON pulse_engine.checkout_timeline(checkout_id);

CREATE INDEX idx_timeline_capability
ON pulse_engine.checkout_timeline(capability);

CREATE INDEX idx_timeline_event
ON pulse_engine.checkout_timeline(event_time);

-- ============================================================================
-- 3. checkout_explanation
-- Explains why an insight was generated.
-- ============================================================================
CREATE TABLE pulse_engine.checkout_explanation
(
    id                 BIGSERIAL PRIMARY KEY,

    checkout_id        VARCHAR(100) NOT NULL,

    explanation_type   VARCHAR(50) NOT NULL,

    explanation        TEXT NOT NULL,

    created_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_explanation_checkout
ON pulse_engine.checkout_explanation(checkout_id);

-- ============================================================================
-- 4. customer_learning
-- Knowledge learned by Engine from transaction history.
-- ============================================================================
CREATE TABLE pulse_engine.customer_learning
(
    customer_id              VARCHAR(100) PRIMARY KEY,

    purchase_count           INTEGER NOT NULL,

    successful_checkout      INTEGER NOT NULL,

    rejected_checkout        INTEGER NOT NULL,

    average_amount           NUMERIC(18,2),

    highest_amount           NUMERIC(18,2),

    preferred_payment_method VARCHAR(50),

    customer_segment         VARCHAR(30),

    last_checkout_time       TIMESTAMP,

    learning_version         INTEGER NOT NULL DEFAULT 1,

    created_at               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_learning_segment
ON pulse_engine.customer_learning(customer_segment);