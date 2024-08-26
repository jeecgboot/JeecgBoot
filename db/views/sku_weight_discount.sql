CREATE OR REPLACE VIEW sku_weight_discount_service_fees AS
SELECT s.id,
       s.erp_code,
       (SELECT sw.weight FROM sku_weight sw WHERE sw.sku_id = s.id ORDER BY effective_date DESC LIMIT 1) as weight,
       s.shipping_discount,
       s.service_fee
FROM sku s