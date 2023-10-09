CREATE VIEW transaction as
SELECT id, create_by, create_time, update_by, update_time,
       type, client_id, payment_proof, invoice_number, shipping_fee, purchase_fee, amount, currency
FROM
    (
        SELECT id as id,
               create_by,
               create_time,
               update_by,
               update_time,
               'Credit' as type,
               client_id,
               payment_proof,
               NULL as invoice_number,
               NULL as shipping_fee,
               NULL as purchase_fee,
               amount as amount,
               (SELECT code FROM currency WHERE credit.currency_id = id) as currency
        FROM credit
        UNION ALL
        SELECT id                                                                   as id,
               create_by,
               create_time,
               update_by,
               update_time,
               'Debit'                                                              as type,
               client_id,
               NULL                                                                 as payment_proof,
               invoice_number,
               total_amount                                                         as shipping_fee,
               IF(invoice_number LIKE '%%%%-%%-7%%%',
                  purchase_total(invoice_number),
                  NULL)                                                            as purchase_fee,
               IF(invoice_number LIKE '%%%%-%%-7%%%',
                  total_amount + (purchase_total(invoice_number)),
                  total_amount)                                                    as amount,
               (SELECT code FROM currency WHERE shipping_invoice.currency_id = id)  as currency
        FROM shipping_invoice
        WHERE client_id IS NOT NULL
          AND shipping_invoice.currency_id IS NOT NULL
          AND shipping_invoice.currency_id <> ''
    ) as id;

-- Function that computes the total of purchase by order
CREATE FUNCTION purchase_total( invoice_number varchar(12)) RETURNS decimal(10, 2)
BEGIN
    RETURN (
        SELECT SUM(poc.purchase_fee) as total
        FROM platform_order_content as poc
                 JOIN platform_order po
                      ON po.id = poc.platform_order_id
        WHERE po.shipping_invoice_number = invoice_number
    );
END;