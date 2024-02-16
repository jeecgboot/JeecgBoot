package org.jeecg.modules.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

/**
 * @Description: SKU表
 * @Author: jeecg-boot
 * @Date: 2021-08-13
 * @Version: V1.2
 */
@Data
@ApiModel(value = "skuOrderPage对象", description = "SKU表")
public class SkuOrderPage {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 商品ID
     */
    @Excel(name = "商品ID", width = 15, dictTable = "product", dicText = "code", dicCode = "id")
    @Dict(dictTable = "product", dicText = "code", dicCode = "id")
    @ApiModelProperty(value = "商品ID")
    private String product;
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
    /**
     * 在途数量
     */
    @Excel(name = "在途数量", width = 15)
    @ApiModelProperty(value = "在途数量")
    private Integer purchasingAmount;
    /**
     * 未发货数量
     */
    @Excel(name = "未发货数量", width = 15)
    @ApiModelProperty(value = "未发货数量")
    private Integer qtyInOrdersNotShipped;
    /**
     * stock
     */
    @Excel(name = "stock", width = 15)
    @ApiModelProperty(value = "stock")
    private Integer stock;
    /**
     * 图片链接
     */
    @Excel(name = "图片链接", width = 15)
    @ApiModelProperty(value = "图片链接")
    private String imageSource;
    /**
     * 运费折扣
     */
    @Excel(name = "运费折扣", width = 15)
    @ApiModelProperty(value = "运费折扣")
    private java.math.BigDecimal shippingDiscount;
    /**
     * 服务费
     */
    @Excel(name = "服务费", width = 15)
    @ApiModelProperty(value = "服务费")
    private java.math.BigDecimal serviceFee;
    /**
     * SKU价格
     */
    @ExcelCollection(name = "SKU价格")
    @ApiModelProperty(value = "SKU价格")
    private java.math.BigDecimal skuPrice;
    /**
     * 优惠价起订量
     */
    @Excel(name = "优惠价起订量", width = 15)
    @ApiModelProperty(value = "优惠价起订量")
    private java.lang.Integer discountMoq;
    /**
     * 优惠价
     */
    @Excel(name = "优惠价", width = 15)
    @ApiModelProperty(value = "优惠价")
    private java.math.BigDecimal discountedPrice;
    /**
     * Sales last week
     */
    @Excel(name = "sales last week", width = 15)
    @ApiModelProperty(value = "sales last week")
    private java.lang.Integer salesLastWeek;
    /**
     * Sales last four week
     */
    @Excel(name = "sales last 28 days", width = 15)
    @ApiModelProperty(value = "sales last 28 days")
    private java.lang.Integer salesFourWeeks;
    /**
     * Sales last six weeks
     */
    @Excel(name = "sales last 42 days", width = 15)
    @ApiModelProperty(value = "sales last 42 days")
    private java.lang.Integer salesSixWeeks;
}
