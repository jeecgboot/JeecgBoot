package org.jeecg.modules.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 客户
 * @Author: jeecg-boot
 * @Date: 2021-11-10
 * @Version: V1.2
 */
@ApiModel(value = "client对象", description = "客户")
@Data
@TableName("client")
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
    private String surname;
    /**
     * 名
     */
    @Excel(name = "名", width = 15)
    @ApiModelProperty(value = "名")
    private String firstName;
    /**
     * 简称
     */
    @Excel(name = "简称", width = 15)
    @ApiModelProperty(value = "简称")
    private String internalCode;
    /**
     * 发票实体
     */
    @Excel(name = "发票实体", width = 15)
    @ApiModelProperty(value = "发票实体")
    private String invoiceEntity;
    /**
     * 邮箱
     */
    @Excel(name = "邮箱", width = 15)
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 电话
     */
    @Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
    private String phone;
    /**
     * 门牌号码
     */
    @Excel(name = "门牌号码", width = 15)
    @ApiModelProperty(value = "门牌号码")
    private String streetNumber;
    /**
     * 街道名
     */
    @Excel(name = "街道名", width = 15)
    @ApiModelProperty(value = "街道名")
    private String streetName;
    /**
     * 备用地址
     */
    @Excel(name = "备用地址", width = 15)
    @ApiModelProperty(value = "备用地址")
    private String additionalAddress;
    /**
     * 邮编
     */
    @Excel(name = "邮编", width = 15)
    @ApiModelProperty(value = "邮编")
    private String postcode;
    /**
     * 城市
     */
    @Excel(name = "城市", width = 15)
    @ApiModelProperty(value = "城市")
    private String city;
    /**
     * 国家
     */
     @Excel(name = "国家", width = 15, dictTable = "country", dicText = "name_en", dicCode = "name_en")
    @Dict(dictTable = "country", dicText = "name_en", dicCode = "name_en")
    @ApiModelProperty(value = "国家")
    private String country;
    /**
     * 货币
     */
    @Excel(name = "货币", width = 15)
    @ApiModelProperty(value = "货币")
    private String currency;
    /**
     * 运费折扣
     */
    @Excel(name = "运费折扣", width = 15)
    @ApiModelProperty(value = "运费折扣")
    private BigDecimal shippingDiscount;
    /**
     * 公司识别码类型
     */
    @Excel(name = "公司识别码类型", width = 15)
    @ApiModelProperty(value = "公司识别码类型")
    private String companyIdType;
    /**
     * 公司识别码数值
     */
    @Excel(name = "公司识别码数值", width = 15)
    @ApiModelProperty(value = "公司识别码数值")
    private String companyIdValue;
    /**
     * 账户余额
     */
    @Excel(name = "账户余额", width = 15)
    @ApiModelProperty(value = "账户余额")
    private BigDecimal balance;
    /**
     * IOSS号码
     */
    @Excel(name = "IOSS号码", width = 15)
    @ApiModelProperty(value = "IOSS号码")
    private String iossNumber;
    /**
     * VAT代缴比例
     */
    @Excel(name = "VAT代缴比例", width = 15)
    @ApiModelProperty(value = "VAT代缴比例")
    private BigDecimal vatPercentage;
    /**
     * 是否活跃
     */
    @Excel(name = "是否活跃", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否活跃")
    private String active;

    public String fullName() {
        return firstName + " " + surname;
    }
}
