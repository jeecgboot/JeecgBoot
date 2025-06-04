package org.jeecg.modules.business.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.math.BigDecimal;

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
    @ApiModelProperty(value = "SKU重量表主键")
    private String weightId;

    /**
     * 商品ID
     */
    @Excel(name = "商品", width = 15)
    @ApiModelProperty(value = "商品")
    private String zhName;
    /**
     * 商品ID
     */
    @Excel(name = "商品英文", width = 15)
    @ApiModelProperty(value = "商品英文")
    private String enName;
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
     * 重量生效日期
     */
    @Excel(name = "重量生效日期", width = 15)
    @ApiModelProperty(value = "重量生效日期")
    private java.util.Date weightEffectiveDate;
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
     * 状态
     * 1:自动创建;2:待开发;3:正常;4:清仓;5:停止销售"
     * default : 3
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
    /**
     * 敏感属性
     */
    @Excel(name = "敏感属性", width = 15)
    @ApiModelProperty(value = "敏感属性")
    private String sensitiveAttribute;
    /**
     * 起订量
     */
    @Excel(name = "起订量", width = 15)
    @ApiModelProperty(value = "起订量")
    private java.lang.Integer moq;
    /**
     * 是否赠品
     * 1是 2否
     */
    @Excel(name = "是否赠品", width = 15)
    @ApiModelProperty(value = "是否赠品")
    private java.lang.Integer isGift;
    /**
     * SKU价格
     */
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
     * 价格生效日期
     */
    @Excel(name = "价格生效日期", width = 15)
    @ApiModelProperty(value = "价格生效日期")
    private java.util.Date skuPriceEffectiveDate;
    /**
     * 申报价格
     */
    @Excel(name = "申报价格", width = 15)
    @ApiModelProperty(value = "申报价格")
    private BigDecimal declaredValue;
    /**
     * 申报价格生效日期
     */
    @Excel(name = "申报价格生效日期", width = 15)
    @ApiModelProperty(value = "申报价格生效日期")
    private java.util.Date declaredValueEffectiveDate;
    /**
     * 申报中文名称
     */
    @Excel(name = "申报中文名称", width = 15)
    @ApiModelProperty(value = "申报中文名称")
    private String declareName;
    /**
     * 申报英文名称
     */
    @Excel(name = "申报英文名称", width = 15)
    @ApiModelProperty(value = "申报英文名称")
    private String declareEname;
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
    /**
     * 供应商
     */
    @Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private String supplier;
    /**
     * 供应商商品网址
     */
    @Excel(name = "供应商商品网址", width = 15)
    @ApiModelProperty(value = "供应商商品网址")
    private String supplierLink;

    @Excel(name="产品销售链接", width = 15)
    @ApiModelProperty(value = "产品销售链接")
    private String saleUrl;

    @Excel(name = "仓库", width = 15)
    @ApiModelProperty(value = "仓库")
    private String warehouse;

    @Excel(name ="自定义分类", width = 15)
    @ApiModelProperty(value = "自定义分类")
    private String labelData;

    @Excel(name = "商品多属性", width = 15)
    @ApiModelProperty(value = "商品多属性")
    private String specifics;
}
