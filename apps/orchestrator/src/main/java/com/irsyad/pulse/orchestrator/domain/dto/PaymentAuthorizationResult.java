package com.irsyad.pulse.orchestrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAuthorizationResult {
    private Boolean authorized;
    private String authorizationId;
    private String paymentStatus;
    private Instant paymentTime;
    private String gatewayReference;
    private String failureCode;
    private String failureMessage;
}
