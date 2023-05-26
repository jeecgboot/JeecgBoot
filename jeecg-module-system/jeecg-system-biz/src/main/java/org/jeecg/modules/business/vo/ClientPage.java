package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.business.entity.ClientSku;
import org.jeecg.modules.business.entity.Shop;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 客户
 * @Author: jeecg-boot
 * @Date: 2021-11-10
 * @Version: V1.2
 */
@Data
@ApiModel(value = "clientPage对象", description = "客户")
public class ClientPage {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * 姓
     */
    @Excel(name = "姓", width = 15)
    @ApiModelProperty(value = "姓")
    private java.lang.String surname;
    /**
     * 名
     */
    @Excel(name = "名", width = 15)
    @ApiModelProperty(value = "名")
    private java.lang.String firstName;
    /**
     * 简称
     */
    @Excel(name = "简称", width = 15)
    @ApiModelProperty(value = "简称")
    private java.lang.String internalCode;
    /**
     * 发票实体
     */
    @Excel(name = "发票实体", width = 15)
    @ApiModelProperty(value = "发票实体")
    private java.lang.String invoiceEntity;
    /**
     * 邮箱
     */
    @Excel(name = "邮箱", width = 15)
    @ApiModelProperty(value = "邮箱")
    private java.lang.String email;
    /**
     * 电话
     */
    @Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
    private java.lang.String phone;
    /**
     * 门牌号码
     */
    @Excel(name = "门牌号码", width = 15)
    @ApiModelProperty(value = "门牌号码")
    private java.lang.String streetNumber;
    /**
     * 街道名
     */
    @Excel(name = "街道名", width = 15)
    @ApiModelProperty(value = "街道名")
    private java.lang.String streetName;
    /**
     * 备用地址
     */
    @Excel(name = "备用地址", width = 15)
    @ApiModelProperty(value = "备用地址")
    private java.lang.String additionalAddress;
    /**
     * 邮编
     */
    @Excel(name = "邮编", width = 15)
    @ApiModelProperty(value = "邮编")
    private java.lang.String postcode;
    /**
     * 城市
     */
    @Excel(name = "城市", width = 15)
    @ApiModelProperty(value = "城市")
    private java.lang.String city;
    /**
     * 国家
     */
    @Excel(name = "国家", width = 15, dictTable = "country", dicText = "name_en", dicCode = "name_en")
    @Dict(dictTable = "country", dicText = "name_en", dicCode = "name_en")
    @ApiModelProperty(value = "国家")
    private java.lang.String country;
    /**
     * 货币
     */
    @Excel(name = "货币", width = 15)
    @ApiModelProperty(value = "货币")
    private java.lang.String currency;
    /**
     * 运费折扣
     */
    @Excel(name = "运费折扣", width = 15)
    @ApiModelProperty(value = "运费折扣")
    private java.math.BigDecimal shippingDiscount;
    /**
     * 公司识别码类型
     */
    @Excel(name = "公司识别码类型", width = 15)
    @ApiModelProperty(value = "公司识别码类型")
    private java.lang.String companyIdType;
    /**
     * 公司识别码数值
     */
    @Excel(name = "公司识别码数值", width = 15)
    @ApiModelProperty(value = "公司识别码数值")
    private java.lang.String companyIdValue;
    /**
     * 账户余额
     */
    @Excel(name = "账户余额", width = 15)
    @ApiModelProperty(value = "账户余额")
    private java.math.BigDecimal balance;
    /**
     * IOSS号码
     */
    @Excel(name = "IOSS号码", width = 15)
    @ApiModelProperty(value = "IOSS号码")
    private java.lang.String iossNumber;
    /**
     * VAT代缴比例
     */
    @Excel(name = "VAT代缴比例", width = 15)
    @ApiModelProperty(value = "VAT代缴比例")
    private java.math.BigDecimal vatPercentage;
    /**
     * 是否活跃
     */
    @Excel(name = "是否活跃", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否活跃")
    private java.lang.String active;

    @ExcelCollection(name = "店铺")
    @ApiModelProperty(value = "店铺")
    private List<Shop> shopList;
    @ExcelCollection(name = "客户名下SKU")
    @ApiModelProperty(value = "客户名下SKU")
    private List<ClientSku> clientSkuList;

}
