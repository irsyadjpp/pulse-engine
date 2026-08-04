package com.irsyad.pulse.product.application.query.company;

import com.irsyad.pulse.product.domain.shared.CompanyStatus;

/**
 * Query for searching Insurance Companies (FSD_01 FR Search, TSD_04 Section 13).
 */
public record SearchCompanyQuery(
        String keyword,
        CompanyStatus status,
        String sort,
        int page,
        int size) {
}