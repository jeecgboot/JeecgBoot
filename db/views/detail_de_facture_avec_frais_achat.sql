CREATE OR REPLACE VIEW detail_de_facture_avec_frais_achat AS
SELECT
    s.name                                            AS `Boutique`,
    po.platform_order_id                              AS `N° de Mabang`,
    po.platform_order_number                          AS `N° de commande`,
    po.tracking_number                                AS `N° de suivi`,
    po.order_time                                     AS `Date de commande`,
    po.shipping_time                                  AS `Date d'expédition`,
    po.recipient                                      AS `Nom de client`,
    po.country                                        AS `Pays`,
    po.postcode                                       AS `Code postal`,
    JSON_ARRAYAGG(sku.erp_code)                       AS `SKU`,
    JSON_ARRAYAGG(sku.en_name)                        AS `Nom produits`,
    JSON_ARRAYAGG(poc.quantity)                       AS `Quantité`,

    -- Calculate purchase fees, using the price from sku_price if purchase_fee is 0
    COALESCE(SUM(
                     CASE
                         WHEN poc.purchase_fee = 0 THEN poc.quantity * COALESCE(
                                 (
                                     SELECT sp.price
                                     FROM sku_price sp
                                     WHERE sp.sku_id = poc.sku_id
                                       AND sp.date <= po.order_time
                                     ORDER BY sp.date DESC
                                     LIMIT 1
                                 ), 0
                                                                       )
                         ELSE poc.purchase_fee
                         END
             ), 0)                                             AS `Frais d'achat`,

    po.fret_fee                                       AS `Frais de FRET`,
    SUM(poc.shipping_fee)                             AS `Frais de livraison`,
    (po.order_service_fee + SUM(poc.service_fee))     AS `Frais de service`,
    (po.picking_fee + SUM(poc.picking_fee))           AS `Frais de préparation`,
    po.packaging_material_fee                         AS `Frais de matériel d'emballage`,
    COALESCE(po.insurance_fee, 0.00)                  AS `Frais d'assurance produits`,
    SUM(poc.vat)                                      AS `TVA`,
    po.shipping_invoice_number                        AS `N° de facture`
FROM platform_order po
         JOIN shop s ON po.shop_id = s.id
         RIGHT JOIN platform_order_content poc ON po.id = poc.platform_order_id
         JOIN sku ON poc.sku_id = sku.id
WHERE po.order_time > '2025-01-01'
  AND shipping_invoice_number IS NOT NULL
  AND poc.erp_status <> 5
GROUP BY po.id, s.name, po.order_time
ORDER BY s.name, po.order_time;
