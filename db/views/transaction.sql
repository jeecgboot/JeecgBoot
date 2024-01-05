CREATE VIEW transaction as
SELECT id                                                                           AS id,
       create_by                                                                    AS create_by,
       create_time                                                                  AS create_time,
       update_by                                                                    AS update_by,
       update_time                                                                  AS update_time,
       type                                                                         AS type,
       client_id                                                                    AS client_id,
       payment_proof                                                                AS payment_proof,
       invoice_number                                                               AS invoice_number,
       shipping_fee                                                                 AS shipping_fee,
       purchase_fee                                                                 AS purchase_fee,
       amount                                                                       AS amount,
       currency                                                                     AS currency
FROM (
         SELECT id                                                                     AS id,
                create_by                                                              AS create_by,
                create_time                                                            AS create_time,
                update_by                                                              AS update_by,
                update_time                                                            AS update_time,
                'Credit'                                                               AS type,
                client_id                                                              AS client_id,
                payment_proof                                                          AS payment_proof,
                NULL                                                                   AS invoice_number,
                NULL                                                                   AS shipping_fee,
                NULL                                                                   AS purchase_fee,
                amount                                                                 AS amount,
                (SELECT code FROM currency WHERE credit.currency_id = id)              AS currency
         FROM credit
         UNION ALL
         SELECT id                                                                     AS id,
                create_by                                                              AS create_by,
                create_time                                                            AS create_time,
                update_by                                                              AS update_by,
                update_time                                                            AS update_time,
                'Debit'                                                                AS type,
                client_id                                                              AS client_id,
                NULL                                                                   AS payment_proof,
                invoice_number                                                         AS invoice_number,
                total_amount                                                           AS shipping_fee,
                if((invoice_number like '%%%%-%%-7%%%'),
                   purchase_total(invoice_number), NULL)                  AS purchase_fee,
                if((invoice_number like '%%%%-%%-7%%%'),
                   (total_amount +
                    purchase_total(invoice_number)),
                   total_amount)                                                       AS amount,
                (SELECT code FROM currency WHERE shipping_invoice.currency_id = id)    AS currency
         FROM shipping_invoice
         WHERE client_id IS NOT NULL
           AND currency_id IS NOT NULL
           AND currency_id <> ''
         UNION ALL
         SELECT id                                                                     AS id,
                create_by                                                              as create_by,
                create_time                                                            as create_time,
                update_by                                                              as update_by,
                update_time                                                            as update_time,
                'Debit'                                                                AS type,
                client_id                                                              AS client_id,
                payment_document                                                       AS payment_proof,
                invoice_number                                                         AS invoice_number,
                NULL                                                                   AS shipping_fee,
                final_amount                                                           AS purchase_fee,
                final_amount                                                           AS amount,
                (SELECT code FROM currency WHERE purchase_order.currency_id = id)      AS currency
         FROM purchase_order
         WHERE invoice_number LIKE '%%%%-%%-1%%%'
           AND client_id IS NOT NULL
           AND currency_id IS NOT NULL
           AND currency_id <> ''
     ) as id;