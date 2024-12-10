package org.jeecg.modules.business.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExtraFeeResult {
    private String id;
    private java.lang.String createBy;
    private java.util.Date createTime;
    private String shop;
    private String enName;
    private String zhName;
    /** for "others" option*/
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String invoiceNumber;
}
