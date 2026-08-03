package com.irsyad.pulse.orchestrator.integration.payment;

import com.irsyad.pulse.orchestrator.domain.dto.PaymentAuthorizationRequest;
import com.irsyad.pulse.orchestrator.domain.dto.PaymentAuthorizationResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;

/**
 * Default implementation of PaymentService.
 * Simulates payment gateway authorization - replace with real integration.
 */
@ApplicationScoped
public class PaymentServiceImpl implements PaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Override
    public PaymentAuthorizationResult authorize(PaymentAuthorizationRequest request) {
        LOG.info("Authorizing payment for order {}", request.getOrderId());

        PaymentAuthorizationResult result = new PaymentAuthorizationResult();
        result.setAuthorized(true);
        result.setAuthorizationId("AUTH-" + UUID.randomUUID());
        result.setPaymentStatus("AUTHORIZED");
        result.setPaymentTime(Instant.now());
        result.setGatewayReference("GW-" + UUID.randomUUID());
        return result;
    }
}
