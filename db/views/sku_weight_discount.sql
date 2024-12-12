CREATE OR REPLACE VIEW sku_weight_discount_service_fees AS
SELECT s.id,
       s.erp_code,
       sw.weight,
       s.shipping_discount,
       s.service_fee
FROM sku_weight sw inner join (select sku_id, max(effective_date) max_date from sku_weight group by sku_id) sw2
    on sw.sku_id = sw2.sku_id and sw.effective_date = sw2.max_date
join sku s on sw.sku_id = s.id