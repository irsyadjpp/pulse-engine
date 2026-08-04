package com.irsyad.pulse.product.application.port;

import com.irsyad.pulse.product.domain.company.Company;
import com.irsyad.pulse.product.domain.shared.CompanyStatus;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Company persistence (Hexagonal Architecture).
 * Implemented by the JPA adapter in infrastructure layer.
 */
public interface CompanyRepositoryPort {

    Company save(Company company);

    Optional<Company> findById(UUID companyId);

    Optional<Company> findByCompanyCode(String companyCode);

    Page<Company> search(String keyword, CompanyStatus status, String sort, int page, int size);
}
