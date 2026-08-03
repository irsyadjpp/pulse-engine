package com.irsyad.pulse.product.application.mapper;

import com.irsyad.pulse.product.domain.company.Company;
import com.irsyad.pulse.product.domain.shared.CompanyStatus;

import java.time.Instant;
import java.util.UUID;

/**
 * Maps Company domain model to/from external representations.
 * Keeps mapping logic out of domain objects.
 */
public class CompanyMapper {

    public Company toDomain(UUID companyId, String companyCode, String companyName, String logoUrl,
                            String contactInformation, String status) {
        return Company.builder()
                .companyId(companyId)
                .companyCode(companyCode)
                .companyName(companyName)
                .logoUrl(logoUrl)
                .contactInformation(contactInformation)
                .status(CompanyStatus.valueOf(status))
                .createdAt(Instant.now())
                .createdBy("system")
                .updatedAt(Instant.now())
                .updatedBy("system")
                .version(0L)
                .deleted(false)
                .build();
    }
}
