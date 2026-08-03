package com.irsyad.pulse.orchestrator.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CheckoutValidation {

    private boolean valid;

    private Instant validatedAt;

    private String validationCode;

    private String validationMessage;

    // Extended validation fields
    private Boolean merchantValid;
    private Boolean duplicate;
    private String lockKey;

}