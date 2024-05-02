CREATE VIEW all_invoices AS
WITH shipping AS (
    SELECT
        s.id AS id,
        s.create_by AS create_by,
        s.create_time AS create_time,
        s.client_id AS client_id,
        s.currency_id AS currency_id,
        s.invoice_number AS invoice_number,
        IFNULL(s.total_amount + p.total_amount, s.total_amount) AS total_amount,
        IFNULL(s.discount_amount + p.discount_amount, s.discount_amount) AS discount_amount,
        IFNULL(s.final_amount + p.final_amount, s.final_amount) AS final_amount,
        IFNULL(s.paid_amount + p.paid_amount, s.paid_amount) AS paid_amount,
        IF(SUBSTRING(s.invoice_number,9,1) = '2', 'shipping', 'complete') AS 'type'
    FROM shipping_invoice s
             LEFT JOIN purchase_order p ON s.invoice_number = p.invoice_number
        AND s.client_id = p.client_id
),
     purchase AS (
         SELECT
             p.id AS id,
             p.create_by AS create_by,
             p.create_time AS create_time,
             p.client_id AS client_id,
             p.currency_id AS currency_id,
             p.invoice_number AS invoice_number,
             p.total_amount AS total_amount,
             p.discount_amount AS discount_amount,
             p.final_amount AS final_amount,
             p.paid_amount AS paid_amount,
             IF(SUBSTRING(p.invoice_number,9,1) = '1', 'purchase', 'error') AS 'type'
         FROM purchase_order p
         WHERE p.invoice_number NOT IN (SELECT invoice_number FROM shipping_invoice)
     )
SELECT s.* FROM shipping s
UNION ALL SELECT p.* FROM purchase p
ORDER BY create_time DESC;