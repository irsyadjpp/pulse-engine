package com.irsyad.pulse.orchestrator.integration.review;

import com.irsyad.pulse.orchestrator.domain.dto.ReviewCaseRequest;
import com.irsyad.pulse.orchestrator.domain.dto.ReviewCaseResult;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Default implementation of ReviewService.
 * Creates a review case in the review queue system.
 */
@ApplicationScoped
public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Override
    public ReviewCaseResult createReviewCase(ReviewCaseRequest request) {
        LOG.info("Creating review case for order {}", request.getOrderId());

        ReviewCaseResult result = new ReviewCaseResult();
        result.setReviewId("REV-" + UUID.randomUUID());
        result.setStatus("OPEN");
        result.setAssignedQueue(request.getAssignedQueue() != null ? request.getAssignedQueue() : "DEFAULT_QUEUE");
        int sla = request.getSlaHours() != null ? request.getSlaHours() : 24;
        result.setSlaDueDate(Instant.now().plus(sla, ChronoUnit.HOURS));
        return result;
    }
}
