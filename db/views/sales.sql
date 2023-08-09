CREATE OR REPLACE VIEW sales_7 AS
SELECT poc.sku_id AS sku_id, SUM(poc.quantity) AS quantity
FROM platform_order_content poc
         JOIN platform_order po ON poc.platform_order_id = po.id
WHERE po.order_time BETWEEN DATE_SUB(CURDATE(), INTERVAL 7 DAY) AND CURDATE()
GROUP BY poc.sku_id;

CREATE OR REPLACE VIEW sales_14 AS
SELECT poc.sku_id AS sku_id, SUM(poc.quantity) AS quantity
FROM platform_order_content poc
         JOIN platform_order po ON poc.platform_order_id = po.id
WHERE po.order_time BETWEEN DATE_SUB(CURDATE(), INTERVAL 14 DAY) AND CURDATE()
GROUP BY poc.sku_id;

CREATE OR REPLACE VIEW sales_28 AS
SELECT poc.sku_id AS sku_id, SUM(poc.quantity) AS quantity
FROM platform_order_content poc
         JOIN platform_order po ON poc.platform_order_id = po.id
WHERE po.order_time BETWEEN DATE_SUB(CURDATE(), INTERVAL 28 DAY) AND CURDATE()
GROUP BY poc.sku_id;