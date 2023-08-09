CREATE OR REPLACE VIEW logistic_profit_analyze AS
SELECT c.internal_code                                       AS '客户',
       shopErpCode                                           AS '店铺',
       lc.name                                               AS '物流公司',
       logisticChannelName                                   AS '物流路线',
       platformOrderId                                       AS '订单号',
       platformOrderNumber                                   AS '交易号',
       orderTime                                             AS '交易时间',
       shippingTime                                          AS '发货时间',
       poled.country                                         AS '国家',
       fretFee                                               AS '应收挂号费（欧元）',
       registration_fee                                      AS '实付挂号费（人民币）',
       (fretFee * 7.6 - registration_fee)                    AS '挂号费利润（人民币）',
       shippingFee                                           AS '应收运费（欧元）',
       realShippingFee                                       AS '实付运费（人民币）',
       (shippingFee * 7.6 - realShippingFee)                 AS '运费利润（人民币）',
       vatFee                                                AS '应收增值税（欧元）',
       (vat + vat_service_fee)                               AS '实付增值税（人民币）',
       (vatFee * 7.6 - vat - vat_service_fee)                AS '增值税利润（人民币）',
       serviceFee                                            AS '服务费（欧元）',
       serviceFee * 7.6                                      AS '服务费（人民币）',
       (fretFee * 7.6 - registration_fee + shippingFee * 7.6 -
        realShippingFee + vat * 7.6 - vat - vat_service_fee) AS '服务费外总利润（人民币）'

FROM platform_order_logistic_expense_detail poled
         JOIN shop s
              ON shop_id = s.id
         JOIN CLIENT c ON s.owner_id = c.id
         JOIN logistic_company lc ON poled.logistic_company_id = lc.id;