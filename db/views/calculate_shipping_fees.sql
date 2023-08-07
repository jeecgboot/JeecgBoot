CREATE FUNCTION calculate_shipping_fees(logistic_channel varchar(50), country varchar(2), shipping_date date,
                                        weight int) RETURNS DOUBLE
BEGIN
    DECLARE minimum_weight INT;
    DECLARE minimum_weight_price double;
    DECLARE cal_unit INT;
    DECLARE cal_unit_price double;
    DECLARE additional_cost double;
    DECLARE shipping_fee double;

    SELECT lcp.minimum_weight,
           lcp.minimum_weight_price,
           lcp.cal_unit,
           lcp.cal_unit_price,
           lcp.additional_cost
    INTO minimum_weight,
        minimum_weight_price,
        cal_unit,
        cal_unit_price,
        additional_cost
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
        SET shipping_fee = 0;
    ELSEIF weight < minimum_weight THEN
        SET shipping_fee = minimum_weight_price;
    ELSE
        SET shipping_fee = ((weight - minimum_weight) / cal_unit) * cal_unit_price + minimum_weight_price;
    END IF;

    RETURN shipping_fee;
END;