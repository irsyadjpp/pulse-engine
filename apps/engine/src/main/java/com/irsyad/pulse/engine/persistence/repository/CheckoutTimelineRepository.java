package com.irsyad.pulse.engine.persistence.repository;

import com.irsyad.pulse.engine.persistence.entity.CheckoutTimelineEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CheckoutTimelineRepository implements PanacheRepositoryBase<CheckoutTimelineEntity, Long> {
    
    public List<CheckoutTimelineEntity> findByCheckoutIdOrderByEventTimeAsc(String checkoutId) {
        return list("checkoutId", checkoutId);
    }

    public Double getAverageProcessingTimeMs() {
        // Calculate average processing_time_ms from all timeline entries
        // This is a simple implementation - in production, you might want to filter by capability
        return getEntityManager().createQuery(
                "SELECT AVG(t.processingTimeMs) FROM CheckoutTimelineEntity t WHERE t.processingTimeMs IS NOT NULL",
                Double.class)
                .getSingleResult();
    }
}
