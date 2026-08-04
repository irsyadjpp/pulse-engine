-- V2__company.sql
-- TSD_03 Section 10: insurance_company table.
CREATE TABLE catalog.insurance_company (
    id                  UUID PRIMARY KEY,
    company_code        VARCHAR(50)  NOT NULL,
    company_name        VARCHAR(200) NOT NULL,
    logo_url            TEXT,
    contact_information TEXT,
    status              VARCHAR(20)  NOT NULL,
    created_at          TIMESTAMP    NOT NULL,
    created_by          VARCHAR(100) NOT NULL,
    updated_at          TIMESTAMP    NOT NULL,
    updated_by          VARCHAR(100) NOT NULL,
    version             BIGINT       NOT NULL DEFAULT 0,
    deleted             BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT uk_insurance_company_company_code UNIQUE (company_code)
);

CREATE INDEX idx_insurance_company_status ON catalog.insurance_company (status);
