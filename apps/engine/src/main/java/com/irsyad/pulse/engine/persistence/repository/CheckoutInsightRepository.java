package com.irsyad.pulse.engine.persistence.repository;

import com.irsyad.pulse.engine.persistence.entity.CheckoutInsightEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CheckoutInsightRepository implements PanacheRepositoryBase<CheckoutInsightEntity, String> {
    
    public long countAll() {
        return count("*");
    }

    public long countByDecision(String decision) {
        return count("decision", decision);
    }

    public CheckoutInsightEntity findByCheckoutId(String checkoutId) {
        return find("checkoutId", checkoutId).firstResult();
    }

    public boolean existsByEventId(String eventId) {
        return count("eventId", eventId) > 0;
    }
}
