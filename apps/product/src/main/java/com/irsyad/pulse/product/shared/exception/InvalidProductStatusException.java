package com.irsyad.pulse.product.shared.exception;

/**
 * Thrown when a Product operation is attempted on an invalid status.
 * Maps to HTTP 409 Conflict (TSD_05 Section 18).
 */
public class InvalidProductStatusException extends BusinessException {
    public InvalidProductStatusException(String message) {
        super(message);
    }
}