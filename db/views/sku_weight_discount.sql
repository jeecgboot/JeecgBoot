CREATE OR REPLACE VIEW sku_weight_discount_service_fees AS
SELECT s.id,
       s.erp_code,
       p.weight,
       s.shipping_discount,
       s.service_fee
FROM sku s JOIN product p ON p.id = s.product_id;