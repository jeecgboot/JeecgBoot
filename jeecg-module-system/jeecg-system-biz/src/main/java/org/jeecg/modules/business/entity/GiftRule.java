package org.jeecg.modules.business.entity;

import lombok.Data;

@Data
public class GiftRule {

    private final String shopCode;

    private final String sku;

    private final String regex;

    private final Boolean matchQuantity;
}