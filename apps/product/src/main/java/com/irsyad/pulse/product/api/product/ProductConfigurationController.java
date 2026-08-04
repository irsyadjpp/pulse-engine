package com.irsyad.pulse.product.api.product;

import com.irsyad.pulse.product.api.common.ApiResponse;
import com.irsyad.pulse.product.application.service.ProductConfigurationService;
import com.irsyad.pulse.product.domain.product.benefit.Benefit;
import com.irsyad.pulse.product.domain.product.coverage.Coverage;
import com.irsyad.pulse.product.domain.product.document.ProductDocument;
import com.irsyad.pulse.product.domain.product.eligibility.Eligibility;
import com.irsyad.pulse.product.domain.product.exclusion.Exclusion;
import com.irsyad.pulse.product.domain.product.premium.PremiumConfiguration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for Product Configuration (TSD_04 Section 9, FSD_03).
 *
 * <p>Supports PUT collection (replace all) as the primary API and CRUD per-item
 * as additional API (A3 decision).
 */
@RestController
@RequestMapping("/api/v1/products/{productId}")
@Tag(name = "Product Configuration", description = "Product Configuration APIs")
public class ProductConfigurationController {

    private final ProductConfigurationService configurationService;

    public ProductConfigurationController(ProductConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    // ---- Coverage ----

    @PutMapping("/coverage")
    @Operation(summary = "Replace Coverages", description = "Replaces all coverages of a Product (PUT collection).")
    public ResponseEntity<ApiResponse<List<Coverage>>> replaceCoverages(
            @PathVariable UUID productId, @RequestBody List<CoverageRequest> request) {
        List<Coverage> coverages = request.stream().map(r -> Coverage.builder()
                .coverageId(r.coverageId())
                .coverageAmount(r.coverageAmount())
                .currency(r.currency())
                .build()).toList();
        return ResponseEntity.ok(ApiResponse.success(this.configurationService.replaceCoverages(productId, coverages)));
    }

    @PostMapping("/coverages")
    @Operation(summary = "Add Coverage", description = "Adds a single coverage (CRUD per-item).")
    public ResponseEntity<ApiResponse<Coverage>> addCoverage(
            @PathVariable UUID productId, @RequestBody CoverageRequest request) {
        Coverage coverage = Coverage.builder()
                .coverageAmount(request.coverageAmount())
                .currency(request.currency())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(this.configurationService.addCoverage(productId, coverage)));
    }

    // ---- Benefit ----

    @PutMapping("/benefits")
    @Operation(summary = "Replace Benefits", description = "Replaces all benefits of a Product (PUT collection).")
    public ResponseEntity<ApiResponse<List<Benefit>>> replaceBenefits(
            @PathVariable UUID productId, @RequestBody List<BenefitRequest> request) {
        List<Benefit> benefits = request.stream().map(r -> Benefit.builder()
                .benefitId(r.benefitId())
                .benefitName(r.benefitName())
                .description(r.description())
                .maximumLimit(r.maximumLimit())
                .build()).toList();
        return ResponseEntity.ok(ApiResponse.success(this.configurationService.replaceBenefits(productId, benefits)));
    }

