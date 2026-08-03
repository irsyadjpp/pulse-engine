package com.irsyad.pulse.product.api.company;

import jakarta.validation.constraints.NotBlank;

/**
 * Update Company request (FSD_01 Section 7).
 * Company Code is immutable and not part of this request.
 */
public record UpdateCompanyRequest(
        @NotBlank String companyName,
        String logoUrl,
        String contactInformation) {
}
