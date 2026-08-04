package com.irsyad.pulse.product.api.audit;

import com.irsyad.pulse.product.domain.shared.AuditAction;
import com.irsyad.pulse.product.domain.shared.EntityName;

import java.time.Instant;
import java.util.UUID;

public record AuditResponse(
        UUID auditId,
        EntityName entityName,
        UUID entityId,
        AuditAction action,
        Integer version,
        String beforeData,
        String afterData,
        String reason,
        UUID correlationId,
        String createdBy,
        Instant createdAt) {
}
