package com.irsyad.pulse.engine.model.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CheckoutRequest(

    // Trace
    String requestId,
    String correlationId,

    // Merchant
    String merchantId,
    String channel,

    // Customer
    String customerId,
    String customerType,
    String fullName,
    String nik,
    String email,
    String phoneNumber,

    // Checkout
    String orderId,
    BigDecimal amount,
    String currency,
    String paymentMethod,

    // Device
    String ipAddress,
    String deviceId,
    String userAgent,

    // Metadata
    Instant requestTime

) {}