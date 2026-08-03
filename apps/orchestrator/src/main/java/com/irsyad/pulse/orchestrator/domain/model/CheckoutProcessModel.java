package com.irsyad.pulse.orchestrator.domain.model;

import com.irsyad.pulse.orchestrator.domain.dto.CheckoutValidation;
import com.irsyad.pulse.orchestrator.domain.dto.CheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.CheckoutDecision;
import com.irsyad.pulse.orchestrator.domain.dto.IdentityVerification;
import com.irsyad.pulse.orchestrator.domain.dto.PaymentAuthorization;
import com.irsyad.pulse.orchestrator.domain.dto.ReviewCase;
import com.irsyad.pulse.orchestrator.domain.dto.RiskAssessment;
import com.irsyad.pulse.orchestrator.domain.enums.CheckoutStatus;
import com.irsyad.pulse.orchestrator.domain.enums.CustomerType;
import com.irsyad.pulse.orchestrator.domain.enums.DukcapilStatus;
import com.irsyad.pulse.orchestrator.domain.enums.IdentityRisk;
import com.irsyad.pulse.orchestrator.domain.enums.KycStatus;
import com.irsyad.pulse.orchestrator.domain.enums.PaymentMethod;
import com.irsyad.pulse.orchestrator.domain.enums.ProcessStatus;
import com.irsyad.pulse.orchestrator.domain.enums.ReasonCode;
import com.irsyad.pulse.orchestrator.domain.enums.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Process model for Checkout BPMN process.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutProcessModel {

    // ==========================================================
    // Process Metadata
    // ==========================================================
    private String processId;
    private String businessKey;
    private String correlationId;
    private String traceId;

    // ==========================================================
    // Checkout Fields (Personal Accident Insurance)
    // ==========================================================
    private String checkoutId;
    private String customerId;
    private BigDecimal premiumAmount;
    private PaymentMethod paymentMethod;
    private String currency;
    private Integer coverageCount;
    private ProcessStatus status;
    private String errorMessage;
    private CheckoutStatus checkoutStatus;

    // ==========================================================
    // Customer Profile Fields (for DMN and delegates)
    // ==========================================================
    private CustomerType customerType;
    private Boolean vip;
    private Boolean isNewCustomer;
    private Integer accountAge;

    // ==========================================================
    // Identity Verification Fields (for DMN)
    // ==========================================================
    private KycStatus identityStatus;
    private DukcapilStatus dukcapilStatus;
    private KycStatus kycStatus;
    private String nik;
    private IdentityRisk identityRisk;

    // ==========================================================
    // Risk Assessment Fields (for DMN)
    // ==========================================================
    private RiskLevel transactionRisk;
    private RiskLevel overallRisk;
    private Integer velocityScore;
    private Integer fraudScore;
    private RiskLevel riskLevel;
    private ReasonCode reasonCode;
    private Integer processingTimeMs;
    private Boolean reviewRequired;

    // ==========================================================
    // Payment Fields (for delegates)
    // ==========================================================
    private Boolean paymentAuthorized;
    private String paymentTransactionId;

    // ==========================================================
    // Workflow Routing
    // ==========================================================
    private Boolean processApproved = true;
    private Boolean paymentFailed;
    private Boolean reservationReleased;

    // ==========================================================
    // Original Request
    // ==========================================================
    private CheckoutRequest request;

    // ==========================================================
    // Step 1 - Validate Checkout
    // ==========================================================
    private CheckoutValidation validation;

    // ==========================================================
    // Step 2 - Verify Identity
    // ==========================================================
    private IdentityVerification identity;

    // ==========================================================
    // Step 3 - Risk Assessment (DMN)
    // ==========================================================
    private RiskAssessment risk;

    // ==========================================================
    // Step 4 - Review Case (REVIEW path)
    // ==========================================================
    private ReviewCase reviewCase;

    // ==========================================================
    // Step 5 - Payment Authorization (APPROVE path)
    // ==========================================================
    private PaymentAuthorization payment;

    // ==========================================================
    // Step 6 - Publish Result
    // ==========================================================
    private Object publishResult;

    // ==========================================================
    // Final Decision
    // ==========================================================
    private CheckoutDecision decision;

    // ==========================================================
    // Audit
    // ==========================================================
    private Instant startedAt;
    private Instant completedAt;

}
