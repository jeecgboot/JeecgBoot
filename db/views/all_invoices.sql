CREATE OR REPLACE VIEW all_invoices AS
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
        COALESCE(s.payment_approved, FALSE) AS payment_approved,
        COALESCE(s.payment_document, p.payment_document) AS payment_document,
        IF(COALESCE(s.paid_amount, p.paid_amount, 0) > 0, TRUE, FALSE) AS paid,
        CASE
            WHEN COALESCE(s.paid_amount, p.paid_amount, 0) > 0 THEN 'paid'
            WHEN COALESCE(s.payment_approved, FALSE) THEN 'approved'
            WHEN COALESCE(s.payment_document, p.payment_document) IS NOT NULL THEN 'proof_uploaded'
            ELSE 'pending'
        END AS payment_status,
        IF(SUBSTRING(s.invoice_number, 9, 1) = '2', 'shipping', 'complete') AS 'type',
        s.status AS status
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
        COALESCE(p.payment_approved, FALSE) AS payment_approved,
        p.payment_document AS payment_document,
        IF(COALESCE(p.paid_amount, 0) > 0, TRUE, FALSE) AS paid,
        CASE
            WHEN COALESCE(p.paid_amount, 0) > 0 THEN 'paid'
            WHEN COALESCE(p.payment_approved, FALSE) THEN 'approved'
            WHEN p.payment_document IS NOT NULL THEN 'proof_uploaded'
            ELSE 'pending'
        END AS payment_status,
        IF(SUBSTRING(p.invoice_number, 9, 1) = '1', 'purchase', 'error') AS 'type',
        p.status AS status
    FROM purchase_order p
    WHERE p.invoice_number NOT IN (SELECT invoice_number FROM shipping_invoice)
)
SELECT s.* FROM shipping s
UNION ALL SELECT p.* FROM purchase p
ORDER BY create_time DESC;