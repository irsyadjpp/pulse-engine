package com.irsyad.pulse.product.shared.exception;

/**
 * Base class for domain business exceptions.
 * Maps to HTTP 409 Conflict (TSD_05 Section 18).
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}