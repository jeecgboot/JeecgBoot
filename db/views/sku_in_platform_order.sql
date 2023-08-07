CREATE OR REPLACE VIEW sku_in_platform_order AS
SELECT poc.sku_id AS sku_id, SUM(poc.quantity) AS quantity
FROM platform_order_content poc join platform_order po ON poc.platform_order_id = po.id
WHERE po.status = 2
GROUP BY poc.sku_id;