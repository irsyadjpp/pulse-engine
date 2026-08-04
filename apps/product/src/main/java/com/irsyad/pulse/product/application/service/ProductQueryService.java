package com.irsyad.pulse.product.application.service;

import com.irsyad.pulse.product.application.port.ProductRepositoryPort;
import com.irsyad.pulse.product.application.query.product.SearchProductQuery;
import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.shared.exception.ProductNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Query handler for Product read use cases (Lightweight CQRS - TSD_01 Section 17).
 *
 * <p>Separated from {@link ProductApplicationService} (command handler) to
 * provide a clear read/write separation. Read operations are cached in Redis
 * for read optimization (TSD_01 Section 19).
 */
@Service
public class ProductQueryService {

    private final ProductRepositoryPort productRepositoryPort;

    public ProductQueryService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "productDetail", key = "#productId")
    public Product detail(UUID productId) {
        return this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "productListing", key = "{#query.companyId(), #query.productCode(), #query.productName(), #query.category(), #query.status(), #query.effectiveDate(), #query.page(), #query.size()}")
    public List<Product> search(SearchProductQuery query) {
        return this.productRepositoryPort.search(
                query.companyId(), query.productCode(), query.productName(), query.category(),
                query.status(), query.effectiveDate(), query.page(), query.size());
    }
}