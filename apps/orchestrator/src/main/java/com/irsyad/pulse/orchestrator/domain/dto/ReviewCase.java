package com.irsyad.pulse.orchestrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCase {
    private String reviewId;
    private String status;
    private String assignedQueue;
    private Instant slaDueDate;
    private String reasonCode;
    private String riskLevel;
    private Integer confidenceScore;
    private BigDecimal requestedSumInsured;
    private IdentityVerification identityVerification;
}
