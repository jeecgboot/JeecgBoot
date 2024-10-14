create
    definer = admin@`%` function purchase_total(invoice_number varchar(12)) returns decimal(10, 2)
BEGIN
    RETURN (
        SELECT SUM(poc.purchase_fee) as total
        FROM platform_order_content as poc
                 JOIN platform_order po
                      ON po.id = poc.platform_order_id
        WHERE po.shipping_invoice_number = invoice_number
    );
END;

