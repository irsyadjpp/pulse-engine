package com.irsyad.pulse.product.application.command.company;

import java.util.UUID;

/**
 * Command to update an Insurance Company profile (FSD_01 Section 7).
 * Company Code is not editable.
 */
public record UpdateCompanyCommand(
        UUID companyId,
        String companyName,
        String logoUrl,
        String contactInformation) {
}
