package com.irsyad.pulse.product.infrastructure.persistence.document;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductDocumentJpaRepository extends JpaRepository<ProductDocumentJpaEntity, UUID> {
    List<ProductDocumentJpaEntity> findByProductVersionId(UUID productVersionId);
    void deleteByProductVersionId(UUID productVersionId);
}
