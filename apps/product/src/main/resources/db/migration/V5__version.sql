-- V5__version.sql
-- TSD_03 Section 14: product_version indexes.
CREATE INDEX idx_product_version_product_id ON catalog.product_version (product_id);
CREATE INDEX idx_product_version_version_number ON catalog.product_version (version);
