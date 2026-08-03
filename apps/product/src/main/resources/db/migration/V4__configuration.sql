-- V4__configuration.sql
-- TSD_03 Section 10: product_version + child configuration tables.
-- Child entities (coverage, benefit, etc.) are bound to product_version_id per JPA entities.

CREATE TABLE catalog.product_version (
    product_version_id UUID PRIMARY KEY,
    product_id         UUID         NOT NULL,
    version            INTEGER      NOT NULL,
    status             VARCHAR(20)  NOT NULL,
    effective_date     DATE,
    published_date     TIMESTAMP,
    created_at         TIMESTAMP    NOT NULL,
    created_by         VARCHAR(100) NOT NULL,
    CONSTRAINT fk_product_version_product FOREIGN KEY (product_id) REFERENCES catalog.product (product_id) ON UPDATE RESTRICT,
    CONSTRAINT uk_product_version_product_version UNIQUE (product_id, version)
);

CREATE TABLE catalog.coverage (
    coverage_id        UUID PRIMARY KEY,
    product_version_id UUID         NOT NULL,
    coverage_amount    NUMERIC(19,4) NOT NULL,
    currency           VARCHAR(10)  NOT NULL,
    CONSTRAINT fk_coverage_product_version FOREIGN KEY (product_version_id) REFERENCES catalog.product_version (product_version_id) ON UPDATE RESTRICT
);

CREATE TABLE catalog.benefit (
    benefit_id         UUID PRIMARY KEY,
    product_version_id UUID         NOT NULL,
    benefit_name       VARCHAR(200) NOT NULL,
    description        TEXT,
    maximum_limit      NUMERIC(19,4),
    CONSTRAINT fk_benefit_product_version FOREIGN KEY (product_version_id) REFERENCES catalog.product_version (product_version_id) ON UPDATE RESTRICT
);

CREATE TABLE catalog.exclusion (
    exclusion_id       UUID PRIMARY KEY,
    product_version_id UUID NOT NULL,
    description        TEXT NOT NULL,
    CONSTRAINT fk_exclusion_product_version FOREIGN KEY (product_version_id) REFERENCES catalog.product_version (product_version_id) ON UPDATE RESTRICT
);

CREATE TABLE catalog.eligibility_configuration (
    eligibility_id      UUID PRIMARY KEY,
    product_version_id  UUID NOT NULL,
    minimum_age         INTEGER,
    maximum_age         INTEGER,
    occupation_class    VARCHAR(100),
    nationality         VARCHAR(100),
    residency           VARCHAR(100),
    CONSTRAINT fk_eligibility_product_version FOREIGN KEY (product_version_id) REFERENCES catalog.product_version (product_version_id) ON UPDATE RESTRICT
);

CREATE TABLE catalog.premium_configuration (
    premium_configuration_id UUID PRIMARY KEY,
    product_version_id       UUID         NOT NULL,
    coverage_band            VARCHAR(50),
    age_band                 VARCHAR(50),
    occupation_class         VARCHAR(100),
    base_premium             NUMERIC(19,4) NOT NULL,
    CONSTRAINT fk_premium_product_version FOREIGN KEY (product_version_id) REFERENCES catalog.product_version (product_version_id) ON UPDATE RESTRICT
);

CREATE TABLE catalog.product_document (
    document_id         UUID PRIMARY KEY,
    product_version_id  UUID         NOT NULL,
    document_name       VARCHAR(200) NOT NULL,
    document_type       VARCHAR(50),
    storage_reference   VARCHAR(500) NOT NULL,
    CONSTRAINT fk_document_product_version FOREIGN KEY (product_version_id) REFERENCES catalog.product_version (product_version_id) ON UPDATE RESTRICT
);
