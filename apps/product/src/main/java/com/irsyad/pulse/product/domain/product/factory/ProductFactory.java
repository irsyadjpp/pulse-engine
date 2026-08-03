package com.irsyad.pulse.product.domain.product.factory;

import com.irsyad.pulse.product.domain.company.valueobject.CompanyId;
import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.product.valueobject.ProductCode;
import com.irsyad.pulse.product.domain.product.valueobject.ProductId;
import com.irsyad.pulse.product.domain.product.valueobject.ProductVersionNumber;

/**
 * Factory for creating valid Product aggregate instances (TSD_02 Section 18).
 */
public final class ProductFactory {

    private ProductFactory() {
    }

    public static Product create(CompanyId companyId, ProductCode code, String productName) {
        Product product = Product.builder()
                .productId(ProductId.generate().value())
                .companyId(companyId.value())
                .productCode(code.value())
                .productName(productName)
                .version(ProductVersionNumber.initial().value())
                .build();
        return product;
    }
}
