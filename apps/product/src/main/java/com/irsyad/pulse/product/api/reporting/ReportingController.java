package com.irsyad.pulse.product.api.reporting;

import com.irsyad.pulse.product.api.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Reporting API (FSD_08).
 * Reporting is read-only and mandatory paginated.
 */
@RestController
@RequestMapping("/api/v1/reports")
public class ReportingController {

    @GetMapping("/companies")
    public ApiResponse<String> companyReport() {
        throw new UnsupportedOperationException("Company report query is not yet implemented.");
    }

    @GetMapping("/products")
    public ApiResponse<String> productReport() {
        throw new UnsupportedOperationException("Product report query is not yet implemented.");
    }

    @GetMapping("/products/status")
    public ApiResponse<String> productStatusReport() {
        throw new UnsupportedOperationException("Product status report query is not yet implemented.");
    }

    @GetMapping("/product-versions")
    public ApiResponse<String> productVersionReport() {
        throw new UnsupportedOperationException("Product version report query is not yet implemented.");
    }

    @GetMapping("/product-configurations")
    public ApiResponse<String> productConfigurationReport() {
        throw new UnsupportedOperationException("Product configuration report query is not yet implemented.");
    }

    @GetMapping("/audit")
    public ApiResponse<String> auditReport() {
        throw new UnsupportedOperationException("Audit report query is not yet implemented.");
    }
}
