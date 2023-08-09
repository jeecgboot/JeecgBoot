CREATE OR REPLACE VIEW sav_refund_with_detail
AS
SELECT sr.*,
       po.platform_order_id                        AS mabang_id,
       s.erp_code,
       s.name                                      as shop_name,
       po.platform_order_number,
       po.fret_fee,
       SUM(poc.shipping_fee)                       AS shipping_fee,
       SUM(poc.vat)                                AS vat,
       po.order_service_fee + SUM(poc.service_fee) AS service_fee
FROM sav_refund sr
         JOIN platform_order po ON sr.platform_order_id = po.id
         JOIN platform_order_content poc ON po.id = poc.platform_order_id
         JOIN shop s ON po.shop_id = s.id
GROUP BY po.id;