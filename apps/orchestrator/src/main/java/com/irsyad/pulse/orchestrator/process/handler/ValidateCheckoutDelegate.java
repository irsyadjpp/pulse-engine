package com.irsyad.pulse.orchestrator.process.handler;

import com.irsyad.pulse.orchestrator.domain.dto.CheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.CheckoutValidation;
import com.irsyad.pulse.orchestrator.domain.enums.ReasonCode;
import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
import com.irsyad.pulse.orchestrator.infrastructure.persistence.ProcessInstanceRepository;
import com.irsyad.pulse.orchestrator.integration.checkout.MerchantService;
import com.irsyad.pulse.orchestrator.integration.checkout.ProductService;
import com.irsyad.pulse.orchestrator.integration.redis.DistributedLockService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Delegate for validating checkout request before processing
 * This performs basic validation checks and initializes CheckoutProcessModel
 */
@ApplicationScoped
public class ValidateCheckoutDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(ValidateCheckoutDelegate.class);

    @Inject
    MerchantService merchantService;

    @Inject
    ProductService productService;

    @Inject
    DistributedLockService distributedLockService;

    @Inject
    ProcessInstanceRepository processInstanceRepository;

    public CheckoutProcessModel execute(CheckoutProcessModel model) {
        LOG.info("Validating checkout request for order {}", model.getCheckoutId());

        // Extract request from model
        CheckoutRequest request = model.getRequest();

        // If request is null, try to reconstruct from model fields
        if (request == null) {
            LOG.warn("CheckoutRequest is null in process model, reconstructing from model fields");
            request = new CheckoutRequest();
            request.setOrderId(model.getCheckoutId());
            request.setCustomerId(model.getCustomerId());
            request.setAmount(model.getPremiumAmount());
            request.setCurrency(model.getCurrency());
            request.setPaymentMethod(model.getPaymentMethod());
        }

        // Step 1: Validate mandatory fields
        validateMandatoryFields(request);

        // Step 2: Normalize data
        normalizeData(request);

        // Step 3: Check duplicate request
        validateDuplicate(request);

        // Step 4: Validate merchant
        boolean merchantValid = validateMerchant(request);

        // Step 5: Validate product
        boolean productValid = validateProduct(request);

        // Step 6: Acquire distributed lock
        String lockKey = acquireLock(request);

        // Step 7: Build validation result on model
        // Step 7: Build validation result on model
        CheckoutValidation validation = new CheckoutValidation();
        validation.setValid(true);
        validation.setValidatedAt(java.time.Instant.now());
        validation.setValidationCode(ReasonCode.SUCCESS.name());
        validation.setValidationMessage("Checkout validation passed");
        validation.setMerchantValid(merchantValid);
        validation.setDuplicate(false);
        validation.setLockKey(lockKey);
        model.setValidation(validation);

        LOG.info("Checkout validation passed for order {}", request.getOrderId());
        return model;
    }

    private void validateDuplicate(CheckoutRequest request) {
        LOG.info("Checking duplicate for merchantId: {} orderId: {}", request.getMerchantId(), request.getOrderId());

        // Check if a process already exists with the same business key (merchantId + orderId)
        String businessKey = request.getMerchantId() + ":" + request.getOrderId();
        var existing = processInstanceRepository.findByBusinessKey(businessKey);

        if (existing != null) {
            LOG.warn("Duplicate checkout detected for merchantId: {} orderId: {}", request.getMerchantId(), request.getOrderId());
            throw new IllegalStateException("Duplicate checkout request: merchant " + request.getMerchantId() + " order " + request.getOrderId() + " already exists");
        }

        LOG.debug("No duplicate found for merchantId: {} orderId: {}", request.getMerchantId(), request.getOrderId());
    }

    private boolean validateMerchant(CheckoutRequest request) {
        try {
            LOG.info("Validating merchant: {}", request.getMerchantId());
            MerchantService.MerchantInfo merchant = merchantService.getMerchant(request.getMerchantId());

            if (merchant == null) {
                throw new IllegalArgumentException("Merchant not found: " + request.getMerchantId());
            }

            if (!merchant.isActive()) {
                throw new IllegalArgumentException("Merchant is not active: " + request.getMerchantId());
            }

            if (!merchant.isAllowed()) {
                throw new IllegalArgumentException("Merchant is not allowed for checkout: " + request.getMerchantId());
            }

            LOG.info("Merchant validation passed: {}", request.getMerchantId());
            return true;

        } catch (Exception e) {
            LOG.error("Merchant validation failed: {}", e.getMessage());
            throw e;
        }
    }

    private boolean validateProduct(CheckoutRequest request) {
        try {
            LOG.info("Validating product: {}", request.getProductId());
            ProductService.ProductInfo product = productService.getProduct(request.getProductId());

            if (product == null) {
                throw new IllegalArgumentException("Product not found: " + request.getProductId());
            }

            if (!product.isActive()) {
                throw new IllegalArgumentException("Product is not active: " + request.getProductId());
            }

            if (!product.isAvailable()) {
                throw new IllegalArgumentException("Product is not available: " + request.getProductId());
            }

            LOG.info("Product validation passed: {}", request.getProductId());
            return true;

        } catch (Exception e) {
            LOG.error("Product validation failed: {}", e.getMessage());
            throw e;
        }
    }

    private String acquireLock(CheckoutRequest request) {
        try {
            String lockKey = "checkout:" + request.getCustomerId();
            boolean acquired = distributedLockService.acquireLock(lockKey, 300); // TTL 5 minutes

            if (!acquired) {
                throw new IllegalStateException("Another checkout is in progress for customer: " + request.getCustomerId());
            }

            LOG.info("Distributed lock acquired: {}", lockKey);
            return lockKey;

        } catch (Exception e) {
            LOG.error("Failed to acquire distributed lock: {}", e.getMessage());
            throw e;
        }
    }

    private void validateMandatoryFields(CheckoutRequest request) {
        if (request.getOrderId() == null || request.getOrderId().isBlank()) {
            throw new IllegalArgumentException("Order ID is required");
        }

        if (request.getCustomerId() == null || request.getCustomerId().isBlank()) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (request.getPaymentMethod() == null) {
            throw new IllegalArgumentException("Payment method is required");
        }

        if (request.getProductId() == null || request.getProductId().isBlank()) {
            throw new IllegalArgumentException("Product ID is required");
        }
    }

    private void normalizeData(CheckoutRequest request) {
        // Normalize currency to uppercase
        if (request.getCurrency() != null) {
            request.setCurrency(request.getCurrency().toUpperCase());
        }
    }
}