package org.jeecg.modules.business.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExtraFeeParam {
    private String id;
    private String shop;
    private String optionId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String description;
}
