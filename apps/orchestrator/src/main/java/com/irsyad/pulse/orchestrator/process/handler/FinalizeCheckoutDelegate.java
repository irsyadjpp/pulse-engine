package com.irsyad.pulse.orchestrator.process.handler;

import com.irsyad.pulse.orchestrator.domain.dto.CheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.FinalizeCheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.FinalizeCheckoutResult;
import com.irsyad.pulse.orchestrator.domain.dto.PaymentAuthorization;
import com.irsyad.pulse.orchestrator.domain.enums.CheckoutStatus;
import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
import com.irsyad.pulse.orchestrator.integration.checkout.CheckoutService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * Delegate for finalizing checkout after successful payment (APPROVE path).
 */
@ApplicationScoped
public class FinalizeCheckoutDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(FinalizeCheckoutDelegate.class);

    @Inject
    CheckoutService checkoutService;

    public CheckoutProcessModel execute(CheckoutProcessModel model) {
        CheckoutRequest request = model.getRequest();
        PaymentAuthorization payment = model.getPayment();
        if (request == null) {
            throw new IllegalStateException("CheckoutRequest is null in process model");
        }

        LOG.info("Finalizing checkout for order {}", request.getOrderId());

        FinalizeCheckoutRequest finalizeRequest = new FinalizeCheckoutRequest();
        finalizeRequest.setOrderId(request.getOrderId());
        finalizeRequest.setAuthorizationId(payment != null ? payment.getAuthorizationId() : null);
        finalizeRequest.setPaymentStatus(payment != null ? payment.getPaymentStatus() : "AUTHORIZED");
        finalizeRequest.setDecision("APPROVE");
        finalizeRequest.setApprovedAt(Instant.now());

        FinalizeCheckoutResult result = checkoutService.finalize(finalizeRequest);

        model.setCheckoutStatus(CheckoutStatus.COMPLETED);
        model.setCompletedAt(result.getCompletedAt());
        return model;
    }
}
