package com.irsyad.pulse.product.domain.product.repository;

import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.product.valueobject.ProductCode;
import com.irsyad.pulse.product.domain.product.valueobject.ProductId;

import java.util.Optional;

/**
 * Domain repository interface (TSD_02 Section 19).
 *
 * <p>Repository berada pada Domain. Implementasi berada pada Infrastructure.
 */
public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(ProductId id);

    Optional<Product> findByProductCode(ProductCode code);
}
