CREATE OR REPLACE VIEW transaction as
SELECT combined.id,
       combined.create_by,
       combined.create_time,
       combined.update_by,
       combined.update_time,
       type,
       client_id,
       payment_proof,
       invoice_number,
       shipping_fee,
       purchase_fee,
       amount,
       currency.code                                                                AS currency
FROM (
         SELECT id,
                create_by,
                create_time,
                update_by,
                update_time,
                'Credit'                                                               AS type,
                client_id,
                payment_proof,
                NULL                                                                   AS invoice_number,
                NULL                                                                   AS shipping_fee,
                NULL                                                                   AS purchase_fee,
                amount,
                currency_id
         FROM credit
         UNION ALL
         SELECT id,
                create_by,
                create_time,
                update_by,
                update_time,
                'Debit'                                                                AS type,
                client_id,
                NULL                                                                   AS payment_proof,
                invoice_number,
                total_amount                                                           AS shipping_fee,
                pt.total                                                               AS purchase_fee,
                COALESCE(pt.total + si.total_amount, si.total_amount)                  AS amount,
                currency_id
         FROM shipping_invoice si
         LEFT JOIN (
             SELECT po.shipping_invoice_number, SUM(poc.purchase_fee) AS total
             FROM platform_order_content poc
                  JOIN platform_order po ON po.id = poc.platform_order_id
             WHERE po.shipping_invoice_number LIKE '%-%-7%'
             GROUP BY po.shipping_invoice_number
         ) pt ON pt.shipping_invoice_number = si.invoice_number
         WHERE client_id IS NOT NULL
           AND currency_id IS NOT NULL
           AND currency_id <> ''
         UNION ALL
         SELECT id,
                create_by,
                create_time,
                update_by,
                update_time,
                'Debit'                                                                AS type,
                client_id,
                payment_document                                                       AS payment_proof,
                invoice_number,
                NULL                                                                   AS shipping_fee,
                final_amount                                                           AS purchase_fee,
                final_amount                                                           AS amount,
                currency_id
         FROM purchase_order
         WHERE invoice_number LIKE '%-%-1%'
           AND client_id IS NOT NULL
           AND currency_id IS NOT NULL
           AND currency_id <> ''
     ) as combined
LEFT JOIN currency ON combined.currency_id = currency.id