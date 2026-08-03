package com.irsyad.pulse.product.api.company;

import jakarta.validation.constraints.NotBlank;

/**
 * Create Company request (FSD_01 Section 15).
 */
public record CreateCompanyRequest(
        @NotBlank String companyCode,
        @NotBlank String companyName,
        String logoUrl,
        String contactInformation) {
}
