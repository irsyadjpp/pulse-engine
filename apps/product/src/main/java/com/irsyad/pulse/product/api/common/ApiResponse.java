package com.irsyad.pulse.product.api.common;

import java.time.Instant;
import java.util.List;

/**
 * Standard API response envelope (Appendix B).
 */
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final String code;
    private final String message;
    private final List<String> errors;
    private final Instant timestamp;

    public ApiResponse(boolean success, T data, String code, String message, List<String> errors, Instant timestamp) {
        this.success = success;
        this.data = data;
        this.code = code;
        this.message = message;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(true, data, null, null, null, Instant.now());
    }

    public boolean isSuccess() {
        return this.success;
    }

    public T getData() {
        return this.data;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }
}
