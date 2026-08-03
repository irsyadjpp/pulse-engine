package com.irsyad.pulse.orchestrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCaseRequest {
    private String requestId;
    private String orderId;
    private String customerId;
    private String reasonCode;
    private String riskLevel;
    private Integer confidenceScore;
    private String assignedQueue;
    private Integer slaHours;
    private BigDecimal requestedSumInsured;
    private IdentityVerification identityVerification;
}
