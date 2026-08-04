package com.irsyad.pulse.product.application.service;

import com.irsyad.pulse.product.application.command.company.CreateCompanyCommand;
import com.irsyad.pulse.product.application.command.company.UpdateCompanyCommand;
import com.irsyad.pulse.product.application.port.AuditPort;
import com.irsyad.pulse.product.application.port.CompanyRepositoryPort;
import com.irsyad.pulse.product.application.query.company.SearchCompanyQuery;
import com.irsyad.pulse.product.domain.audit.AuditHistory;
import com.irsyad.pulse.product.domain.company.Company;
import com.irsyad.pulse.product.domain.shared.AuditAction;
import com.irsyad.pulse.product.domain.shared.CompanyStatus;
import com.irsyad.pulse.product.domain.shared.EntityName;
import com.irsyad.pulse.product.shared.exception.CompanyNotFoundException;
import com.irsyad.pulse.product.shared.exception.DuplicateCompanyCodeException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * Application service orchestrating Company use cases (FSD_01).
 * Validates uniqueness of Company Code before persisting.
 *
 * <p>Transaction boundary is at Application Layer (TSD_01 Section 18).
 */
@Service
public class CompanyApplicationService {

    private final CompanyRepositoryPort companyRepositoryPort;
    private final AuditPort auditPort;
    private final ApplicationEventPublisher eventPublisher;

    public CompanyApplicationService(CompanyRepositoryPort companyRepositoryPort, AuditPort auditPort,
                                     ApplicationEventPublisher eventPublisher) {
        this.companyRepositoryPort = companyRepositoryPort;
        this.auditPort = auditPort;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Company create(CreateCompanyCommand command) {
        this.companyRepositoryPort.findByCompanyCode(command.companyCode())
                .ifPresent(existing -> {
                    throw new DuplicateCompanyCodeException("Company Code already exists.");
                });
        Company company = Company.builder()
                .companyId(UUID.randomUUID())
                .companyCode(command.companyCode())
                .companyName(command.companyName())
                .logoUrl(command.logoUrl())
                .contactInformation(command.contactInformation())
                .status(CompanyStatus.ACTIVE)
                .createdAt(Instant.now())
                .createdBy("system")
                .updatedAt(Instant.now())
                .updatedBy("system")
                .version(0L)
                .deleted(false)
                .build();
        Company saved = this.companyRepositoryPort.save(company);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.COMPANY, saved.getCompanyId(), AuditAction.CREATE, null, null));
        return saved;
    }

    @Transactional
    public Company update(UpdateCompanyCommand command) {
        Company company = this.companyRepositoryPort.findById(command.companyId())
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        company.updateProfile(command.companyName(), command.logoUrl(), command.contactInformation());
        Company saved = this.companyRepositoryPort.save(company);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.COMPANY, saved.getCompanyId(), AuditAction.UPDATE, null, null));
        return saved;
    }

    @Transactional
    public Company activate(UUID companyId) {
        Company company = this.companyRepositoryPort.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        company.activate();
        Company saved = this.companyRepositoryPort.save(company);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.COMPANY, saved.getCompanyId(), AuditAction.ACTIVATE, null, null));
        return saved;
    }

    @Transactional
    public Company deactivate(UUID companyId) {
        Company company = this.companyRepositoryPort.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        company.deactivate();
        Company saved = this.companyRepositoryPort.save(company);
        this.publishEvents(saved);
        this.auditPort.save(this.audit(EntityName.COMPANY, saved.getCompanyId(), AuditAction.DEACTIVATE, null, null));
        return saved;
    }

    @Transactional(readOnly = true)
    public Company detail(UUID companyId) {
        return this.companyRepositoryPort.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
    }

    @Transactional(readOnly = true)
    public Page<Company> search(SearchCompanyQuery query) {
        return this.companyRepositoryPort.search(query.keyword(), query.status(), query.sort(),
                query.page(), query.size());
    }

    private void publishEvents(Company company) {
        company.pullDomainEvents().forEach(this.eventPublisher::publishEvent);
    }

    private AuditHistory audit(EntityName entityName, UUID entityId, AuditAction action,
                               String beforeData, String afterData) {
        return AuditHistory.builder()
                .auditId(UUID.randomUUID())
                .entityName(entityName)
                .entityId(entityId)
                .action(action)
                .beforeData(beforeData)
                .afterData(afterData)
                .createdBy("system")
                .createdAt(Instant.now())
                .build();
    }
}
