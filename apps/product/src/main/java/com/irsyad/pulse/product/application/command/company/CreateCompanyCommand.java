package com.irsyad.pulse.product.application.command.company;

import java.util.UUID;

/**
 * Command to create an Insurance Company (FSD_01 FR-01).
 * Immutable command object; no business logic here.
 */
public record CreateCompanyCommand(
        String companyCode,
        String companyName,
        String logoUrl,
        String contactInformation) {
}
