package com.irsyad.pulse.product.test.support;

import com.irsyad.pulse.product.domain.company.Company;
import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.shared.CompanyStatus;
import com.irsyad.pulse.product.domain.shared.ProductStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Test data factory for building domain objects in tests (FSD_10 Section 21).
 */
public class TestDataFactory {

    public Company createCompany(String companyCode, String companyName) {
        return Company.builder()
                .companyId(UUID.randomUUID())
                .companyCode(companyCode)
                .companyName(companyName)
                .contactInformation("{\"email\":\"partner@example.com\"}")
                .status(CompanyStatus.ACTIVE)
                .createdAt(Instant.now())
                .createdBy("test")
                .updatedAt(Instant.now())
                .updatedBy("test")
                .version(0L)
                .deleted(false)
                .build();
    }

    public Product createDraftProduct(UUID companyId, String productCode, String productName) {
        return Product.builder()
                .productId(UUID.randomUUID())
                .companyId(companyId)
                .productCode(productCode)
                .productName(productName)
                .category("PERSONAL_ACCIDENT")
                .version(1)
                .status(ProductStatus.DRAFT)
                .effectiveDate(LocalDate.now())
                .createdAt(Instant.now())
                .createdBy("test")
                .updatedAt(Instant.now())
                .updatedBy("test")
                .optimisticLockVersion(0L)
                .deleted(false)
                .build();
    }
}
