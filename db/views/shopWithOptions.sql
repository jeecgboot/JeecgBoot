CREATE OR REPLACE VIEW shop_with_options AS
SELECT so.id as id,
       c.id as client_id,
       c.internal_code as internal_code,
       c.active as is_client_active,
       s.id as shop_id,
       s.erp_code as erp_code,
       s.active as is_shop_active,
       so.id as shop_options_id,
       so.is_auto_invoice,
       so.is_breakdown_invoice,
       so.is_complete_invoice,
       so.show_unassigned_logistics_orders,
       so.can_self_invoice,
       so.can_self_p,
       so.can_self_l,
       so.can_self_p_l,
       so.is_self_ignore_stock,
       so.has_stock,
       so.has_shipping_invoice_remark,
       IF(so.id IS NULL, 0, 1) AS has_options
FROM shop s
LEFT JOIN shop_options so ON s.id = so.shop_id
JOIN client c ON s.owner_id = c.id
GROUP BY c.id, c.internal_code, s.erp_code
ORDER BY c.internal_code,c.id, s.erp_code;

