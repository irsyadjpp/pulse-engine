package com.irsyad.pulse.orchestrator.integration.checkout;

import com.irsyad.pulse.orchestrator.domain.dto.FinalizeCheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.FinalizeCheckoutResult;
import com.irsyad.pulse.orchestrator.domain.dto.RejectCheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.RejectCheckoutResult;

/**
 * Service for finalizing or rejecting a checkout.
 */
public interface CheckoutService {

    RejectCheckoutResult reject(RejectCheckoutRequest request);

    FinalizeCheckoutResult finalize(FinalizeCheckoutRequest request);
}
