package com.irsyad.pulse.product.application.service;

import com.irsyad.pulse.product.application.port.ProductRepositoryPort;
import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.shared.ProductStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service for the Public Catalog (FSD_04 QD-06, FSD_07 ID-01).
 *
 * <p>Only returns PUBLISHED, effective, not-yet-expired products. This service is
 * the single orchestrator for public catalog reads (TSD_04 Section 25: repository
 * must not be called directly from controllers).
 */
@Service
public class PublicCatalogService {

    private final ProductRepositoryPort productRepositoryPort;

    public PublicCatalogService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Transactional(readOnly = true)
    public List<Product> listPublished(UUID companyId, String productCode, String productName,
                                       int page, int size) {
        LocalDate today = LocalDate.now();
        return this.productRepositoryPort
                .search(companyId, productCode, productName, null, ProductStatus.PUBLISHED, null, null, page, size)
                .getContent()
                .stream()
                .filter(p -> !p.getEffectiveDate().isAfter(today))
                .filter(p -> p.getExpiryDate() == null || !p.getExpiryDate().isBefore(today))
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<Product> findByCode(String productCode) {
        LocalDate today = LocalDate.now();
        return this.productRepositoryPort
                .search(null, productCode, null, null, ProductStatus.PUBLISHED, null, null, 0, 1)
                .getContent()
                .stream()
                .filter(p -> !p.getEffectiveDate().isAfter(today))
                .filter(p -> p.getExpiryDate() == null || !p.getExpiryDate().isBefore(today))
                .findFirst();
    }
}