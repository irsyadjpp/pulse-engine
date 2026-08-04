package com.irsyad.pulse.product.api.audit;

import com.irsyad.pulse.product.api.common.ApiResponse;
import com.irsyad.pulse.product.application.service.AuditQueryService;
import com.irsyad.pulse.product.domain.audit.AuditHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Audit History (FSD_05 Section 18, TSD_04 Section 11).
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Audit", description = "Audit History APIs")
public class AuditController {

    private final AuditQueryService auditQueryService;

    public AuditController(AuditQueryService auditQueryService) {
        this.auditQueryService = auditQueryService;
    }

    @GetMapping("/products/{productId}/audit")
    @Operation(summary = "Audit History", description = "Returns the audit history of a Product.")
    public ApiResponse<List<AuditResponse>> productAudit(@PathVariable UUID productId) {
        List<AuditResponse> result = this.auditQueryService.productAudit(productId).stream()
                .map(this::toResponse)
                .toList();
        return ApiResponse.success(result);
    }

    @GetMapping("/audit/{auditId}")
    @Operation(summary = "Audit Detail", description = "Returns a single audit record.")
    public ApiResponse<AuditResponse> auditDetail(@PathVariable UUID auditId) {
        return ApiResponse.success(this.toResponse(this.auditQueryService.auditDetail(auditId)));
    }

    private AuditResponse toResponse(AuditHistory audit) {
        return new AuditResponse(
                audit.getAuditId(),
                audit.getEntityName(),
                audit.getEntityId(),
                audit.getAction(),
                audit.getVersion(),
                audit.getBeforeData(),
                audit.getAfterData(),
                audit.getReason(),
                audit.getCorrelationId(),
                audit.getCreatedBy(),
                audit.getCreatedAt());
    }
}