package com.irsyad.pulse.orchestrator.integration.payment;

import com.irsyad.pulse.orchestrator.domain.dto.PaymentAuthorizationRequest;
import com.irsyad.pulse.orchestrator.domain.dto.PaymentAuthorizationResult;

/**
 * Service for authorizing payment transactions.
 */
public interface PaymentService {

    PaymentAuthorizationResult authorize(PaymentAuthorizationRequest request);
}
