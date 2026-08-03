package com.irsyad.pulse.orchestrator.process.handler;

import com.irsyad.pulse.orchestrator.domain.dto.CheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.ReviewCase;
import com.irsyad.pulse.orchestrator.domain.dto.ReviewCaseRequest;
import com.irsyad.pulse.orchestrator.domain.dto.ReviewCaseResult;
import com.irsyad.pulse.orchestrator.domain.dto.RiskAssessment;
import com.irsyad.pulse.orchestrator.domain.enums.CheckoutStatus;
import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
import com.irsyad.pulse.orchestrator.integration.review.ReviewService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Delegate for creating a review case in the REVIEW path.
 * Non-blocking: creates review case and ends the BPMN process.
 */
@ApplicationScoped
public class CreateReviewCaseDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(CreateReviewCaseDelegate.class);

    @Inject
    ReviewService reviewService;

    public CheckoutProcessModel execute(CheckoutProcessModel model) {
        CheckoutRequest request = model.getRequest();
        RiskAssessment risk = model.getRisk();
        if (request == null) {
            throw new IllegalStateException("CheckoutRequest is null in process model");
        }

        LOG.info("Creating review case for order {}", request.getOrderId());

        ReviewCaseRequest reviewRequest = new ReviewCaseRequest();
        reviewRequest.setRequestId(model.getTraceId());
        reviewRequest.setOrderId(request.getOrderId());
        reviewRequest.setCustomerId(request.getCustomerId());
        reviewRequest.setReasonCode(risk != null ? risk.getReasonCode() : "MANUAL_REVIEW");
        reviewRequest.setRiskLevel(risk != null ? risk.getRiskLevel() : "MEDIUM");
        reviewRequest.setConfidenceScore(risk != null ? risk.getConfidenceScore() : null);
        reviewRequest.setAssignedQueue("RISK_REVIEW_QUEUE");
        reviewRequest.setSlaHours(24);
        reviewRequest.setRequestedSumInsured(request.getSumInsured());
        reviewRequest.setIdentityVerification(model.getIdentity());

        ReviewCaseResult result = reviewService.createReviewCase(reviewRequest);

        ReviewCase reviewCase = new ReviewCase();
        reviewCase.setReviewId(result.getReviewId());
        reviewCase.setStatus(result.getStatus());
        reviewCase.setAssignedQueue(result.getAssignedQueue());
        reviewCase.setSlaDueDate(result.getSlaDueDate());
        reviewCase.setReasonCode(reviewRequest.getReasonCode());
        reviewCase.setRiskLevel(reviewRequest.getRiskLevel());
        reviewCase.setConfidenceScore(reviewRequest.getConfidenceScore());
        reviewCase.setRequestedSumInsured(reviewRequest.getRequestedSumInsured());
        reviewCase.setIdentityVerification(reviewRequest.getIdentityVerification());

        model.setReviewCase(reviewCase);
        model.setCheckoutStatus(CheckoutStatus.PENDING_REVIEW);
        return model;
    }
}
