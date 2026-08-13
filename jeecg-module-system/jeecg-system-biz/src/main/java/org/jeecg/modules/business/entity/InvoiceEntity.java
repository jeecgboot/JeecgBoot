package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
import java.math.BigDecimal;

@ApiModel(value = "invoiceEntity对象", description = "发票实体")
@Data
@TableName("invoice_entity")
public class InvoiceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;

    @Dict(dictTable = "client", dicText = "internal_code", dicCode = "id")
    @ApiModelProperty(value = "所属客户ID")
    private String clientId;

    @TableField("invoice_entity")
    @Excel(name = "发票实体", width = 15)
    @ApiModelProperty(value = "发票实体")
    private String invoiceEntity;

    @Excel(name = "邮箱", width = 15)
    @ApiModelProperty(value = "邮箱")
    private String email;

    @Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
    private String phone;

    @Excel(name = "门牌号码", width = 15)
    @ApiModelProperty(value = "门牌号码")
    private String streetNumber;

    @Excel(name = "街道名", width = 15)
    @ApiModelProperty(value = "街道名")
    private String streetName;

    @Excel(name = "备用地址", width = 15)
    @ApiModelProperty(value = "备用地址")
    private String additionalAddress;

    @Excel(name = "邮编", width = 15)
    @ApiModelProperty(value = "邮编")
    private String postcode;

    @Excel(name = "城市", width = 15)
    @ApiModelProperty(value = "城市")
    private String city;

    @Excel(name = "国家", width = 15, dictTable = "country", dicText = "name_en", dicCode = "name_en")
    @Dict(dictTable = "country", dicText = "name_en", dicCode = "name_en")
    @ApiModelProperty(value = "国家")
    private String country;

    @Excel(name = "货币", width = 15)
    @ApiModelProperty(value = "货币")
    private String currency;

    @Excel(name = "公司识别码类型", width = 15)
    @ApiModelProperty(value = "公司识别码类型")
    private String companyIdType;

    @Excel(name = "公司识别码数值", width = 15)
    @ApiModelProperty(value = "公司识别码数值")
    private String companyIdValue;

    @Excel(name = "IOSS号码", width = 15)
    @ApiModelProperty(value = "IOSS号码")
    private String iossNumber;

    @Excel(name = "VAT代缴比例", width = 15)
    @ApiModelProperty(value = "VAT代缴比例")
    private BigDecimal vatPercentage;

    @Excel(name = "是否启用", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否启用")
    private String active;

    @Excel(name = "是否默认实体", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否默认实体")
    private String isDefault;
}
