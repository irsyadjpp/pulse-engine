package com.irsyad.pulse.product.api.company;

import com.irsyad.pulse.product.domain.shared.CompanyStatus;

import java.util.UUID;

/**
 * Company detail response (FSD_01 Section 16).
 */
public record CompanyResponse(
        UUID companyId,
        String companyCode,
        String companyName,
        String logoUrl,
        CompanyStatus status) {
}
