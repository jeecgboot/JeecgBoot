CREATE OR REPLACE VIEW sku_current_price AS
SELECT id                   AS price_id,
       sp.sku_id            AS sku_id,
       price                AS price,
       threshold            AS threshold,
       discounted_price     AS discounted_price,
       price_rmb            AS price_rmb,
       discounted_price_rmb AS discounted_price_rmb,
       date                 AS date
FROM sku_price sp
         INNER JOIN (SELECT sku_id, MAX(date) max_date FROM sku_price GROUP BY sku_id) sp2
                    ON sp.sku_id = sp2.sku_id AND sp.date = sp2.max_date