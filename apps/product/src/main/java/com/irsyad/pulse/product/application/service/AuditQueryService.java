package com.irsyad.pulse.product.application.service;

import com.irsyad.pulse.product.application.port.AuditPort;
import com.irsyad.pulse.product.domain.audit.AuditHistory;
import com.irsyad.pulse.product.shared.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Query service for Audit History (TSD_04 Section 11, FSD_05).
 */
@Service
public class AuditQueryService {

    private final AuditPort auditPort;

    public AuditQueryService(AuditPort auditPort) {
        this.auditPort = auditPort;
    }

    @Transactional(readOnly = true)
    public List<AuditHistory> productAudit(UUID productId) {
        return this.auditPort.findByEntityId(productId);
    }

    @Transactional(readOnly = true)
    public AuditHistory auditDetail(UUID auditId) {
        return this.auditPort.findById(auditId)
                .orElseThrow(() -> new ProductNotFoundException("Audit not found."));
    }
}
