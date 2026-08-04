-- V7__align_tsd03.sql
-- Aligns the schema with TSD_03_DATABASE.md (Section 10, 12, 13, 14, 18, 24, 29.1).
--
-- TSD_03 Section 19: migrations are immutable. This migration ALTERs the existing
-- tables to align with TSD_03 while preserving the ProductVersion aggregate design
-- from TSD_02 (child entities remain bound to product_version_id for immutable
-- versioning, and product_id is added to satisfy the TSD_03 relationship).

-- ============================================================
-- GAP 1: Add product_id FK on child entities (TSD_03 Section 12)
--        Preserve product_version_id for TSD_02 immutable versioning.
-- ============================================================

ALTER TABLE catalog.coverage
    ADD COLUMN product_id UUID,
    ADD CONSTRAINT fk_coverage_product FOREIGN KEY (product_id) REFERENCES catalog.product (id) ON UPDATE RESTRICT;

ALTER TABLE catalog.benefit
    ADD COLUMN product_id UUID,
    ADD CONSTRAINT fk_benefit_product FOREIGN KEY (product_id) REFERENCES catalog.product (id) ON UPDATE RESTRICT;

ALTER TABLE catalog.exclusion
    ADD COLUMN product_id UUID,
    ADD CONSTRAINT fk_exclusion_product FOREIGN KEY (product_id) REFERENCES catalog.product (id) ON UPDATE RESTRICT;

ALTER TABLE catalog.eligibility_configuration
    ADD COLUMN product_id UUID,
    ADD CONSTRAINT fk_eligibility_product FOREIGN KEY (product_id) REFERENCES catalog.product (id) ON UPDATE RESTRICT;

ALTER TABLE catalog.premium_configuration
    ADD COLUMN product_id UUID,
    ADD CONSTRAINT fk_premium_product FOREIGN KEY (product_id) REFERENCES catalog.product (id) ON UPDATE RESTRICT;

ALTER TABLE catalog.product_document
    ADD COLUMN product_id UUID,
    ADD CONSTRAINT fk_document_product FOREIGN KEY (product_id) REFERENCES catalog.product (id) ON UPDATE RESTRICT;

-- ============================================================
-- GAP 2: Add TSD_03 columns on child entities (Section 10)
-- ============================================================

-- coverage: add coverage_code, coverage_name (keep coverage_amount/currency as extension)
ALTER TABLE catalog.coverage
    ADD COLUMN coverage_code VARCHAR(50),
    ADD COLUMN coverage_name VARCHAR(200);

-- benefit: add benefit_code (keep benefit_name as extension)
ALTER TABLE catalog.benefit
    ADD COLUMN benefit_code VARCHAR(50);

-- ============================================================
-- GAP 3: JSONB configuration_json on eligibility & premium (Section 18)
-- ============================================================

ALTER TABLE catalog.eligibility_configuration
    ADD COLUMN configuration_json JSONB;

ALTER TABLE catalog.premium_configuration
    ADD COLUMN configuration_json JSONB;

-- ============================================================
-- GAP 4: Align audit_history with TSD_03 Section 10 / 30.4
-- ============================================================

-- Rename columns to match TSD_03
ALTER TABLE catalog.audit_history RENAME COLUMN audit_id TO id;
ALTER TABLE catalog.audit_history RENAME COLUMN entity_name TO entity_type;
ALTER TABLE catalog.audit_history RENAME COLUMN created_by TO performed_by;
ALTER TABLE catalog.audit_history RENAME COLUMN created_at TO performed_at;

-- NOTE: before_data / after_data remain TEXT (encrypted via AttributeEncryptor).
-- TSD_03 Section 30.3 requires encryption of sensitive audit fields; converting
-- to JSONB would break the existing AES/GCM encryption. Encryption takes priority
-- over the JSONB recommendation in Section 18 for audit fields.

-- ============================================================
-- GAP 5: Complete product_document metadata (TSD_03 Section 29.1)
-- ============================================================

ALTER TABLE catalog.product_document
    ADD COLUMN mime_type VARCHAR(100),
    ADD COLUMN storage_key VARCHAR(1024),
    ADD COLUMN checksum VARCHAR(128),
    ADD COLUMN file_size BIGINT,
    ADD COLUMN description VARCHAR(1000);

-- ============================================================
-- GAP 6: Naming convention & product_version column (Section 24)
-- ============================================================

-- Rename product_version.version -> version_number (TSD_03 Section 10)
ALTER TABLE catalog.product_version RENAME COLUMN version TO version_number;

-- ============================================================
-- GAP 7: Complete index strategy (Section 14)
-- ============================================================

-- product: index on product_code and current_version
CREATE INDEX IF NOT EXISTS idx_product_product_code ON catalog.product (product_code);
CREATE INDEX IF NOT EXISTS idx_product_current_version ON catalog.product (current_version);

-- product_version: index on version_number
CREATE INDEX IF NOT EXISTS idx_product_version_version_number ON catalog.product_version (version_number);

-- audit_history: index on performed_at (renamed column)
CREATE INDEX IF NOT EXISTS idx_audit_history_performed_at ON catalog.audit_history (performed_at);

-- child entities: index on product_id (new FK)
CREATE INDEX IF NOT EXISTS idx_coverage_product_id ON catalog.coverage (product_id);
CREATE INDEX IF NOT EXISTS idx_benefit_product_id ON catalog.benefit (product_id);
CREATE INDEX IF NOT EXISTS idx_exclusion_product_id ON catalog.exclusion (product_id);
CREATE INDEX IF NOT EXISTS idx_eligibility_product_id ON catalog.eligibility_configuration (product_id);
CREATE INDEX IF NOT EXISTS idx_premium_product_id ON catalog.premium_configuration (product_id);
CREATE INDEX IF NOT EXISTS idx_document_product_id ON catalog.product_document (product_id);