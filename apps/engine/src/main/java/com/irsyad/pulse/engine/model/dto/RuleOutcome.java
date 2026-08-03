package com.irsyad.pulse.engine.model.dto;

/**
 * Outcome of a specific rule evaluation.
 */
public record RuleOutcome(
        String ruleName,
        String outcome,
        String description
) {
}