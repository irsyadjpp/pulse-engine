package com.irsyad.pulse.product.infrastructure.persistence.audit;

import com.irsyad.pulse.product.domain.shared.AuditAction;
import com.irsyad.pulse.product.domain.shared.EntityName;
import com.irsyad.pulse.product.infrastructure.security.AttributeEncryptor;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity mapping the audit_history table (Appendix O).
 * Append-only record; must never be updated or deleted.
 */
@Entity
@Table(name = "audit_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false, length = 50)
    private EntityName entityType;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 50)
    private AuditAction action;

    @Column(name = "version")
    private Integer version;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "before_data", columnDefinition = "TEXT")
    private String beforeData;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "after_data", columnDefinition = "TEXT")
    private String afterData;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "correlation_id")
    private UUID correlationId;

    @Column(name = "performed_by", nullable = false, length = 100)
    private String performedBy;

    @Column(name = "performed_at", nullable = false, updatable = false)
    private Instant performedAt;
}