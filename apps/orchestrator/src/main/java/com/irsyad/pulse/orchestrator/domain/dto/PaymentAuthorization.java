package com.irsyad.pulse.orchestrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Payment authorization result from PaymentService.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAuthorization {

    private Boolean authorized;
    private String authorizationId;
    private String paymentStatus;
    private Instant authorizedAt;
    private String gatewayReference;
    private String failureCode;
    private String failureMessage;

}