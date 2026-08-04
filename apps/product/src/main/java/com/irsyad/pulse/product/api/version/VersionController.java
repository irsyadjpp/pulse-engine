package com.irsyad.pulse.product.api.version;

import com.irsyad.pulse.product.api.common.ApiResponse;
import com.irsyad.pulse.product.application.service.VersionQueryService;
import com.irsyad.pulse.product.domain.version.ProductVersion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Product Version History (FSD_04 FR-04-04, FSD_05, TSD_04 Section 10).
 */
@RestController
@RequestMapping("/api/v1/products/{productId}/versions")
@Tag(name = "Version", description = "Product Version APIs")
public class VersionController {

    private final VersionQueryService versionQueryService;

    public VersionController(VersionQueryService versionQueryService) {
        this.versionQueryService = versionQueryService;
    }

    @GetMapping
    @Operation(summary = "Product Versions", description = "Returns the version history of a Product.")
    public ApiResponse<List<VersionResponse>> history(@PathVariable UUID productId) {
        List<VersionResponse> result = this.versionQueryService.history(productId).stream()
                .map(this::toResponse)
                .toList();
        return ApiResponse.success(result);
    }

    @GetMapping("/{version}")
    @Operation(summary = "Product Version Detail", description = "Returns a specific Product Version.")
    public ApiResponse<VersionResponse> detail(@PathVariable UUID productId, @PathVariable int version) {
        return ApiResponse.success(this.toResponse(this.versionQueryService.detail(productId, version)));
    }

    private VersionResponse toResponse(ProductVersion version) {
        return new VersionResponse(
                version.getProductVersionId(),
                version.getProductId(),
                version.getVersion(),
                version.getStatus(),
                version.getEffectiveDate(),
                version.getPublishedDate(),
                version.getCreatedAt(),
                version.getCreatedBy());
    }
}