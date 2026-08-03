package com.irsyad.pulse.orchestrator.domain.dto;

import com.irsyad.pulse.orchestrator.domain.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class CheckoutRequest {

    // Correlation
    private String requestId;
    private String traceId;

    // Customer
    private String customerId;

    // Identity (from client input)
    private String nik;
    private String fullName;
    private String dateOfBirth;
    private String occupation;

    // Checkout
    private String merchantId;
    private String orderId;
    private BigDecimal amount;
    private BigDecimal sumInsured;
    private String currency;

    // Premium
    private PaymentMethod paymentMethod;

    // Product
    private String productId;

    // Device
    private String ipAddress;
    private String deviceId;

    // Optional metadata
    private String channel;
    private String userAgent;
    private String metadata;
}
