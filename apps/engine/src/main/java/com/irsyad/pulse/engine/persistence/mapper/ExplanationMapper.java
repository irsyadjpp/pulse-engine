package com.irsyad.pulse.engine.persistence.mapper;

import com.irsyad.pulse.engine.api.dto.response.ExplanationItem;
import com.irsyad.pulse.engine.api.dto.response.ExplanationResponse;
import com.irsyad.pulse.engine.persistence.entity.CheckoutExplanationEntity;

import java.util.List;

public class ExplanationMapper {

    public static ExplanationResponse toResponse(String checkoutId, List<CheckoutExplanationEntity> entities) {
        ExplanationResponse response = ExplanationResponse.builder()
                .checkoutId(checkoutId)
                .build();

        if (entities != null && !entities.isEmpty()) {
            // Use first entity's explanation as reason
            String reason = entities.get(0).getExplanation();
            
            // Note: decision and confidence should come from insight entity
            // For now, leaving them null and let client infer from factors
            response.setReason(reason);

            response.setFactors(entities.stream()
                    .map(entity -> ExplanationItem.builder()
                            .factor(entity.getExplanationType())
                            .impact("POSITIVE")
                            .detail(entity.getExplanation())
                            .build())
                    .toList());
        }

        return response;
    }
}