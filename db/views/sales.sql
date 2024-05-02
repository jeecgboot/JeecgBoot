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

create view sales_42 as
select `poc`.`sku_id` AS `sku_id`, sum(`poc`.`quantity`) AS `quantity`
from (`wia_app`.`platform_order_content` `poc`
    join `wia_app`.`platform_order` `po`
      on ((`poc`.`platform_order_id` = `po`.`id`)))
where (`po`.`order_time` between (curdate() - interval 42 day) and curdate())
  and po.erp_status <> 5
group by `poc`.`sku_id`;