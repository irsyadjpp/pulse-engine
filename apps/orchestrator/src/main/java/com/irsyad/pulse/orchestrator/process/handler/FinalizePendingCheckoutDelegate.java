package com.irsyad.pulse.orchestrator.process.handler;

import com.irsyad.pulse.orchestrator.domain.dto.CheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.enums.CheckoutStatus;
import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * Delegate for finalizing pending checkout in the REVIEW path.
 * Sets checkout status to PENDING_REVIEW. No payment involved.
 */
@ApplicationScoped
public class FinalizePendingCheckoutDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(FinalizePendingCheckoutDelegate.class);

    public CheckoutProcessModel execute(CheckoutProcessModel model) {
        CheckoutRequest request = model.getRequest();
        if (request == null) {
            throw new IllegalStateException("CheckoutRequest is null in process model");
        }

        LOG.info("Finalizing pending checkout for order {}", request.getOrderId());

        model.setCheckoutStatus(CheckoutStatus.PENDING_REVIEW);
        model.setCompletedAt(Instant.now());
        return model;
    }
}
