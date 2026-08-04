package com.irsyad.pulse.product.infrastructure.persistence.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditJpaRepository extends JpaRepository<AuditJpaEntity, UUID> {
    List<AuditJpaEntity> findByEntityIdOrderByPerformedAtDesc(UUID entityId);
}
