create
    definer = admin@`%` function shopErpToId(erp varchar(36)) returns varchar(36) reads sql data
BEGIN
    DECLARE found_shop_id varchar(36);
    SET found_shop_id = (SELECT id FROM shop WHERE erp_code = erp);
    IF found_shop_id IS NOT NULL THEN
        RETURN found_shop_id;
    ELSE
        RETURN erp;
    END IF;
END;

