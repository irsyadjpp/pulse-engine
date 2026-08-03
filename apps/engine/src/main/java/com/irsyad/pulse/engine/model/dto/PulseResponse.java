package com.irsyad.pulse.engine.model.dto;

import java.time.Instant;
import java.util.List;

/**
 * Pulse response DTO representing the complete decision with explanation.
 * This reflects the Pulse Engine's capability to not just decide, but explain and learn.
 */
public record PulseResponse(
        String orderId,
        String status,
        int confidence,
        String reason,
        Explanation explanation,
        List<InsightResponse> insights,
        List<LearnedPattern> learnedPatterns,
        Integer processingTimeMs,
        String engineVersion,
        Instant processedAt
) {
}