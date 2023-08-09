CREATE OR REPLACE VIEW logistic_fees_by_country AS
SELECT s.name                                     AS '店铺',
       po.country                                 AS '国家',
       SUM(po.fret_fee)                           AS '收取挂号费',
       SUM(led.registration_fee)                  AS '实际支付挂号费',
       SUM(poc.shipping_fee)                      AS '收取运费',
       SUM(led.total_fee) - SUM(registration_fee) AS '实际支付运费',
       SUM(poc.vat)                               AS '收取TVA',
       SUM(led.vat) + SUM(led.vat_service_fee)    AS '实际支付TVA'
FROM platform_order po
         JOIN shop s ON po.shop_id = s.id
         RIGHT JOIN platform_order_content poc ON po.id = poc.platform_order_id
         JOIN logistic_expense_detail led ON po.tracking_number = led.tracking_number
WHERE shipping_invoice_number IS NOT NULL
GROUP BY s.name, po.country
ORDER BY s.name