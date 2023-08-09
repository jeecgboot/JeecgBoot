CREATE OR REPLACE VIEW full_logistic_expense_detail AS
SELECT led.tracking_number AS 'trackingNumber',
       led.real_weight,
       led.volumetric_weight,
       led.charging_weight,
       led.discount,
       led.shipping_fee,
       led.fuel_surcharge,
       led.registration_fee,
       led.second_delivery_fee,
       led.vat,
       led.vat_service_fee,
       led.total_fee,
       led.logistic_company_id,
       led.additional_fee
FROM logistic_expense_detail led
WHERE tracking_number = logistic_internal_number
UNION
SELECT led.logistic_internal_number AS 'trackingNumber',
       led.real_weight,
       led.volumetric_weight,
       led.charging_weight,
       led.discount,
       led.shipping_fee,
       led.fuel_surcharge,
       led.registration_fee,
       led.second_delivery_fee,
       led.vat,
       led.vat_service_fee,
       led.total_fee,
       led.logistic_company_id,
       led.additional_fee
FROM logistic_expense_detail led
WHERE tracking_number <> logistic_internal_number
UNION
SELECT led.tracking_number AS 'trackingNumber',
       led.real_weight,
       led.volumetric_weight,
       led.charging_weight,
       led.discount,
       led.shipping_fee,
       led.fuel_surcharge,
       led.registration_fee,
       led.second_delivery_fee,
       led.vat,
       led.vat_service_fee,
       led.total_fee,
       led.logistic_company_id,
       led.additional_fee
FROM logistic_expense_detail led
WHERE tracking_number <> logistic_internal_number;