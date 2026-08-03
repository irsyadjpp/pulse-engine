package com.irsyad.pulse.orchestrator.integration.checkout;

import com.irsyad.pulse.orchestrator.domain.dto.FinalizeCheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.FinalizeCheckoutResult;
import com.irsyad.pulse.orchestrator.domain.dto.RejectCheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.RejectCheckoutResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

/**
 * Default implementation of CheckoutService.
 * Simulates finalize/reject operations - replace with real integration.
 */
@ApplicationScoped
public class CheckoutServiceImpl implements CheckoutService {

    private static final Logger LOG = LoggerFactory.getLogger(CheckoutServiceImpl.class);

    @Override
    public RejectCheckoutResult reject(RejectCheckoutRequest request) {
        LOG.info("Rejecting checkout for order {} reason {}", request.getOrderId(), request.getReasonCode());

        RejectCheckoutResult result = new RejectCheckoutResult();
        result.setStatus("REJECTED");
        result.setReasonCode(request.getReasonCode());
        result.setRejectedAt(Instant.now());
        return result;
    }

    @Override
    public FinalizeCheckoutResult finalize(FinalizeCheckoutRequest request) {
        LOG.info("Finalizing checkout for order {}", request.getOrderId());

        FinalizeCheckoutResult result = new FinalizeCheckoutResult();
        result.setCheckoutId(request.getOrderId());
        result.setStatus("COMPLETED");
        result.setCompletedAt(Instant.now());
        return result;
    }
}
