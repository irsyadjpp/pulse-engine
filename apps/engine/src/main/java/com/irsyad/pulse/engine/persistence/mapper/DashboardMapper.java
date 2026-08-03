package com.irsyad.pulse.engine.persistence.mapper;

import com.irsyad.pulse.engine.api.dto.response.DashboardMetric;
import com.irsyad.pulse.engine.api.dto.response.DashboardResponse;

public class DashboardMapper {

    public static DashboardResponse toResponse(long totalProcessed, long approved, long review, long rejected, double avgProcessingTimeMs) {
        return DashboardResponse.builder()
                .totalProcessed(totalProcessed)
                .approved(approved)
                .review(review)
                .rejected(rejected)
                .avgProcessingTimeMs(avgProcessingTimeMs)
                .metrics(java.util.List.of(
                        DashboardMetric.builder()
                                .name("checkout.processed")
                                .value(totalProcessed)
                                .build(),
                        DashboardMetric.builder()
                                .name("checkout.duplicate")
                                .value(0L)
                                .build()
                ))
                .build();
    }
}