package org.jeecg.modules.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

/**
 * @Description: SKU采购表
 * @Author: jeecg-boot
 * @Date: 2024-03-25
 * @Version: V1.1
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
    @Excel(name = "商品", width = 15)
    @ApiModelProperty(value = "商品")
    private String product;
    /**
     * 商品ID
     */
    @Excel(name = "商品英文", width = 15)
    @ApiModelProperty(value = "商品英文")
    private String productEn;
    /**
     * ERP中商品代码
     */
    @Excel(name = "ERP中商品代码", width = 15)
    @ApiModelProperty(value = "ERP中商品代码")
    private String erpCode;
    /**
     * 重量
     */
    @Excel(name = "重量", width = 15)
    @ApiModelProperty(value = "重量")
    private java.lang.Integer weight;
    /**
     * 生效日期
     */
    @Excel(name = "生效日期", width = 15)
    @ApiModelProperty(value = "生效日期")
    private java.util.Date effectiveDate;
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
     * 采购中数量
     */
    @Excel(name = "采购中数量", width = 15)
    @ApiModelProperty(value = "采购中数量")
    private Integer qtyOrdered;
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
