package com.irsyad.pulse.engine.persistence.repository;

import com.irsyad.pulse.engine.persistence.entity.CheckoutExplanationEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CheckoutExplanationRepository implements PanacheRepositoryBase<CheckoutExplanationEntity, Long> {
    
    public List<CheckoutExplanationEntity> findByCheckoutId(String checkoutId) {
        return list("checkoutId", checkoutId);
    }
}