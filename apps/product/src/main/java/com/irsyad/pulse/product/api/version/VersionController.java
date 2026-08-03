package com.irsyad.pulse.product.api.version;

import com.irsyad.pulse.product.api.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for Product Version History (FSD_04 FR-04-04, FSD_05).
 */
@RestController
@RequestMapping("/api/v1/products/{productId}/versions")
public class VersionController {

    @GetMapping
    public ApiResponse<String> history(@PathVariable UUID productId) {
        throw new UnsupportedOperationException("Version history query is not yet implemented.");
    }

    @GetMapping("/{version}")
    public ApiResponse<String> detail(@PathVariable UUID productId, @PathVariable int version) {
        throw new UnsupportedOperationException("Version detail query is not yet implemented.");
    }
}
