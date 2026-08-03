package com.irsyad.pulse.product.application.query.company;

/**
 * Query for searching Insurance Companies (FSD_01 FR Search).
 */
public record SearchCompanyQuery(
        String keyword,
        int page,
        int size) {
}
