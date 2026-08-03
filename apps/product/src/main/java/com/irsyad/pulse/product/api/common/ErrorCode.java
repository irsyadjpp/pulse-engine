package com.irsyad.pulse.product.api.common;

import org.springframework.http.HttpStatus;

/**
 * Standard business error codes (TSD_04 Section 18).
 *
 * <p>Each code maps to an HTTP status and a stable machine-readable identifier
 * used in RFC 7807 Problem Details responses (TSD_04 Section 27).
 */
public enum ErrorCode {

    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND),
    VERSION_NOT_FOUND(HttpStatus.NOT_FOUND),
    PRODUCT_NOT_READY(HttpStatus.CONFLICT),
    INVALID_STATUS(HttpStatus.CONFLICT),
    INVALID_TRANSITION(HttpStatus.CONFLICT),
    DUPLICATE_PRODUCT_CODE(HttpStatus.CONFLICT),
    DUPLICATE_COMPANY_CODE(HttpStatus.CONFLICT),
    CONCURRENT_MODIFICATION(HttpStatus.CONFLICT),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus status() {
        return this.status;
    }

    public String code() {
        return this.name();
    }
}