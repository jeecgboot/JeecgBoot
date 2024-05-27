DELIMITER $$

CREATE TRIGGER init_balance
    AFTER INSERT ON client FOR EACH ROW
BEGIN
    DECLARE clientCategory VARCHAR(10);
    SET @clientCategory := (SELECT name FROM client_category WHERE id=NEW.client_category_id);
    IF @clientCategory = 'vip' OR @clientCategory = 'confirmed' THEN
        INSERT INTO balance (id, create_by, create_time, client_id, currency_id, operation_type, amount)
        VALUES (UUID(), NEW.create_by, NOW(), NEW.id, (SELECT id FROM currency WHERE code = 'EUR'), 'init', 0),
               (UUID(), NEW.create_by, NOW(), NEW.id, (SELECT id FROM currency WHERE code = 'USD'), 'init', 0);
    END IF;
END$$

DELIMITER ;