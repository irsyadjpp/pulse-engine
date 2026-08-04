package com.irsyad.pulse.product.api.version;

import com.irsyad.pulse.product.api.common.ApiResponse;
import com.irsyad.pulse.product.application.port.AuditPort;
import com.irsyad.pulse.product.application.service.VersionQueryService;
import com.irsyad.pulse.product.domain.audit.AuditHistory;
import com.irsyad.pulse.product.domain.shared.AuditAction;
import static com.irsyad.pulse.product.domain.shared.AuditAction.VERSION_ACCESSED;
import com.irsyad.pulse.product.domain.shared.EntityName;
import com.irsyad.pulse.product.domain.version.ProductVersion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
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
    private final AuditPort auditPort;

    public VersionController(VersionQueryService versionQueryService, AuditPort auditPort) {
        this.versionQueryService = versionQueryService;
        this.auditPort = auditPort;
    }

    @GetMapping
    @Operation(summary = "Product Versions", description = "Returns the version history of a Product.")
    public ApiResponse<List<VersionResponse>> history(@PathVariable UUID productId) {
        List<VersionResponse> result = this.versionQueryService.history(productId).stream()
                .map(this::toResponse)
                .toList();
        this.auditPort.save(this.auditVersionAccessed(EntityName.PRODUCT, productId, VERSION_ACCESSED, null));
        return ApiResponse.success(result);
    }

    @GetMapping("/{version}")
    @Operation(summary = "Product Version Detail", description = "Returns a specific Product Version.")
    public ApiResponse<VersionResponse> detail(@PathVariable UUID productId, @PathVariable int version) {
        ProductVersion productVersion = this.versionQueryService.detail(productId, version);
        this.auditPort.save(this.auditVersionAccessed(EntityName.PRODUCT, productId, VERSION_ACCESSED, version));
        return ApiResponse.success(this.toResponse(productVersion));
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

    private AuditHistory auditVersionAccessed(EntityName entityName, UUID entityId, AuditAction action, Integer version) {
        return AuditHistory.builder()
                .auditId(UUID.randomUUID())
                .entityName(entityName)
                .entityId(entityId)
                .action(action)
                .version(version)
                .beforeData(null)
                .afterData(null)
                .reason("Version accessed")
                .correlationId(null)
                .createdBy("system")
                .createdAt(Instant.now())
                .build();
    }
}
