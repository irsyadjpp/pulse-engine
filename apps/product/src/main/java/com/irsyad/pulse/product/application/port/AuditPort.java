package com.irsyad.pulse.product.application.port;

import com.irsyad.pulse.product.domain.audit.AuditHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Audit History persistence (TSD_04 Section 11, FSD_05).
 */
public interface AuditPort {

    AuditHistory save(AuditHistory audit);

    List<AuditHistory> findByEntityId(UUID entityId);

    Optional<AuditHistory> findById(UUID auditId);
}
