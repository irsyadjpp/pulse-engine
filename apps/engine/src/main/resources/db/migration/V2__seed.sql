-- ============================================================================
-- Pulse Engine — Seed Data
-- Sample data for the 4 main tables.
-- ============================================================================

-- 1. checkout_insight
INSERT INTO pulse_engine.checkout_insight
    (checkout_id, process_id, customer_id, order_id, decision, confidence, risk_level, explainability_score, total_amount, processed_at)
VALUES
    ('CHK-001', 'PROC-001', 'CUST-001', 'ORD-001', 'APPROVED', 'HIGH', 'LOW', 0.95, 2500000, NOW()),
    ('CHK-002', 'PROC-002', 'CUST-002', 'ORD-002', 'REVIEW',   'MEDIUM', 'MEDIUM', 0.72, 8500000, NOW()),
    ('CHK-003', 'PROC-003', 'CUST-003', 'ORD-003', 'REJECTED', 'LOW', 'HIGH', 0.45, 15000000, NOW());

-- 2. checkout_timeline
INSERT INTO pulse_engine.checkout_timeline
    (checkout_id, capability, status, message, processing_time_ms, event_time)
VALUES
    ('CHK-001', 'OBSERVE', 'SUCCESS', 'Event consumed from Kafka', 12, NOW()),
    ('CHK-001', 'UNDERSTAND', 'SUCCESS', 'Context extracted', 18, NOW()),
    ('CHK-001', 'EXPLAIN', 'SUCCESS', 'Explanation generated', 8, NOW()),
    ('CHK-001', 'LEARN', 'SUCCESS', 'Pattern updated', 25, NOW()),
    ('CHK-001', 'PERSIST', 'SUCCESS', 'Data persisted', 15, NOW()),
    ('CHK-001', 'PUBLISH', 'SUCCESS', 'Insight published to Kafka', 5, NOW());

-- 3. checkout_explanation
INSERT INTO pulse_engine.checkout_explanation
    (checkout_id, explanation_type, explanation)
VALUES
    ('CHK-001', 'CUSTOMER', 'VIP Customer'),
    ('CHK-001', 'PAYMENT', 'Payment Success'),
    ('CHK-001', 'RISK', 'Low Risk'),
    ('CHK-001', 'DECISION', 'Approved because customer is trusted');

-- 4. customer_learning
INSERT INTO pulse_engine.customer_learning
    (customer_id, purchase_count, successful_checkout, rejected_checkout, average_amount, highest_amount, preferred_payment_method, customer_segment, last_checkout_time)
VALUES
    ('CUST-001', 24, 23, 1, 2500000, 8500000, 'CREDIT_CARD', 'VIP', NOW());
