package com.irsyad.pulse.product.application.service;

import com.irsyad.pulse.product.application.port.ProductVersionPort;
import com.irsyad.pulse.product.domain.version.ProductVersion;
import com.irsyad.pulse.product.shared.exception.VersionNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class VersionQueryService {

    private final ProductVersionPort productVersionPort;

    public VersionQueryService(ProductVersionPort productVersionPort) {
        this.productVersionPort = productVersionPort;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "product-version", key = "#productId")
    public List<ProductVersion> history(UUID productId) {
        return this.productVersionPort.findByProductId(productId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "product-version", key = "#productId + '::' + #version")
    public ProductVersion detail(UUID productId, int version) {
        return this.productVersionPort.findByProductIdAndVersion(productId, version)
                .orElseThrow(() -> new VersionNotFoundException("Version not found."));
    }
}
