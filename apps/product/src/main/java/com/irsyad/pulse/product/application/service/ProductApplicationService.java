package com.irsyad.pulse.product.application.service;

import com.irsyad.pulse.product.application.command.product.CreateProductCommand;
import com.irsyad.pulse.product.application.command.product.UpdateProductCommand;
import com.irsyad.pulse.product.application.port.AuditPort;
import com.irsyad.pulse.product.application.port.ProductRepositoryPort;
import com.irsyad.pulse.product.application.port.ProductVersionPort;
import com.irsyad.pulse.product.domain.audit.AuditHistory;
import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.shared.AuditAction;
import com.irsyad.pulse.product.domain.shared.EntityName;
import com.irsyad.pulse.product.domain.shared.ProductStatus;
import com.irsyad.pulse.product.domain.version.ProductVersion;
import com.irsyad.pulse.product.shared.exception.DuplicateProductCodeException;
import com.irsyad.pulse.product.shared.exception.ProductNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Application service orchestrating Product use cases (FSD_02).
 * Product lifecycle operations must be executed through the Product aggregate.
 *
 * <p>Transaction boundary is at Application Layer (TSD_01 Section 18).
 */
@Service
public class ProductApplicationService {

    private final ProductRepositoryPort productRepositoryPort;
    private final ProductVersionPort productVersionPort;
    private final AuditPort auditPort;
    private final ApplicationEventPublisher eventPublisher;

    public ProductApplicationService(ProductRepositoryPort productRepositoryPort,
                                     ProductVersionPort productVersionPort,
                                     AuditPort auditPort,
                                     ApplicationEventPublisher eventPublisher) {
        this.productRepositoryPort = productRepositoryPort;
        this.productVersionPort = productVersionPort;
        this.auditPort = auditPort;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @CacheEvict(cacheNames = {"productDetail", "productListing"}, allEntries = true)
    @PreAuthorize("hasAuthority('SCOPE_product.write')")
    public Product create(CreateProductCommand command) {
        this.productRepositoryPort.findByCompanyIdAndProductCode(command.companyId(), command.productCode())
                .ifPresent(existing -> {
                    throw new DuplicateProductCodeException("Product Code already exists for this company.");
                });
        Product product = Product.builder()
                .productId(UUID.randomUUID())
                .companyId(command.companyId())
                .productCode(command.productCode())
                .productName(command.productName())
                .category(command.category())
                .version(1)
                .status(ProductStatus.DRAFT)
                .effectiveDate(command.effectiveDate())
                .expiryDate(command.expiryDate())
                .createdAt(Instant.now())
                .createdBy("system")
                .updatedAt(Instant.now())
                .updatedBy("system")
                .optimisticLockVersion(0L)
                .deleted(false)
                .build();
        Product saved = this.productRepositoryPort.save(product);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.PRODUCT, saved.getProductId(), AuditAction.CREATE,
                saved.getVersion(), null, null));
        return saved;
    }

    @Transactional
    @CacheEvict(cacheNames = {"productDetail", "productListing"}, allEntries = true)
    public Product update(UpdateProductCommand command) {
        Product product = this.productRepositoryPort.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        product.updateDraft(command.productName(), command.category(),
                command.effectiveDate(), command.expiryDate());
        Product saved = this.productRepositoryPort.save(product);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.PRODUCT, saved.getProductId(), AuditAction.UPDATE,
                saved.getVersion(), null, null));
        return saved;
    }

    @Transactional
    @CacheEvict(cacheNames = {"productDetail", "productListing"}, allEntries = true)
    @PreAuthorize("hasAuthority('SCOPE_product.publish')")
    public Product publish(UUID productId) {
        Product product = this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        product.publish();
        Product saved = this.productRepositoryPort.save(product);
        this.createVersionSnapshot(saved);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.PRODUCT, saved.getProductId(), AuditAction.PUBLISH,
                saved.getVersion(), null, null));
        return saved;
    }

    @Transactional
    @CacheEvict(cacheNames = {"productDetail", "productListing"}, allEntries = true)
    public Product archive(UUID productId) {
        Product product = this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        product.archive();
        Product saved = this.productRepositoryPort.save(product);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.PRODUCT, saved.getProductId(), AuditAction.ARCHIVE,
                saved.getVersion(), null, null));
        return saved;
    }

    @Transactional
    @CacheEvict(cacheNames = {"productDetail", "productListing"}, allEntries = true)
    public Product createNewVersion(UUID productId) {
        Product product = this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        product.createNewVersion();
        Product saved = this.productRepositoryPort.save(product);
        this.publishEvents(saved);
        return saved;
    }

    private void publishEvents(Product product) {
        product.pullDomainEvents().forEach(this.eventPublisher::publishEvent);
    }

    private void createVersionSnapshot(Product product) {
        ProductVersion version = ProductVersion.builder()
                .productVersionId(UUID.randomUUID())
                .productId(product.getProductId())
                .version(product.getVersion())
                .status(product.getStatus())
                .effectiveDate(product.getEffectiveDate())
                .publishedDate(Instant.now())
                .createdAt(Instant.now())
                .createdBy("system")
                .build();
        this.productVersionPort.save(version);
    }

    private AuditHistory audit(EntityName entityName, UUID entityId, AuditAction action,
                               int version, String beforeData, String afterData) {
        return AuditHistory.builder()
                .auditId(UUID.randomUUID())
                .entityName(entityName)
                .entityId(entityId)
                .action(action)
                .version(version)
                .beforeData(beforeData)
                .afterData(afterData)
                .createdBy("system")
                .createdAt(Instant.now())
                .build();
    }
}
