CREATE OR REPLACE VIEW platform_order_logistic_expense_detail AS
SELECT s.erp_code                                  AS 'shopErpCode',
       po.tracking_number                          AS 'trackingNumber',
       po.shop_id,
       po.logistic_channel_name                    AS 'logisticChannelName',
       po.platform_order_id                        AS 'platformOrderId',
       po.platform_order_number                    AS 'platformOrderNumber',
       po.order_time                               AS 'orderTime',
       po.shipping_time                            AS 'shippingTime',
       po.country,
       po.fret_fee                                 AS 'fretFee',
       SUM(poc.shipping_fee)                       AS 'shippingFee',
       SUM(poc.vat)                                AS 'vatFee',
       po.order_service_fee + SUM(poc.service_fee) AS 'serviceFee',
       po.shipping_invoice_number                  AS 'shippingInvoiceNumber',
       fled.real_weight,
       fled.volumetric_weight,
       fled.charging_weight,
       fled.discount,
       fled.shipping_fee                           AS 'realShippingFee',
       fled.fuel_surcharge,
       fled.registration_fee,
       fled.second_delivery_fee,
       fled.vat,
       fled.vat_service_fee,
       fled.total_fee,
       fled.logistic_company_id,
       fled.additional_fee
FROM full_logistic_expense_detail fled
         RIGHT JOIN platform_order po ON fled.trackingNumber = po.tracking_number
         JOIN shop s ON po.shop_id = s.id
         JOIN platform_order_content poc ON po.id = poc.platform_order_id
WHERE po.erp_status IN (3, 4)
GROUP BY po.id, s.erp_code
ORDER BY s.erp_code;