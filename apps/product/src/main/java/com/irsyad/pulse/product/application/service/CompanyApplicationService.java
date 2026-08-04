package com.irsyad.pulse.product.application.service;

import com.irsyad.pulse.product.application.command.company.CreateCompanyCommand;
import com.irsyad.pulse.product.application.command.company.UpdateCompanyCommand;
import com.irsyad.pulse.product.application.port.CompanyRepositoryPort;
import com.irsyad.pulse.product.application.query.company.SearchCompanyQuery;
import com.irsyad.pulse.product.domain.company.Company;
import com.irsyad.pulse.product.domain.shared.CompanyStatus;
import com.irsyad.pulse.product.shared.exception.CompanyNotFoundException;
import com.irsyad.pulse.product.shared.exception.DuplicateCompanyCodeException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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

    public CompanyApplicationService(CompanyRepositoryPort companyRepositoryPort) {
        this.companyRepositoryPort = companyRepositoryPort;
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
        return this.companyRepositoryPort.save(company);
    }

    @Transactional
    public Company update(UpdateCompanyCommand command) {
        Company company = this.companyRepositoryPort.findById(command.companyId())
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        company.updateProfile(command.companyName(), command.logoUrl(), command.contactInformation());
        return this.companyRepositoryPort.save(company);
    }

    @Transactional
    public Company activate(UUID companyId) {
        Company company = this.companyRepositoryPort.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        company.activate();
        return this.companyRepositoryPort.save(company);
    }

    @Transactional
    public Company deactivate(UUID companyId) {
        Company company = this.companyRepositoryPort.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
        company.deactivate();
        return this.companyRepositoryPort.save(company);
    }

    @Transactional(readOnly = true)
    public Company detail(UUID companyId) {
        return this.companyRepositoryPort.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found."));
    }

    @Transactional(readOnly = true)
    public List<Company> search(SearchCompanyQuery query) {
        return this.companyRepositoryPort.search(query.keyword(), query.page(), query.size());
    }
}
