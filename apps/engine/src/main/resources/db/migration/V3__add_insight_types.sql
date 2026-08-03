-- Add insight_type column to checkout_insight
ALTER TABLE pulse_engine.checkout_insight 
ADD COLUMN IF NOT EXISTS insight_type VARCHAR(50);

-- Add index for insight_type queries
CREATE INDEX IF NOT EXISTS idx_checkout_insight_type 
ON pulse_engine.checkout_insight(insight_type);

-- Insert insight types for existing records based on decision/amount
UPDATE pulse_engine.checkout_insight 
SET insight_type = 'HIGH_AMOUNT' 
WHERE total_amount > 100000000 AND insight_type IS NULL;

UPDATE pulse_engine.checkout_insight 
SET insight_type = 'CUSTOMER_FIRST_PURCHASE' 
WHERE insight_type IS NULL AND decision = 'APPROVE'
  AND checkout_id IN (
    SELECT checkout_id FROM pulse_engine.checkout_explanation 
    WHERE explanation LIKE '%first purchase%'
  );

UPDATE pulse_engine.checkout_insight 
SET insight_type = 'PAYMENT_RETRY' 
WHERE insight_type IS NULL AND decision = 'REVIEW'
  AND checkout_id IN (
    SELECT checkout_id FROM pulse_engine.checkout_explanation 
    WHERE explanation LIKE '%retry%'
  );

UPDATE pulse_engine.checkout_insight 
SET insight_type = 'HIGH_FRAUD_RISK' 
WHERE insight_type IS NULL AND decision = 'REJECT'
  AND risk_level = 'HIGH';

UPDATE pulse_engine.checkout_insight 
SET insight_type = 'VELOCITY_ANOMALY' 
WHERE insight_type IS NULL 
  AND checkout_id IN (
    SELECT checkout_id FROM pulse_engine.checkout_explanation 
    WHERE explanation LIKE '%velocity%'
  );

UPDATE pulse_engine.checkout_insight 
SET insight_type = 'PAYMENT_METHOD_RISK' 
WHERE insight_type IS NULL 
  AND checkout_id IN (
    SELECT checkout_id FROM pulse_engine.checkout_explanation 
    WHERE explanation LIKE '%payment method%'
  );

UPDATE pulse_engine.checkout_insight 
SET insight_type = 'CUSTOMER_BEHAVIOR' 
WHERE insight_type IS NULL;
