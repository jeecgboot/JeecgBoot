CREATE OR REPLACE VIEW payment_proof_accountant_view AS
WITH
    -- PLATFORM IDS BY SHIPPING INVOICE (erp_status IN 1/2)
    po_ship_active AS (
        SELECT
            shipping_invoice_number AS inv_no,
            GROUP_CONCAT(DISTINCT platform_order_id ORDER BY platform_order_id SEPARATOR ',') AS platformOrderId
        FROM platform_order
        WHERE erp_status IN ('1','2')
          AND shipping_invoice_number IS NOT NULL
          AND TRIM(shipping_invoice_number) <> ''
        GROUP BY shipping_invoice_number
    ),

    -- PLATFORM IDS BY PURCHASE INVOICE (erp_status IN 1/2, NOT NULL)
    purchase_active AS (
        SELECT
            purchase_invoice_number AS po_no,
            GROUP_CONCAT(DISTINCT platform_order_id ORDER BY platform_order_id SEPARATOR ',') AS po_platform_ids
        FROM platform_order
        WHERE erp_status IN ('1','2')
          AND purchase_invoice_number IS NOT NULL
          AND TRIM(purchase_invoice_number) <> ''
        GROUP BY purchase_invoice_number
    ),

    /* ---------- SHIPPING ---------- */

    -- STRICT SHIPPING SET: status=1, within 1y, (paid>0 OR has proof)
    si_base_strict AS (
        SELECT
            si.id, si.create_by, si.create_time, si.update_by, si.update_time,
            si.invoice_number, si.client_id, si.currency_id,
            si.total_amount, si.discount_amount, si.final_amount,
            si.payment_document, si.status,
            LEFT(SUBSTRING_INDEX(si.invoice_number,'-',-1),1) AS ticket_prefix,
            a.platformOrderId,
            si.payment_approved AS payment_approved
        FROM shipping_invoice si
                 LEFT JOIN po_ship_active a ON a.inv_no = si.invoice_number
        WHERE si.status = 1
          AND si.create_time >= DATE_SUB(NOW(), INTERVAL 1 YEAR)
          AND (
                si.payment_approved = 1
                OR (si.payment_document IS NOT NULL AND si.payment_document <> '')
            )
    ),

    -- RELAXED SHIPPING SUM: status=1, within 1y (for PO merge only)
    si_sum_relaxed AS (
        SELECT
            si.invoice_number,
            SUM(si.total_amount) AS siTotalAmount_any,
            SUM(si.final_amount) AS siFinalAmount_any
        FROM shipping_invoice si
        WHERE si.status = 1
          AND si.create_time >= DATE_SUB(NOW(), INTERVAL 1 YEAR)
        GROUP BY si.invoice_number
    ),

    -- STRICT SHIPPING SUM (preferred for merge)
    si_sum_strict AS (
        SELECT
            invoice_number,
            SUM(total_amount) AS siTotalAmount,
            SUM(final_amount) AS siFinalAmount
        FROM si_base_strict
        GROUP BY invoice_number
    ),

    /* ---------- PURCHASE ---------- */

    -- PO BASE: ticket 1/7, not ordered, within 1y, (paid>0 OR has proof), status=1
    po_base AS (
        SELECT
            po.id, po.create_by, po.create_time, po.update_by, po.update_time,
            po.invoice_number, po.client_id, po.currency_id,
            po.total_amount, po.discount_amount, po.final_amount,
            po.payment_document, po.inventory_document, po.ordered, po.group_id,
            po.status,
            LEFT(SUBSTRING_INDEX(po.invoice_number,'-',-1),1) AS ticket_prefix,
            po.payment_approved AS payment_approved
        FROM purchase_order po
        WHERE po.ordered = 0
          AND po.create_time >= DATE_SUB(NOW(), INTERVAL 1 YEAR)
          AND LEFT(SUBSTRING_INDEX(po.invoice_number,'-',-1),1) IN ('1','7')
          AND (po.payment_approved = 1 OR (po.payment_document IS NOT NULL AND po.payment_document <> ''))
          AND po.status = 1
    ),

    -- SHIPPING-ONLY BRANCH: include all type-2; include type-7 only if no PO(7)
    si_branch AS (
        SELECT
            s.id, s.create_by, s.create_time, s.update_by, s.update_time,
            s.invoice_number, s.client_id, s.currency_id,
            s.total_amount, s.discount_amount, s.final_amount,
            s.payment_document,
            NULL AS inventory_document,
            s.payment_approved AS ordered,
            NULL AS group_id,
            s.status,
            s.platformOrderId,
            s.payment_approved,
            -- SPLIT: P=0, S=shipping
            0              AS poTotalAmount,
            s.total_amount AS siTotalAmount,
            0              AS poFinalAmount,
            s.final_amount AS siFinalAmount
        FROM si_base_strict s
        WHERE (s.ticket_prefix = '2' OR s.ticket_prefix = '7')
          AND s.payment_approved = 0
          AND NOT EXISTS (
            SELECT 1 FROM po_base pb
            WHERE pb.ticket_prefix = '7' AND pb.invoice_number = s.invoice_number
        )
    ),

    -- PO BRANCH: PO + SHIPPING (prefer strict sum, fallback relaxed); platformId: PO first, else shipping
    po_branch AS (
        SELECT
            pb.id, pb.create_by, pb.create_time, pb.update_by, pb.update_time,
            pb.invoice_number, pb.client_id, pb.currency_id,

            (COALESCE(pb.total_amount,0)
                + COALESCE(ss.siTotalAmount, ssr.siTotalAmount_any, 0))  AS total_amount,
            pb.discount_amount,
            (COALESCE(pb.final_amount,0)
                + COALESCE(ss.siFinalAmount, ssr.siFinalAmount_any, 0))  AS final_amount,

            pb.payment_document, pb.inventory_document, pb.ordered, pb.group_id,
            pb.status,
            COALESCE(pa.po_platform_ids, psa.platformOrderId) AS platformOrderId,
            pb.payment_approved,

            -- SPLIT: show (P + S) in UI
            COALESCE(pb.total_amount,0)                                   AS poTotalAmount,
            COALESCE(ss.siTotalAmount, ssr.siTotalAmount_any, 0)          AS siTotalAmount,
            COALESCE(pb.final_amount,0)                                   AS poFinalAmount,
            COALESCE(ss.siFinalAmount, ssr.siFinalAmount_any, 0)          AS siFinalAmount

        FROM po_base pb
                 LEFT JOIN si_sum_strict  ss  ON ss.invoice_number  = pb.invoice_number
                 LEFT JOIN si_sum_relaxed ssr ON ssr.invoice_number = pb.invoice_number
                 LEFT JOIN purchase_active pa  ON pa.po_no          = pb.invoice_number
                 LEFT JOIN po_ship_active  psa ON psa.inv_no        = pb.invoice_number
    )

-- RESULT: prefer PO rows (already merged S); add shipping-only rows
SELECT
    id, create_by, create_time, update_by, update_time,
    invoice_number, client_id, currency_id,
    total_amount, discount_amount, final_amount,
    payment_document, inventory_document, ordered, group_id,
    status, platformOrderId, payment_approved,
    poTotalAmount, siTotalAmount, poFinalAmount, siFinalAmount
FROM po_branch
UNION ALL
SELECT
    id, create_by, create_time, update_by, update_time,
    invoice_number, client_id, currency_id,
    total_amount, discount_amount, final_amount,
    payment_document, inventory_document, ordered, group_id,
    status, platformOrderId, payment_approved,
    poTotalAmount, siTotalAmount, poFinalAmount, siFinalAmount
FROM si_branch;
