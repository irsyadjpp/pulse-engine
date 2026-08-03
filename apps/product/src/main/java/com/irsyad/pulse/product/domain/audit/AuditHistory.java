package com.irsyad.pulse.product.domain.audit;

import com.irsyad.pulse.product.domain.shared.AuditAction;
import com.irsyad.pulse.product.domain.shared.EntityName;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Append-only Audit Trail record (FSD_05 Section 13-17).
 *
 * <p>Audit Trail is not an Aggregate Root; it is an append-only record generated
 * by aggregate changes and does not have a business lifecycle of its own.
 */
@Getter
@Builder
public class AuditHistory {

    private final UUID auditId;
    private final EntityName entityName;
    private final UUID entityId;
    private final AuditAction action;
    private final Integer version;
    private String beforeData;
    private String afterData;
    private final String reason;
    private final UUID correlationId;
    private final String createdBy;
    private final Instant createdAt;
}
