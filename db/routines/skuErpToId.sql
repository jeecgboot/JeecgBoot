create
    definer = admin@`%` function skuErpToId(sku varchar(100)) returns varchar(100)
BEGIN
    DECLARE found_sku_id varchar(36);
    SET found_sku_id = (SELECT id FROM sku WHERE erp_code = sku);
    IF found_sku_id IS NOT NULL THEN
        RETURN found_sku_id;
    ELSE
        RETURN sku;
    END IF;
END;

