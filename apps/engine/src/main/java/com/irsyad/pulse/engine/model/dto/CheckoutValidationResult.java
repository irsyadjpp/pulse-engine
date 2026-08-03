package com.irsyad.pulse.engine.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result of a checkout validation containing the validation status,
 * code, message, process ID, and the enriched checkout context.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutValidationResult {

    private boolean valid;

    private String validationCode;

    private String validationMessage;

    private String processId;

    private CheckoutContext checkoutContext;
}