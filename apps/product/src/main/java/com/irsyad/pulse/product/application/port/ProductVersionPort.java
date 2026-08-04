package com.irsyad.pulse.product.application.port;

import com.irsyad.pulse.product.domain.version.ProductVersion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Product Version persistence (TSD_04 Section 10, TSD_07).
 */
public interface ProductVersionPort {

    ProductVersion save(ProductVersion version);

    List<ProductVersion> findByProductId(UUID productId);

    Optional<ProductVersion> findByProductIdAndVersion(UUID productId, int version);
}
