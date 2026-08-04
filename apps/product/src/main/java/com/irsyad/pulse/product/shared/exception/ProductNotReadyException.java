package com.irsyad.pulse.product.shared.exception;

/**
 * Thrown when a Product is not ready for publishing.
 * Maps to HTTP 409 Conflict (TSD_05 Section 18).
 */
public class ProductNotReadyException extends BusinessException {
    public ProductNotReadyException(String message) {
        super(message);
    }
}