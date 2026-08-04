package com.irsyad.pulse.product.domain.product.factory;

import com.irsyad.pulse.product.domain.company.valueobject.CompanyId;
import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.product.valueobject.ProductCode;
import com.irsyad.pulse.product.domain.product.valueobject.ProductId;

import java.time.LocalDate;

/**
 * Factory for creating valid Product aggregate instances (TSD_02 Section 18).
 */
public final class ProductFactory {

    private ProductFactory() {
    }

    public static Product create(CompanyId companyId, ProductCode code, String productName,
                                 String category, LocalDate effectiveDate, LocalDate expiryDate,
                                 String createdBy) {
        return Product.create(
                ProductId.generate().value(),
                companyId.value(),
                code.value(),
                productName,
                category,
                effectiveDate,
                expiryDate,
                createdBy);
    }
}
