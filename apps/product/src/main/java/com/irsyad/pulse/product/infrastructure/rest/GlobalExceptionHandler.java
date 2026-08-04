package com.irsyad.pulse.product.infrastructure.rest;

import com.irsyad.pulse.product.api.common.ErrorCode;
import com.irsyad.pulse.product.shared.exception.ProductCatalogException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler producing RFC 7807 / RFC 9457 Problem Details
 * (TSD_04 Section 27 Recommendation 1, TSD_10).
 *
 * <p>Every error response uses {@code application/problem+json} media type with
 * a stable {@code code} from {@link ErrorCode} and the request Correlation ID.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductCatalogException.class)
    public ProblemDetail handleProductCatalog(ProductCatalogException exception, HttpServletRequest request) {
        ErrorCode errorCode = exception.errorCode();
        ProblemDetail problem = this.problemDetail(errorCode, exception.getMessage(), request);
        return problem;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException exception, HttpServletRequest request) {
        return this.problemDetail(ErrorCode.VALIDATION_ERROR, exception.getMessage(), request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException exception, HttpServletRequest request) {
        return this.problemDetail(ErrorCode.INVALID_TRANSITION, exception.getMessage(), request);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail handleOptimisticLock(ObjectOptimisticLockingFailureException exception, HttpServletRequest request) {
        return this.problemDetail(ErrorCode.CONCURRENT_MODIFICATION,
                "Product has been modified by another user.", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, Object> errors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(),
                        fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid"));
        ProblemDetail problem = this.problemDetail(ErrorCode.VALIDATION_ERROR, "Validation failed.", request);
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception exception, HttpServletRequest request) {
        return this.problemDetail(ErrorCode.INTERNAL_SERVER_ERROR, "Internal server error.", request);
    }

    private ProblemDetail problemDetail(ErrorCode errorCode, String message, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(errorCode.status(), message);
        problem.setTitle(errorCode.status().getReasonPhrase());
        problem.setType(URI.create("about:blank"));
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("code", errorCode.code());
        problem.setProperty("timestamp", Instant.now().toString());
        problem.setProperty("correlationId", MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC));
        return problem;
    }
}