package org.jeecg.modules.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

@Data
@ApiModel(value = "sku_weight Page对象", description = "SKU重量")

public class SkuWeightPage {
    @Excel(name = "ERP中商品代码", width = 15)
    @ApiModelProperty(value = "ERP中商品代码")
    private String erpCode;
    @Excel(name = "重量", width = 15)
    @ApiModelProperty(value = "重量")
    private Integer weight;
    @Excel(name = "生效日期", width = 15)
    @ApiModelProperty(value = "生效日期")
    private Date effectiveDate;
}
