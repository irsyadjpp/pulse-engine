package com.irsyad.pulse.orchestrator.integration.review;

import com.irsyad.pulse.orchestrator.domain.dto.ReviewCaseRequest;
import com.irsyad.pulse.orchestrator.domain.dto.ReviewCaseResult;

/**
 * Service for creating manual review cases.
 */
public interface ReviewService {

    ReviewCaseResult createReviewCase(ReviewCaseRequest request);
}
