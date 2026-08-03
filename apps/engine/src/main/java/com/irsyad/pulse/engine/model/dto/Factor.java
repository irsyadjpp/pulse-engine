package com.irsyad.pulse.engine.model.dto;

/**
 * Individual factor contributing to the decision.
 */
public record Factor(
        String name,
        String impact,
        String value
) {
}