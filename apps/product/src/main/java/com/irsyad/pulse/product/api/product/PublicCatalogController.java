package com.irsyad.pulse.product.api.product;

import com.irsyad.pulse.product.api.common.ApiResponse;
import com.irsyad.pulse.product.application.service.PublicCatalogService;
import com.irsyad.pulse.product.domain.product.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Public Catalog API (FSD_04 QD-06, FSD_07 ID-01).
 * Only returns PUBLISHED, effective, not-yet-expired products.
 *
 * <p>Uses {@link PublicCatalogService} as the single orchestrator (TSD_04 Section 25:
 * repository must not be called directly from controllers).
 */
@RestController
@RequestMapping("/api/v1/catalog/products")
@Tag(name = "Public Catalog", description = "Public Catalog API for Marketplace/Quote/Proposal/Checkout")
public class PublicCatalogController {

    private final PublicCatalogService publicCatalogService;

    public PublicCatalogController(PublicCatalogService publicCatalogService) {
        this.publicCatalogService = publicCatalogService;
    }

    @GetMapping
    @Operation(summary = "List Published Products", description = "Returns only PUBLISHED, effective, not-yet-expired products.")
    public ApiResponse<List<ProductResponse>> list(
            @RequestParam(required = false) UUID companyId,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String productName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<ProductResponse> result = this.publicCatalogService
                .listPublished(companyId, productCode, productName, page, size)
                .stream()
                .map(this::toResponse)
                .toList();
        return ApiResponse.success(result);
    }

    @GetMapping("/{productCode}")
    @Operation(summary = "Get Published Product by Code", description = "Returns a single PUBLISHED product by business code.")
    public ApiResponse<ProductResponse> byCode(@PathVariable String productCode) {
        return this.publicCatalogService.findByCode(productCode)
                .map(this::toResponse)
                .map(ApiResponse::success)
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getProductId(),
                product.getCompanyId(),
                product.getProductCode(),
                product.getProductName(),
                product.getCategory(),
                product.getVersion(),
                product.getStatus(),
                product.getEffectiveDate(),
                product.getExpiryDate());
    }
}