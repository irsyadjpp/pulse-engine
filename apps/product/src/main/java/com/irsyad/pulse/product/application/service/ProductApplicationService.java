package com.irsyad.pulse.product.application.service;

import com.irsyad.pulse.product.application.command.product.CreateProductCommand;
import com.irsyad.pulse.product.application.command.product.UpdateProductCommand;
import com.irsyad.pulse.product.application.port.AuditPort;
import com.irsyad.pulse.product.application.port.CompanyRepositoryPort;
import com.irsyad.pulse.product.application.port.ProductRepositoryPort;
import com.irsyad.pulse.product.application.port.ProductVersionPort;
import com.irsyad.pulse.product.domain.audit.AuditHistory;
import com.irsyad.pulse.product.infrastructure.cache.CacheConfig;
import com.irsyad.pulse.product.domain.company.Company;
import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.company.valueobject.CompanyId;
import com.irsyad.pulse.product.domain.product.factory.ProductFactory;
import com.irsyad.pulse.product.domain.product.valueobject.ProductCode;
import com.irsyad.pulse.product.domain.shared.AuditAction;
import com.irsyad.pulse.product.domain.shared.CompanyStatus;
import com.irsyad.pulse.product.domain.shared.EntityName;
import com.irsyad.pulse.product.domain.shared.ProductStatus;
import com.irsyad.pulse.product.domain.version.ProductVersion;
import com.irsyad.pulse.product.shared.exception.CompanyInactiveException;
import com.irsyad.pulse.product.shared.exception.CompanyNotFoundException;
import com.irsyad.pulse.product.shared.exception.DuplicateProductCodeException;
import com.irsyad.pulse.product.shared.exception.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ProductApplicationService {

    private final ProductRepositoryPort productRepositoryPort;
    private final ProductVersionPort productVersionPort;
    private final CompanyRepositoryPort companyRepositoryPort;
    private final ProductSnapshotService productSnapshotService;
    private final AuditPort auditPort;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    public ProductApplicationService(ProductRepositoryPort productRepositoryPort,
                                     ProductVersionPort productVersionPort,
                                     CompanyRepositoryPort companyRepositoryPort,
                                     ProductSnapshotService productSnapshotService,
                                     AuditPort auditPort,
                                     ApplicationEventPublisher eventPublisher,
                                     ObjectMapper objectMapper) {
        this.productRepositoryPort = productRepositoryPort;
        this.productVersionPort = productVersionPort;
        this.companyRepositoryPort = companyRepositoryPort;
        this.productSnapshotService = productSnapshotService;
        this.auditPort = auditPort;
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @CacheEvict(cacheNames = {CacheConfig.PRODUCT_DETAIL_CACHE, CacheConfig.PRODUCT_LISTING_CACHE, 
                              CacheConfig.PRODUCT_VERSION_CACHE}, allEntries = true)
    @PreAuthorize("hasAuthority('SCOPE_product.write')")
    public Product create(CreateProductCommand command) {
        Company company = this.companyRepositoryPort.findById(command.companyId())
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        if (company.getStatus() != CompanyStatus.ACTIVE) {
            throw new CompanyInactiveException("Product cannot be created for an INACTIVE company (BR-021).");
        }
        this.productRepositoryPort.findByCompanyIdAndProductCode(command.companyId(), command.productCode())
                .ifPresent(existing -> {
                    throw new DuplicateProductCodeException("Product Code already exists for this company.");
                });
        Product product = ProductFactory.create(
                new CompanyId(command.companyId()),
                new ProductCode(command.productCode()),
                command.productName(),
                command.category(),
                command.effectiveDate(),
                command.expiryDate(),
                this.currentUsername());
        Product saved = this.productRepositoryPort.save(product);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.PRODUCT, saved.getProductId(), AuditAction.CREATE,
                saved.getVersion(), null, null));
        return saved;
    }

    @Transactional
    @CacheEvict(cacheNames = {CacheConfig.PRODUCT_DETAIL_CACHE, CacheConfig.PRODUCT_LISTING_CACHE, 
                              CacheConfig.PRODUCT_VERSION_CACHE}, allEntries = true)
    public Product update(UpdateProductCommand command) {
        Product product = this.productRepositoryPort.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        String beforeData = this.toJson(product);
        product.updateDraft(command.productName(), command.category(),
                command.effectiveDate(), command.expiryDate());
        Product saved = this.productRepositoryPort.save(product);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.PRODUCT, saved.getProductId(), AuditAction.UPDATE,
                saved.getVersion(), beforeData, this.toJson(saved)));
        return saved;
    }

    @Transactional
    @CacheEvict(cacheNames = {CacheConfig.PRODUCT_DETAIL_CACHE, CacheConfig.PRODUCT_LISTING_CACHE, 
                              CacheConfig.PRODUCT_VERSION_CACHE}, allEntries = true)
    @PreAuthorize("hasAuthority('SCOPE_product.publish')")
    public Product publish(UUID productId) {
        Product product = this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        if (product.getStatus() == ProductStatus.PUBLISHED) {
            return product;
        }
        String beforeData = this.toJson(product);
        product.publish();
        Product saved = this.productRepositoryPort.save(product);
        this.createVersionSnapshot(saved);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.PRODUCT, saved.getProductId(), AuditAction.PUBLISH,
                saved.getVersion(), beforeData, this.toJson(saved)));
        return saved;
    }

    @Transactional
    @CacheEvict(cacheNames = {CacheConfig.PRODUCT_DETAIL_CACHE, CacheConfig.PRODUCT_LISTING_CACHE, 
                              CacheConfig.PRODUCT_VERSION_CACHE}, allEntries = true)
    public Product archive(UUID productId) {
        Product product = this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        String beforeData = this.toJson(product);
        product.archive();
        Product saved = this.productRepositoryPort.save(product);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.PRODUCT, saved.getProductId(), AuditAction.ARCHIVE,
                saved.getVersion(), beforeData, this.toJson(saved)));
        return saved;
    }

    @Transactional
    @CacheEvict(cacheNames = {CacheConfig.PRODUCT_DETAIL_CACHE, CacheConfig.PRODUCT_LISTING_CACHE, 
                              CacheConfig.PRODUCT_VERSION_CACHE}, allEntries = true)
    public Product createNewVersion(UUID productId) {
        Product product = this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        String beforeData = this.toJson(product);
        product.createNewVersion();
        Product saved = this.productRepositoryPort.save(product);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.PRODUCT, saved.getProductId(), AuditAction.UPDATE,
                saved.getVersion(), beforeData, this.toJson(saved)));
        return saved;
    }

    @Transactional
    public Product addCoverage(UUID productId, com.irsyad.pulse.product.domain.product.coverage.Coverage coverage) {
        Product product = this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        product.addCoverage(coverage);
        Product saved = this.productRepositoryPort.save(product);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.PRODUCT, saved.getProductId(), AuditAction.UPDATE,
                saved.getVersion(), null, null));
        return saved;
    }

    @Transactional
    public Product addBenefit(UUID productId, com.irsyad.pulse.product.domain.product.benefit.Benefit benefit) {
        Product product = this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        product.addBenefit(benefit);
        Product saved = this.productRepositoryPort.save(product);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.PRODUCT, saved.getProductId(), AuditAction.UPDATE,
                saved.getVersion(), null, null));
        return saved;
    }

    private void publishEvents(Product product) {
        product.pullDomainEvents().forEach(this.eventPublisher::publishEvent);
    }

    private void createVersionSnapshot(Product product) {
        String snapshot = this.productSnapshotService.buildSnapshot(product.getProductId());
        ProductVersion version = ProductVersion.builder()
                .productVersionId(UUID.randomUUID())
                .productId(product.getProductId())
                .version(product.getVersion())
                .status(product.getStatus())
                .effectiveDate(product.getEffectiveDate())
                .publishedDate(Instant.now())
                .snapshot(snapshot)
                .createdAt(Instant.now())
                .createdBy(this.currentUsername())
                .build();
        this.productVersionPort.save(version);
    }

    private String toJson(Product product) {
        try {
            return this.objectMapper.writeValueAsString(product);
        } catch (Exception e) {
            return "{\"error\": \"serialization failed\"}";
        }
    }

    private AuditHistory audit(EntityName entityName, UUID entityId, AuditAction action,
                               int version, String beforeData, String afterData) {
        String correlationId = MDC.get("correlationId");
        UUID correlationUuid = correlationId != null ? UUID.fromString(correlationId) : null;
        return AuditHistory.builder()
                .auditId(UUID.randomUUID())
                .entityName(entityName)
                .entityId(entityId)
                .action(action)
                .version(version)
                .beforeData(beforeData)
                .afterData(afterData)
                .reason(null)
                .correlationId(correlationUuid)
                .createdBy(this.currentUsername())
                .createdAt(Instant.now())
                .build();
    }

    private String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "system";
    }
}
