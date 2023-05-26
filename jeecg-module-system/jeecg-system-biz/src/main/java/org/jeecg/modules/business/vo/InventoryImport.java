package org.jeecg.modules.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
@ApiModel(value="inventory", description="库存采购")
public class InventoryImport {
    @ApiModelProperty(value = "SKU")
    @Excel(name = "SKU", width = 15)
    private String erpCode;

    @Excel(name = "Quantité d'achat")
    @ApiModelProperty(value = "Quantité d'achat")
    private Integer quantity;

}
