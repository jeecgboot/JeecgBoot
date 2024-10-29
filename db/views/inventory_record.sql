CREATE OR REPLACE VIEW inventory_record AS
SELECT sku.id               AS id,
       cs.client_id         AS client_id,
       sku.id               AS product_id,
       sku.erp_code         AS erp_code,
       sku.image_source     AS image_source,
       sku.available_amount AS available_amount,
       sku.moq              AS moq,
       rs.quantity          AS red_quantity,
       gs.quantity          AS green_quantity,
       sales_7.quantity     AS sales_7,
       sales_14.quantity    AS sales_14,
       sales_28.quantity    AS sales_28,
       sipo.quantity        AS platform_order_quantity
FROM sku
         JOIN client_sku cs ON sku.id = cs.sku_id
         LEFT JOIN sales_7 ON sku.id = sales_7.sku_id
         LEFT JOIN sales_14 ON sku.id = sales_14.sku_id
         LEFT JOIN sales_28 ON sku.id = sales_28.sku_id
         LEFT JOIN red_sku rs ON sku.id = rs.sku_id
         LEFT JOIN green_sku gs ON sku.id = gs.sku_id
         LEFT JOIN sku_in_platform_order sipo ON sku.id = sipo.sku_id
ORDER BY platform_order_quantity DESC;

