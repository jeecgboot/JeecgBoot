package org.jeecg.modules.business.vo.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: Record in client Inventory page
 * @Author: Wenke
 * @Date: 2021-05-08
 * @Version: V1.1
 */
@ApiModel(value = "Inventory Record", description = "Entry in client inventory page")
@Data
@TableName("inventory_record")
public class InventoryRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "client id")
    @Dict(dictTable = "client", dicText = "internal_code", dicCode = "id")
    private String clientId;

    /**
     * 商品ID
     */
    @Excel(name = "商品ID", width = 15, dictTable = "product", dicText = "code", dicCode = "id")
    @Dict(dictTable = "product", dicText = "en_name", dicCode = "id")
    @ApiModelProperty(value = "商品ID")
    private String productId;

    /**
     * ERP中商品代码
     */
    @Excel(name = "ERP中商品代码", width = 15)
    @ApiModelProperty(value = "ERP中商品代码")
    private String erpCode;

    /**
     * 库存数量
     */
    @Excel(name = "库存数量", width = 15)
    @ApiModelProperty(value = "库存数量")
    private Integer availableAmount;

    @Excel(name = "green quantity", width = 15)
    @ApiModelProperty(value = "greenQuantity")
    private Integer greenQuantity;

    @Excel(name = "redQuantity", width = 15)
    @ApiModelProperty(value = "redQuantity")
    private Integer redQuantity;

    @Excel(name = "Sales from last 7 days", width = 15)
    @ApiModelProperty(value = "sales7")
    private Integer sales7 = 0;

    @Excel(name = "Sales from last 14 days", width = 15)
    @ApiModelProperty(value = "sales14")
    private Integer sales14 = 0;

    @Excel(name = "Sales from last 28 days", width = 15)
    @ApiModelProperty(value = "sales28")
    private Integer sales28 = 0;

    @Excel(name = "platform Order Quantity", width = 15)
    @ApiModelProperty(value = "platformOrderQuantity")
    private Integer platformOrderQuantity;

    /**
     * 图片链接
     */
    @Excel(name = "图片链接", width = 15)
    @ApiModelProperty(value = "图片链接")
    private String imageSource;

    @Excel(name = "MOQ", width = 15)
    @ApiModelProperty(value = "moq")
    private Integer moq = 0;
}
