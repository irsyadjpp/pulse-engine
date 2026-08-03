package com.irsyad.pulse.engine.persistence.mapper;

import com.irsyad.pulse.engine.api.dto.response.LearningMetric;
import com.irsyad.pulse.engine.api.dto.response.LearningPattern;
import com.irsyad.pulse.engine.api.dto.response.LearningResponse;
import com.irsyad.pulse.engine.persistence.entity.CustomerLearningEntity;

public class LearningMapper {

    public static LearningResponse toResponse(CustomerLearningEntity entity) {
        return LearningResponse.builder()
                .customerId(entity.getCustomerId())
                .segment(entity.getCustomerSegment())
                .patterns(java.util.List.of(
                        LearningPattern.builder()
                                .pattern("HIGH_FREQUENCY")
                                .description("Customer checks out more than 10 times per month")
                                .occurrences(entity.getPurchaseCount())
                                .build(),
                        LearningPattern.builder()
                                .pattern("LOW_RISK")
                                .description("Customer consistently approved with high confidence")
                                .occurrences(entity.getSuccessfulCheckout())
                                .build()
                ))
                .metrics(java.util.List.of(
                        LearningMetric.builder()
                                .name("avg_order_value")
                                .value(entity.getAverageAmount() != null ? entity.getAverageAmount().toString() : "0")
                                .build(),
                        LearningMetric.builder()
                                .name("approval_rate")
                                .value(entity.getPurchaseCount() > 0 ? String.format("%.1f%%", (entity.getSuccessfulCheckout() * 100.0 / entity.getPurchaseCount())) : "0%")
                                .build()
                ))
                .build();
    }
}
