package com.irsyad.pulse.product.domain.company.repository;

import com.irsyad.pulse.product.domain.company.Company;
import com.irsyad.pulse.product.domain.company.valueobject.CompanyCode;
import com.irsyad.pulse.product.domain.company.valueobject.CompanyId;

import java.util.Optional;

/**
 * Domain repository interface (TSD_02 Section 19).
 */
public interface CompanyRepository {

    Company save(Company company);

    Optional<Company> findById(CompanyId id);

    Optional<Company> findByCompanyCode(CompanyCode code);
}
