package com.irsyad.pulse.orchestrator.domain.enums;

public enum ReasonCode {
    SUCCESS,
    VALIDATION_FAILED,
    IDENTITY_VERIFICATION_FAILED,
    HIGH_RISK,
    FRAUD_DETECTED,
    PAYMENT_FAILED,
    INSUFFICIENT_BALANCE,
    TIMEOUT,
    SYSTEM_ERROR,
    MANUAL_REVIEW
}