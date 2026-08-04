package com.irsyad.pulse.product.infrastructure.persistence.audit;

import com.irsyad.pulse.product.application.port.AuditPort;
import com.irsyad.pulse.product.domain.audit.AuditHistory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA adapter implementing the AuditPort (Hexagonal Architecture).
 * Append-only; never updates or deletes.
 */
@Component
public class AuditJpaAdapter implements AuditPort {

    private final AuditJpaRepository auditJpaRepository;

    public AuditJpaAdapter(AuditJpaRepository auditJpaRepository) {
        this.auditJpaRepository = auditJpaRepository;
    }

    @Override
    public AuditHistory save(AuditHistory audit) {
        AuditJpaEntity saved = this.auditJpaRepository.save(this.toEntity(audit));
        return this.toDomain(saved);
    }

    @Override
    public List<AuditHistory> findByEntityId(UUID entityId) {
        return this.auditJpaRepository.findByEntityIdOrderByPerformedAtDesc(entityId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<AuditHistory> findById(UUID auditId) {
        return this.auditJpaRepository.findById(auditId).map(this::toDomain);
    }

    private AuditJpaEntity toEntity(AuditHistory audit) {
        return AuditJpaEntity.builder()
                .id(audit.getAuditId())
                .entityType(audit.getEntityName())
                .entityId(audit.getEntityId())
                .action(audit.getAction())
                .version(audit.getVersion())
                .beforeData(audit.getBeforeData())
                .afterData(audit.getAfterData())
                .reason(audit.getReason())
                .correlationId(audit.getCorrelationId())
                .performedBy(audit.getCreatedBy())
                .performedAt(audit.getCreatedAt())
                .build();
    }

    private AuditHistory toDomain(AuditJpaEntity entity) {
        return AuditHistory.builder()
                .auditId(entity.getId())
                .entityName(entity.getEntityType())
                .entityId(entity.getEntityId())
                .action(entity.getAction())
                .version(entity.getVersion())
                .beforeData(entity.getBeforeData())
                .afterData(entity.getAfterData())
                .reason(entity.getReason())
                .correlationId(entity.getCorrelationId())
                .createdBy(entity.getPerformedBy())
                .createdAt(entity.getPerformedAt())
                .build();
    }
}
