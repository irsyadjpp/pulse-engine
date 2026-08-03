package com.irsyad.pulse.engine.pipeline;

public class UnderstandingContext {
    private boolean isVIP;
    private boolean isFirstPurchase;
    private boolean isWeekend;
    private boolean highAmount;
    private boolean trustedCustomer;
    private boolean highRisk;
    private String customerSegment;
    private String paymentCategory;
    private String recommendedDecisionConfidence;
    
    // New DRG-specific fields
    private String identityStatus; // MATCH, NOT_MATCH, NOT_FOUND
    private String dukcapilStatus; // VALID, INVALID (Dukcapil verification)
    private String kycStatus; // PASSED, REVIEW, FAILED
    private String identityRisk; // LOW, MEDIUM, HIGH
    private String transactionRisk; // LOW, MEDIUM, HIGH
    private String overallRisk; // LOW, MEDIUM, HIGH
    private Integer velocityRisk; // numeric score (0-100)
    private Integer fraudScore; // numeric score (0-100)

    public UnderstandingContext() {}

    public UnderstandingContext(boolean isVIP, boolean isFirstPurchase, boolean isWeekend, boolean highAmount, 
                                boolean trustedCustomer, boolean highRisk, String customerSegment, 
                                String paymentCategory, String recommendedDecisionConfidence,
                                String identityStatus, String dukcapilStatus, String identityRisk,
                                String transactionRisk, String overallRisk,
                                Integer velocityRisk, Integer fraudScore) {
        this.isVIP = isVIP;
        this.isFirstPurchase = isFirstPurchase;
        this.isWeekend = isWeekend;
        this.highAmount = highAmount;
        this.trustedCustomer = trustedCustomer;
        this.highRisk = highRisk;
        this.customerSegment = customerSegment;
        this.paymentCategory = paymentCategory;
        this.recommendedDecisionConfidence = recommendedDecisionConfidence;
        this.identityStatus = identityStatus;
        this.dukcapilStatus = dukcapilStatus;
        this.identityRisk = identityRisk;
        this.transactionRisk = transactionRisk;
        this.overallRisk = overallRisk;
        this.velocityRisk = velocityRisk;
        this.fraudScore = fraudScore;
    }

    public boolean isVIP() { return isVIP; }
    public boolean isFirstPurchase() { return isFirstPurchase; }
    public boolean isWeekend() { return isWeekend; }
    public boolean isHighAmount() { return highAmount; }
    public boolean isTrustedCustomer() { return trustedCustomer; }
    public boolean isHighRisk() { return highRisk; }
    public String getSegment() { return customerSegment; }
    public String getCustomerSegment() { return customerSegment; }
    public String getPaymentCategory() { return paymentCategory; }
    public String getRecommendedDecisionConfidence() { return recommendedDecisionConfidence; }
    
    // New DRG getters and setters
    public String getIdentityStatus() { return identityStatus; }
    public void setIdentityStatus(String identityStatus) { this.identityStatus = identityStatus; }
    
    public String getDukcapilStatus() { return dukcapilStatus; }
    public void setDukcapilStatus(String dukcapilStatus) { this.dukcapilStatus = dukcapilStatus; }
    
    public String getIdentityRisk() { return identityRisk; }
    public void setIdentityRisk(String identityRisk) { this.identityRisk = identityRisk; }
    
    public String getTransactionRisk() { return transactionRisk; }
    public void setTransactionRisk(String transactionRisk) { this.transactionRisk = transactionRisk; }
    
    public String getOverallRisk() { return overallRisk; }
    public void setOverallRisk(String overallRisk) { this.overallRisk = overallRisk; }
    
    public Integer getVelocityRisk() { return velocityRisk; }
    public void setVelocityRisk(Integer velocityRisk) { this.velocityRisk = velocityRisk; }
    
    public Integer getFraudScore() { return fraudScore; }
    public void setFraudScore(Integer fraudScore) { this.fraudScore = fraudScore; }
}
