package org.jeecg.modules.business.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

@Data
public class ExtraFeeParam {
    private String id;
    @Excel(name = "shop", width = 20)
    private String shop;
    @Excel(name = "optionId", width = 20)
    private String optionId;
    @Excel(name = "quantity", width = 15)
    private Integer quantity;
    @Excel(name = "unitPrice", width = 15)
    private BigDecimal unitPrice;
    @Excel(name = "description", width = 30)
    private String description;
}
