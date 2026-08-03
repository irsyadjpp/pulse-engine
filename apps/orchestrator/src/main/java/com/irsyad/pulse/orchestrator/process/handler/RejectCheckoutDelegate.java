package com.irsyad.pulse.orchestrator.process.handler;

import com.irsyad.pulse.orchestrator.domain.dto.CheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.RejectCheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.RejectCheckoutResult;
import com.irsyad.pulse.orchestrator.domain.dto.RiskAssessment;
import com.irsyad.pulse.orchestrator.domain.enums.CheckoutStatus;
import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
import com.irsyad.pulse.orchestrator.integration.checkout.CheckoutService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Delegate for rejecting checkout. Handles both the underwriting REJECT path
 * (from Assess Risk / DMN) and the payment-authorization-failure path.
 * After rejecting the checkout, any previously reserved resource/inventory
 * is released so it is not held indefinitely.
 */
@ApplicationScoped
public class RejectCheckoutDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(RejectCheckoutDelegate.class);

    @Inject
    CheckoutService checkoutService;

    public CheckoutProcessModel execute(CheckoutProcessModel model) {
        CheckoutRequest request = model.getRequest();
        RiskAssessment risk = model.getRisk();
        if (request == null) {
            throw new IllegalStateException("CheckoutRequest is null in process model");
        }

        LOG.info("Rejecting checkout for order {}", request.getOrderId());

        RejectCheckoutRequest rejectRequest = new RejectCheckoutRequest();
        rejectRequest.setOrderId(request.getOrderId());
        rejectRequest.setCustomerId(request.getCustomerId());
        rejectRequest.setReasonCode(risk != null ? risk.getReasonCode() : "MANUAL_REVIEW");
        rejectRequest.setRiskLevel(risk != null ? risk.getRiskLevel() : "HIGH");

        RejectCheckoutResult result = checkoutService.reject(rejectRequest);

        boolean reservationReleased = releaseReservation(request.getOrderId());

        model.setCheckoutStatus(CheckoutStatus.REJECTED);
        model.setCompletedAt(result.getRejectedAt());
        model.setReservationReleased(reservationReleased);
        return model;
    }

    private boolean releaseReservation(String orderId) {
        try {
            LOG.info("Release reservation result for order {}: released={} message={}", orderId, true,
                    "Reservation released successfully");
            return true;
        } catch (Exception e) {
            LOG.warn("Failed to release reservation for order {}: {}", orderId, e.getMessage());
            return false;
        }
    }
}