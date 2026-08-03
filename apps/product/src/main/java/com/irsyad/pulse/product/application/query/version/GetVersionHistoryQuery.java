package com.irsyad.pulse.product.application.query.version;

import java.util.UUID;

/**
 * Query for Product Version History (FSD_04 FR-04-04).
 */
public record GetVersionHistoryQuery(
        UUID productId) {
}
