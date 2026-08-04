package com.irsyad.pulse.product.domain.company;

import com.irsyad.pulse.product.domain.shared.CompanyStatus;
import com.irsyad.pulse.product.domain.shared.DomainEvent;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Insurance Company aggregate root.
 *
 * <p>Referenced from FSD_01 (Section 12 Data Model). A Company owns zero or
 * more Products (BR-007). Company Code is immutable after creation.
 */
@Getter
@Builder
public class Company {

    private final UUID companyId;
    private final String companyCode;
    private String companyName;
    private String logoUrl;
    private String contactInformation;
    private CompanyStatus status;
    private final Instant createdAt;
    private final String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private long version;
    private boolean deleted;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public void updateProfile(String companyName, String logoUrl, String contactInformation) {
        this.companyName = companyName;
        this.logoUrl = logoUrl;
        this.contactInformation = contactInformation;
        this.updatedAt = Instant.now();
        this.record(com.irsyad.pulse.product.domain.shared.CompanyUpdatedEvent.of(this.companyId));
    }

    public void activate() {
        this.status = CompanyStatus.ACTIVE;
        this.updatedAt = Instant.now();
        this.record(com.irsyad.pulse.product.domain.shared.CompanyActivatedEvent.of(this.companyId));
    }

    public void deactivate() {
        this.status = CompanyStatus.INACTIVE;
        this.updatedAt = Instant.now();
        this.record(com.irsyad.pulse.product.domain.shared.CompanyDeactivatedEvent.of(this.companyId));
    }

    protected void record(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }
}
