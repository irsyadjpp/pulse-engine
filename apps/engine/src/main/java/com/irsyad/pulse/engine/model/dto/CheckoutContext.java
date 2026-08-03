package com.irsyad.pulse.engine.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Context of a checkout validation containing the enriched request data
 * and validation metadata.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutContext {

    // Trace
    private String requestId;
    private String correlationId;

    // Merchant
    private String merchantId;
    private String channel;

    // Customer
    private String customerId;
    private String customerType;
    private String fullName;
    private String nik;
    private String email;
    private String phoneNumber;

    // Checkout
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;

    // Device
    private String ipAddress;
    private String deviceId;
    private String userAgent;

    // Metadata
    private Instant requestTime;
}