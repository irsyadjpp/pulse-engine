package com.irsyad.pulse.engine.persistence.repository;

import com.irsyad.pulse.engine.persistence.entity.CheckoutInsightEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Search insights with optional filters and pagination using idiomatic Panache.
     *
     * @param checkoutId optional partial match on checkout id
     * @param customerId optional partial match on customer id
     * @param insightType optional exact match on insight type (severity)
     * @param decision    optional exact match on decision (type)
     * @param page        zero-based page number
     * @param size        page size
     * @return matching entities ordered by processedAt DESC
     */
    public List<CheckoutInsightEntity> search(String checkoutId, String customerId,
            String insightType, String decision, int page, int size) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (checkoutId != null && !checkoutId.isEmpty()) {
            query.append(" AND checkoutId LIKE :checkoutId");
            params.put("checkoutId", "%" + checkoutId + "%");
        }
        if (customerId != null && !customerId.isEmpty()) {
            query.append(" AND customerId LIKE :customerId");
            params.put("customerId", "%" + customerId + "%");
        }
        if (insightType != null && !insightType.isEmpty()) {
            query.append(" AND insightType = :insightType");
            params.put("insightType", insightType);
        }
        if (decision != null && !decision.isEmpty()) {
            query.append(" AND decision = :decision");
            params.put("decision", decision);
        }

        PanacheQuery<CheckoutInsightEntity> panacheQuery = params.isEmpty()
                ? find(query.toString())
                : find(query.toString(), params);

        return panacheQuery
                .page(Page.of(page, size))
                .list();
    }
}
