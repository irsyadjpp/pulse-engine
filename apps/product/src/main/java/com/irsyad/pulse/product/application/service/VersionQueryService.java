package com.irsyad.pulse.product.application.service;

import com.irsyad.pulse.product.application.port.ProductVersionPort;
import com.irsyad.pulse.product.domain.version.ProductVersion;
import com.irsyad.pulse.product.shared.exception.VersionNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Query service for Product Version history (TSD_04 Section 10, FSD_05).
 */
@Service
public class VersionQueryService {

    private final ProductVersionPort productVersionPort;

    public VersionQueryService(ProductVersionPort productVersionPort) {
        this.productVersionPort = productVersionPort;
    }

    @Transactional(readOnly = true)
    public List<ProductVersion> history(UUID productId) {
        return this.productVersionPort.findByProductId(productId);
    }

    @Transactional(readOnly = true)
    public ProductVersion detail(UUID productId, int version) {
        return this.productVersionPort.findByProductIdAndVersion(productId, version)
                .orElseThrow(() -> new VersionNotFoundException("Version not found."));
    }
}
