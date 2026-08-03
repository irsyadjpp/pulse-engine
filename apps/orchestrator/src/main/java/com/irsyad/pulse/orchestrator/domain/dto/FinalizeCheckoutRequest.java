package com.irsyad.pulse.orchestrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalizeCheckoutRequest {
    private String orderId;
    private String authorizationId;
    private String paymentStatus;
    private String decision;
    private Instant approvedAt;
}
