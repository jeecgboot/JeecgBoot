CREATE OR REPLACE VIEW sku_price_promotion AS
SELECT s.id                     AS sku_id,
       s.en_name                AS name_en,
       s.zh_name                AS name_zh,
       s.erp_code               AS erp_code,
       s.image_source           AS image_source,
       spr.promotion_id         AS promotion_id,
       spr.promo_milestone      AS promo_milestone,
       spr.quantity_purchased   AS quantity_purchased,
       spr.discount             AS discount,
       scp.price_id             AS price_id,
       scp.price                AS price,
       scp.threshold            AS threshold,
       scp.discounted_price     AS discounted_price,
       scp.currency_id          AS currency_id
FROM sku s
         LEFT JOIN sku_promotion_relation spr ON s.id = spr.sku_id
         LEFT JOIN sku_current_price scp ON s.id = scp.sku_id
         LEFT JOIN sku_in_platform_order sipo ON s.id = sipo.sku_id
ORDER BY sipo.quantity DESC;