    @PostMapping("/benefits")
    @Operation(summary = "Add Benefit", description = "Adds a single benefit (CRUD per-item).")
    public ResponseEntity<ApiResponse<Benefit>> addBenefit(
            @PathVariable UUID productId, @RequestBody BenefitRequest request) {
        Benefit benefit = Benefit.builder()
                .benefitName(request.benefitName())
                .description(request.description())
                .maximumLimit(request.maximumLimit())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(this.configurationService.addBenefit(productId, benefit)));
    }

    // ---- Exclusion ----

    @PutMapping("/exclusions")
    @Operation(summary = "Replace Exclusions", description = "Replaces all exclusions of a Product (PUT collection).")
    public ResponseEntity<ApiResponse<List<Exclusion>>> replaceExclusions(
            @PathVariable UUID productId, @RequestBody List<ExclusionRequest> request) {
        List<Exclusion> exclusions = request.stream().map(r -> Exclusion.builder()
                .exclusionId(r.exclusionId())
                .description(r.description())
                .build()).toList();
        return ResponseEntity.ok(ApiResponse.success(this.configurationService.replaceExclusions(productId, exclusions)));
    }

    @PostMapping("/exclusions")
    @Operation(summary = "Add Exclusion", description = "Adds a single exclusion (CRUD per-item).")
    public ResponseEntity<ApiResponse<Exclusion>> addExclusion(
            @PathVariable UUID productId, @RequestBody ExclusionRequest request) {
        Exclusion exclusion = Exclusion.builder().description(request.description()).build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(this.configurationService.addExclusion(productId, exclusion)));
    }

    // ---- Eligibility ----

    @PutMapping("/eligibility")
    @Operation(summary = "Replace Eligibility", description = "Replaces the eligibility configuration of a Product.")
    public ResponseEntity<ApiResponse<Eligibility>> replaceEligibility(
            @PathVariable UUID productId, @RequestBody EligibilityRequest request) {
        Eligibility eligibility = Eligibility.builder()
                .minimumAge(request.minimumAge())
                .maximumAge(request.maximumAge())
                .occupationClass(request.occupationClass())
                .nationality(request.nationality())
                .residency(request.residency())
                .build();
        return ResponseEntity.ok(ApiResponse.success(this.configurationService.replaceEligibility(productId, eligibility)));
    }

    // ---- Premium ----

    @PutMapping("/premium")
    @Operation(summary = "Replace Premiums", description = "Replaces all premium configurations of a Product (PUT collection).")
    public ResponseEntity<ApiResponse<List<PremiumConfiguration>>> replacePremiums(
            @PathVariable UUID productId, @RequestBody List<PremiumRequest> request) {
        List<PremiumConfiguration> premiums = request.stream().map(r -> PremiumConfiguration.builder()
                .premiumConfigurationId(r.premiumConfigurationId())
                .coverageBand(r.coverageBand())
                .ageBand(r.ageBand())
                .occupationClass(r.occupationClass())
                .basePremium(r.basePremium())
                .build()).toList();
        return ResponseEntity.ok(ApiResponse.success(this.configurationService.replacePremiums(productId, premiums)));
    }

    @PostMapping("/premium-configurations")
    @Operation(summary = "Add Premium", description = "Adds a single premium configuration (CRUD per-item).")
    public ResponseEntity<ApiResponse<PremiumConfiguration>> addPremium(
            @PathVariable UUID productId, @RequestBody PremiumRequest request) {
        PremiumConfiguration premium = PremiumConfiguration.builder()
                .coverageBand(request.coverageBand())
                .ageBand(request.ageBand())
                .occupationClass(request.occupationClass())
                .basePremium(request.basePremium())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(this.configurationService.addPremium(productId, premium)));
    }

    // ---- Document ----

    @PutMapping("/documents")
    @Operation(summary = "Replace Documents", description = "Replaces all document metadata of a Product (PUT collection).")
    public ResponseEntity<ApiResponse<List<ProductDocument>>> replaceDocuments(
            @PathVariable UUID productId, @RequestBody List<DocumentRequest> request) {
        List<ProductDocument> documents = request.stream().map(r -> ProductDocument.builder()
                .documentId(r.documentId())
                .documentName(r.documentName())
                .documentType(r.documentType())
                .storageReference(r.storageReference())
                .build()).toList();
        return ResponseEntity.ok(ApiResponse.success(this.configurationService.replaceDocuments(productId, documents)));
    }

    @PostMapping("/documents")
    @Operation(summary = "Add Document", description = "Adds a single document metadata (CRUD per-item).")
    public ResponseEntity<ApiResponse<ProductDocument>> addDocument(
            @PathVariable UUID productId, @RequestBody DocumentRequest request) {
        ProductDocument document = ProductDocument.builder()
                .documentName(request.documentName())
                .documentType(request.documentType())
                .storageReference(request.storageReference())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(this.configurationService.addDocument(productId, document)));
    }

    // ---- Request DTOs ----

    public record CoverageRequest(UUID coverageId, BigDecimal coverageAmount, String currency) {
    }

    public record BenefitRequest(UUID benefitId, String benefitName, String description, BigDecimal maximumLimit) {
    }

    public record ExclusionRequest(UUID exclusionId, String description) {
    }

    public record EligibilityRequest(Integer minimumAge, Integer maximumAge, String occupationClass,
                                     String nationality, String residency) {
    }

    public record PremiumRequest(UUID premiumConfigurationId, String coverageBand, String ageBand,
                                 String occupationClass, BigDecimal basePremium) {
    }

    public record DocumentRequest(UUID documentId, String documentName, String documentType, String storageReference) {
    }
}
