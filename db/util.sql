SELECT DISTINCT sku_id FROM platform_order_content WHERE sku_id not LIKE '1%';

UPDATE platform_order
SET fret_fee                = NULL,
    shipping_invoice_number = NULL
WHERE shipping_invoice_number IS NOT NULL;
UPDATE platform_order_content
SET shipping_fee = NULL,
    service_fee  = NULL,
    vat          = NULL
WHERE vat IS NOT NULL;

DELETE from platform_order_content WHERE sku_id is NULL;


SELECT @@character_set_database, @@collation_database;
SHOW VARIABLES LIKE 'collation%';
SHOW TABLE STATUS LIKE 'sen%';
ALTER DATABASE wia_app COLLATE utf8mb4_general_ci;

WITH latestSkuWeights AS (
    SELECT
        sku_id,
        weight,
        effective_date,
        ROW_NUMBER() OVER (PARTITION BY sku_id ORDER BY effective_date DESC) AS rn
    FROM sku_weight
)
SELECT c.internal_code                                      AS '客户',
        s.erp_code                                          AS SKU,
        s.zh_name                                           AS '中文名',
        lsw.weight                                           AS '重量',
        ROUND(calculate_shipping_fees(IF(sa.zh_name = '普货', '联邮通优先挂号-普货', '联邮通优先挂号-带电'), 'FR', '2021-06-24',
                                      lsw.weight), 2) AS '运费',
        get_registration_fees(IF(sa.zh_name = '普货', '联邮通优先挂号-普货', '联邮通优先挂号-带电'), 'FR', '2021-06-24',
            lsw.weight)                               AS '挂号费'
FROM sku s
         LEFT JOIN client_sku ON s.id = client_sku.sku_id
         LEFT JOIN client c ON client_sku.client_id = c.id
         LEFT JOIN latestSkuWeights lsw ON lsw.sku_id = s.id AND lsw.rn = 1
         JOIN sensitive_attribute sa ON s.sensitive_attribute_id = sa.id;

SELECT c.internal_code                                AS 'Client',
        po.platform_order_id                           AS 'Order ID',
        po.logistic_channel_name                       AS 'Logistic Channel',
        po.platform_order_number                       AS 'Order Number',
        po.order_time                                  AS 'Order Time',
        po.shipping_time                               AS 'Shipping Time',
        po.country                                     AS 'Country',
        IF(s.erp_code IS NULL, poc.sku_id, s.erp_code) AS 'SKU',
        poc.quantity                                   AS 'Quantity',
        po.fret_fee                                    AS 'Fret Fee',
        (SELECT SUM(poc.shipping_fee)
WHERE poc.platform_order_id = po.id)          AS 'Shipping Fee',
    poc.service_fee                                AS 'Service Fee',
    po.status                                      AS 'Status'
FROM platform_order po
    JOIN platform_order_content poc ON po.id = poc.platform_order_id
    LEFT JOIN shop ON po.shop_id = shop.id
    LEFT JOIN client c ON shop.owner_id = c.id
    LEFT JOIN sku s ON poc.sku_id = s.id
WHERE po.erp_status = 3
ORDER BY Client;

SELECT json_array(poc.shipping_fee)
from platform_order_content poc JOIN platform_order po ON po.id = poc.platform_order_id
WHERE poc.platform_order_id = po.id;

SELECT s.erp_code, count(DISTINCT po.id), sum(poc.quantity)
FROM platform_order po
         JOIN platform_order_content poc ON po.id = poc.platform_order_id
         JOIN shop s ON po.shop_id = s.id
WHERE shipping_invoice_number IS not NULL
  AND po.erp_status = '3'
GROUP BY erp_code
ORDER BY erp_code;

SELECT s.erp_code, po.*
FROM platform_order po
         JOIN platform_order_content poc ON po.id = poc.platform_order_id
         JOIN shop s ON po.shop_id = s.id
WHERE shipping_invoice_number IS NULL
  AND po.erp_status = '3' and (erp_code = 'EP5' OR erp_code ='EP6')
ORDER BY erp_code;