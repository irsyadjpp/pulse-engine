package com.irsyad.pulse.engine.model.dto;

import java.util.List;

/**
 * Explanation component providing human-readable decision reasoning.
 */
public record Explanation(
        List<Factor> factors,
        List<RuleOutcome> ruleOutcomes
) {
}