package com.irsyad.pulse.orchestrator.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Result of the Assess Risk DMN decision.
 * Contains the business decision (APPROVE/REVIEW/REJECT) and reason.
 */
@Getter
@Setter
public class RiskAssessment {

    private String decision;

    private String reasonCode;

    private String riskLevel;

    private Integer confidenceScore;

    private BigDecimal totalActiveSumInsured;

    private BigDecimal requestedSumInsured;

    private Instant evaluatedAt;

    private String dmnVersion;
}