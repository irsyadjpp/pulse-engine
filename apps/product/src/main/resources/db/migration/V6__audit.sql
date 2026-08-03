-- V6__audit.sql
-- TSD_03 Section 10: audit_history table.
CREATE TABLE catalog.audit_history (
    audit_id       UUID PRIMARY KEY,
    entity_name    VARCHAR(50)  NOT NULL,
    entity_id      UUID         NOT NULL,
    action         VARCHAR(30)  NOT NULL,
    version        INTEGER,
    before_data    TEXT,
    after_data     TEXT,
    reason         VARCHAR(500),
    correlation_id UUID,
    created_by     VARCHAR(100) NOT NULL,
    created_at     TIMESTAMP    NOT NULL
);

CREATE INDEX idx_audit_history_entity_id ON catalog.audit_history (entity_id);
CREATE INDEX idx_audit_history_performed_at ON catalog.audit_history (created_at);
CREATE INDEX idx_audit_history_action ON catalog.audit_history (action);
