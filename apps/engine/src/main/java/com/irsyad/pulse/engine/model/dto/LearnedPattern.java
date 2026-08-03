package com.irsyad.pulse.engine.model.dto;

/**
 * Pattern learned from historical decision data.
 */
public record LearnedPattern(
        String patternId,
        String description,
        double significance,
        String detectedAt
) {
}