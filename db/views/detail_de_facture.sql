create definer = admin@`%` view detail_de_facture as
select `s`.`name`                                            AS `Boutique`,
       `po`.`platform_order_id`                              AS `N° de Mabang`,
       `po`.`platform_order_number`                          AS `N° de commande`,
       `po`.`tracking_number`                                AS `N° de suivi`,
       `po`.`order_time`                                     AS `Date de commande`,
       `po`.`shipping_time`                                  AS `Date d'expédition`,
       `po`.`recipient`                                      AS `Nom de client`,
       `po`.`country`                                        AS `Pays`,
       `po`.`postcode`                                       AS `Code postal`,
       json_arrayagg(`wia_app`.`sku`.`erp_code`)             AS `SKU`,
       json_arrayagg(`wia_app`.`sku`.`en_name`)              AS `Nom produits`,
       json_arrayagg(`poc`.`quantity`)                       AS `Quantité`,
       sum(`poc`.`purchase_fee`)                             AS `Frais d'achat`,
       `po`.`fret_fee`                                       AS `Frais de FRET`,
       sum(`poc`.`shipping_fee`)                             AS `Frais de livraison`,
       (`po`.`order_service_fee` + sum(`poc`.`service_fee`)) AS `Frais de service`,
       (`po`.`picking_fee` + sum(`poc`.`picking_fee`))       AS `Frais de préparation`,
       `po`.`packaging_material_fee`                         AS `Frais de matériel d'emballage`,
       `po`.`insurance_fee`                                  AS `Frais d'assurance produits`,
       sum(`poc`.`vat`)                                      AS `TVA`,
       `po`.`shipping_invoice_number`                        AS `N° de facture`
from ((`wia_app`.`platform_order_content` `poc` left join (`wia_app`.`platform_order` `po` join `wia_app`.`shop` `s`
                                                           on ((`po`.`shop_id` = `s`.`id`)))
       on ((`po`.`id` = `poc`.`platform_order_id`))) join `wia_app`.`sku` on ((`poc`.`sku_id` = `wia_app`.`sku`.`id`)))
where ((`po`.`order_time` > '2024-01-01') and (`po`.`shipping_invoice_number` is not null) and
       (`poc`.`erp_status` <> 5))
group by `po`.`id`, `s`.`name`, `po`.`order_time`
order by `s`.`name`, `po`.`order_time`;

-- comment on column detail_de_facture.Boutique not supported: 店铺名称

-- comment on column detail_de_facture.`N° de Mabang` not supported: 平台订单号码

-- comment on column detail_de_facture.`N° de commande` not supported: 平台订单交易号

-- comment on column detail_de_facture.`N° de suivi` not supported: 物流跟踪号

-- comment on column detail_de_facture.`Date de commande` not supported: 订单交易时间

-- comment on column detail_de_facture.`Date d'expédition` not supported: 订单发货时间

-- comment on column detail_de_facture.`Nom de client` not supported: 订单收件人

-- comment on column detail_de_facture.Pays not supported: 订单收件人国家

-- comment on column detail_de_facture.`Code postal` not supported: 订单收件人邮编

-- comment on column detail_de_facture.`Frais de FRET` not supported: 物流挂号费

-- comment on column detail_de_facture.`Frais de matériel d'emballage` not supported: 包材费

-- comment on column detail_de_facture.`Frais d'assurance produits` not supported: 物流保险费

-- comment on column detail_de_facture.`N° de facture` not supported: 物流发票号

