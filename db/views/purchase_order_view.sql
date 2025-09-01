CREATE OR REPLACE VIEW purchase_order_view AS
SELECT
    po.*,
    GROUP_CONCAT(p.platform_order_id) AS platformOrderId
FROM
    purchase_order po
LEFT JOIN
    platform_order p ON p.purchase_invoice_number = po.invoice_number
WHERE
    po.create_time >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR)
  AND po.status = 1
GROUP BY po.id, po.create_time;

