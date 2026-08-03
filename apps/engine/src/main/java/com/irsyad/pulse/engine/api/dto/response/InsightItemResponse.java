package com.irsyad.pulse.engine.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsightItemResponse {
    private String checkoutId;
    private String customerId;
    private String decision;
    private String confidence;
    private String riskLevel;
    private String reason;
    private String processedAt;
    private String insightType;
}