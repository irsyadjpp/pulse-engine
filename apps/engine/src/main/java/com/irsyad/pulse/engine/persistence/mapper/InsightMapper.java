package com.irsyad.pulse.engine.persistence.mapper;

import com.irsyad.pulse.engine.api.dto.response.InsightItemResponse;
import com.irsyad.pulse.engine.persistence.entity.CheckoutInsightEntity;

public class InsightMapper {

    public static InsightItemResponse toResponse(CheckoutInsightEntity entity) {
        return InsightItemResponse.builder()
                .checkoutId(entity.getCheckoutId())
                .customerId(entity.getCustomerId())
                .decision(entity.getDecision())
                .confidence(entity.getConfidence())
                .riskLevel(entity.getRiskLevel())
                .reason(entity.getDecision())
                .processedAt(entity.getProcessedAt().toString())
                .insightType(entity.getInsightType())
                .build();
    }
}