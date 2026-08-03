package com.irsyad.pulse.product.application.query.audit;

import java.util.UUID;

/**
 * Query for Audit History (FSD_05 Section 18).
 */
public record GetAuditHistoryQuery(
        UUID productId) {
}
