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
public class NewSkuPage {

    /**
     * 主键
     */
    @Excel(name = "马帮动创建Sku", width = 15)
    @ApiModelProperty(value = "马帮动创建Sku")
    private String mabangSku;
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
    private Integer weight;
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
    private BigDecimal shippingDiscount;
    /**
     * 服务费
     */
    @Excel(name = "服务费", width = 15)
    @ApiModelProperty(value = "服务费")
    private BigDecimal serviceFee;
    /**
     * 状态
     * 1:自动创建;2:待开发;3:正常;4:清仓;5:停止销售"
     * default : 3
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private Integer status;
    /**
     * 敏感属性
     */
    @Excel(name = "敏感属性", width = 15)
    @ApiModelProperty(value = "敏感属性")
    private String sensitiveAttribute;
    /**
     * 是否赠品
     * 1是 2否
     */
    @Excel(name = "是否赠品", width = 15)
    @ApiModelProperty(value = "是否赠品")
    private Integer isGift;
    /**
     * SKU价格
     */
    @ApiModelProperty(value = "SKU价格")
    private BigDecimal skuPrice;
    /**
     * 申报价格
     */
    @Excel(name = "申报价格", width = 15)
    @ApiModelProperty(value = "申报价格")
    private BigDecimal declaredValue;
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

    @Excel(name = "仓库", width = 15)
    @ApiModelProperty(value = "仓库")
    private String warehouse;
}
