CREATE FUNCTION get_registration_fees(logistic_channel varchar(50), country varchar(2), shipping_date date,
                                      weight int) RETURNS DOUBLE
BEGIN
    DECLARE registration_fee double;

    SELECT lcp.registration_fee
    INTO registration_fee
    FROM logistic_channel_price lcp
             JOIN logistic_channel lc ON lc.id = lcp.channel_id
    WHERE lc.zh_name = logistic_channel
      AND weight_range_start <= weight
      AND weight_range_end >= weight
      AND effective_country = country
      AND effective_date <= shipping_date
    ORDER BY effective_date
            DESC
    LIMIT 1;

    IF weight = 0 THEN
        RETURN 0;
    ELSE
        RETURN registration_fee;
    END IF;
END;