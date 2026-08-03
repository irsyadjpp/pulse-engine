package com.irsyad.pulse.orchestrator.process.handler;

import com.irsyad.pulse.orchestrator.domain.dto.CheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.PaymentAuthorization;
import com.irsyad.pulse.orchestrator.domain.dto.PaymentAuthorizationRequest;
import com.irsyad.pulse.orchestrator.domain.dto.PaymentAuthorizationResult;
import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
import com.irsyad.pulse.orchestrator.integration.payment.PaymentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Delegate for authorizing payment in the APPROVE path.
 */
@ApplicationScoped
public class AuthorizePaymentDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizePaymentDelegate.class);

    @Inject
    PaymentService paymentService;

    public CheckoutProcessModel execute(CheckoutProcessModel model) {
        CheckoutRequest request = model.getRequest();
        if (request == null) {
            throw new IllegalStateException("CheckoutRequest is null in process model");
        }

        LOG.info("Authorizing payment for order {}", request.getOrderId());

        PaymentAuthorizationRequest authRequest = new PaymentAuthorizationRequest();
        authRequest.setRequestId(model.getTraceId());
        authRequest.setTraceId(model.getTraceId());
        authRequest.setOrderId(request.getOrderId());
        authRequest.setCustomerId(request.getCustomerId());
        authRequest.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod().name() : "VA");
        authRequest.setPaymentReference(model.getCheckoutId());
        BigDecimal amount = request.getAmount() != null ? request.getAmount() : BigDecimal.ZERO;
        authRequest.setAmount(amount);
        authRequest.setMerchantId(request.getMerchantId());

        PaymentAuthorizationResult result = paymentService.authorize(authRequest);

        PaymentAuthorization payment = new PaymentAuthorization();
        payment.setAuthorized(result.getAuthorized());
        payment.setAuthorizationId(result.getAuthorizationId());
        payment.setPaymentStatus(result.getPaymentStatus());
        payment.setAuthorizedAt(result.getPaymentTime());
        payment.setGatewayReference(result.getGatewayReference());
        payment.setFailureCode(result.getFailureCode());
        payment.setFailureMessage(result.getFailureMessage());

        model.setPayment(payment);
        model.setPaymentAuthorized(result.getAuthorized());
        model.setPaymentFailed(!Boolean.TRUE.equals(result.getAuthorized()));
        return model;
    }
}
