package com.irsyad.pulse.engine.persistence.repository;

import com.irsyad.pulse.engine.persistence.entity.CustomerLearningEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerLearningRepository implements PanacheRepositoryBase<CustomerLearningEntity, String> {
    // No custom methods needed, using PanacheRepositoryBase methods
}
