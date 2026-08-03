package com.irsyad.pulse.orchestrator.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutDecision {

    private String decision;

    private String reasonCode;

    private String reason;

}