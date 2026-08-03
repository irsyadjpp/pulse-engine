package com.irsyad.pulse.product.api.audit;

import com.irsyad.pulse.product.api.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for Audit History (FSD_05 Section 18).
 */
@RestController
@RequestMapping("/api/v1")
public class AuditController {

    @GetMapping("/products/{productId}/audit")
    public ApiResponse<String> productAudit(@PathVariable UUID productId) {
        throw new UnsupportedOperationException("Product audit query is not yet implemented.");
    }

    @GetMapping("/audit/{auditId}")
    public ApiResponse<String> auditDetail(@PathVariable UUID auditId) {
        throw new UnsupportedOperationException("Audit detail query is not yet implemented.");
    }
}
