package com.irsyad.pulse.orchestrator.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class IdentityVerification {

    private String dukcapilStatus;

    private String kycStatus;

    private Boolean identityVerified;

    // Enrichment fields for DMN and audit
    private Integer age;
    private String occupationClass;
    private Integer confidenceScore;
    private BigDecimal existingActiveSumInsured;

    private String identityStatus;

    private String nik;
    private String fullName;
    private String dateOfBirth;
    private String occupation;

    private String reasonCode;
    private String message;

    private Instant verifiedAt;

}