package com.irsyad.pulse.orchestrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectCheckoutRequest {
    private String orderId;
    private String customerId;
    private String reasonCode;
    private String riskLevel;
}
