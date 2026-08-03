package com.irsyad.pulse.orchestrator.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DecisionResponse {
    private String decision;
    private String riskLevel;
    private String reason;
    private String dmnModel;
    private Instant executedAt;
}