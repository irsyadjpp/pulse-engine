-- V3__product.sql
-- TSD_03 Section 10: product table.
CREATE TABLE catalog.product (
    product_id              UUID PRIMARY KEY,
    company_id              UUID         NOT NULL,
    product_code            VARCHAR(50)  NOT NULL,
    product_name            VARCHAR(200) NOT NULL,
    category                VARCHAR(100),
    version                 INTEGER      NOT NULL,
    status                  VARCHAR(20)  NOT NULL,
    effective_date          DATE,
    expiry_date             DATE,
    created_at              TIMESTAMP    NOT NULL,
    created_by              VARCHAR(100) NOT NULL,
    updated_at              TIMESTAMP    NOT NULL,
    updated_by              VARCHAR(100) NOT NULL,
    optimistic_lock_version BIGINT       NOT NULL DEFAULT 0,
    deleted                 BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_product_company FOREIGN KEY (company_id) REFERENCES catalog.insurance_company (company_id) ON UPDATE RESTRICT,
    CONSTRAINT uk_product_company_code UNIQUE (company_id, product_code)
);

CREATE INDEX idx_product_company_id ON catalog.product (company_id);
CREATE INDEX idx_product_status ON catalog.product (status);
CREATE INDEX idx_product_current_version ON catalog.product (version);
