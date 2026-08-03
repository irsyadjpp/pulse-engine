package com.irsyad.pulse.product.domain.shared;

/**
 * Business status for an Insurance Company.
 *
 * <p>Referenced from FSD_01 (Section 13 State Machine) and Appendix K.
 * Only ACTIVE and INACTIVE are supported per BRD.
 */
public enum CompanyStatus {

    ACTIVE,
    INACTIVE
}