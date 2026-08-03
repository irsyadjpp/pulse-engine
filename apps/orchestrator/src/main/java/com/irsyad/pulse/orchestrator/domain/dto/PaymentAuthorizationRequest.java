package com.irsyad.pulse.orchestrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAuthorizationRequest {
    private String requestId;
    private String traceId;
    private String orderId;
    private String customerId;
    private String paymentMethod;
    private String paymentReference;
    private BigDecimal amount;
    private String merchantId;
}
