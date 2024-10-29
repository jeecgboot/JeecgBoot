CREATE OR REPLACE VIEW sales_analyze AS
SELECT c.internal_code             AS '客户代码',
       s.name                      AS '店铺名',
       s.erp_code                  AS '店铺代码',
       po.platform_order_number    AS '订单交易号',
       po.country                  AS '国家',
       po.order_time               AS '订单交易时间',
       CAST(po.order_time AS DATE) AS '订单交易日期',
       CASE
           WHEN
               s2.erp_code IS NULL
               THEN poc.sku_id
           WHEN s2.erp_code IS NOT NULL
               THEN s2.erp_code
           END                     AS 'SKU',
       s2.zh_name                  AS '产品中文名',
       poc.quantity                AS '产品数量',
       (
       SELECT sw.weight
       FROM sku_weight sw
       WHERE sw.sku_id = s.id
       ORDER BY effective_date DESC LIMIT 1
       )                           AS '商品收费重',
       poc.purchase_fee            AS '商品采购费',
       poc.service_fee             AS '商品服务费',
       poc.shipping_fee            AS '商品运费',
       po.fret_fee                 AS '包裹挂号费',
       CASE
           WHEN po.erp_status = '1'
               THEN '待处理'
           WHEN po.erp_status = '2'
               THEN '配货中'
           WHEN po.erp_status = '3'
               THEN '已发货'
           WHEN po.erp_status = '4'
               THEN '已完成'
           WHEN po.erp_status = '5'
               THEN '已作废'
           END                     AS '订单状态',
       po.logistic_channel_name    AS '物流渠道'
FROM platform_order_content poc
         LEFT JOIN sku s2 ON poc.sku_id = s2.id
         JOIN platform_order po ON poc.platform_order_id = po.id
         JOIN shop s ON po.shop_id = s.id
         JOIN client c ON s.owner_id = c.id
ORDER BY order_time;