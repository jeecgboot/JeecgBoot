package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 物流开销明细
 * @Author: jeecg-boot
 * @Date: 2021-09-06
 * @Version: V1.2
 */
@Data
@TableName("logistic_expense_detail")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "logistic_expense_detail对象", description = "物流开销明细")
public class LogisticExpenseDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+2", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 平台订单序列号（客户单号）
     */
    @Excel(name = "平台订单序列号（客户单号）", width = 15)
    @ApiModelProperty(value = "平台订单序列号（客户单号）")
    private String platformOrderSerialId;
    /**
     * 虚拟单号
     */
    @Excel(name = "虚拟单号", width = 15)
    @ApiModelProperty(value = "虚拟单号")
    private String virtualTrackingNumber;
    /**
     * 物流商内部单号
     */
    @Excel(name = "物流商内部单号", width = 15)
    @ApiModelProperty(value = "物流商内部单号")
    private String logisticInternalNumber;
    /**
     * 物流单号（服务商单号）
     */
    @Excel(name = "物流单号（服务商单号）", width = 15)
    @ApiModelProperty(value = "物流单号（服务商单号）")
    private String trackingNumber;
    /**
     * 实际重量
     */
    @Excel(name = "实际重量", width = 15)
    @ApiModelProperty(value = "实际重量")
    private BigDecimal realWeight;
    /**
     * 体积重量
     */
    @Excel(name = "体积重量", width = 15)
    @ApiModelProperty(value = "体积重量")
    private BigDecimal volumetricWeight;
    /**
     * 计费重量
     */
    @Excel(name = "计费重量", width = 15)
    @ApiModelProperty(value = "计费重量")
    private BigDecimal chargingWeight;
    /**
     * 优惠金额
     */
    @Excel(name = "优惠金额", width = 15)
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discount;
    /**
     * 运费金额
     */
    @Excel(name = "运费金额", width = 15)
    @ApiModelProperty(value = "运费金额")
    private BigDecimal shippingFee;
    /**
     * 燃油附加费
     */
    @Excel(name = "燃油附加费", width = 15)
    @ApiModelProperty(value = "燃油附加费")
    private BigDecimal fuelSurcharge;
    /**
     * 挂号费
     */
    @Excel(name = "挂号费", width = 15)
    @ApiModelProperty(value = "挂号费")
    private BigDecimal registrationFee;
    /**
     * 重派费
     */
    @Excel(name = "重派费", width = 15)
    @ApiModelProperty(value = "重派费")
    private BigDecimal secondDeliveryFee;
    /**
     * 增值税
     */
    @Excel(name = "增值税", width = 15)
    @ApiModelProperty(value = "增值税")
    private BigDecimal vat;
    /**
     * 增值税服务费
     */
    @Excel(name = "增值税服务费", width = 15)
    @ApiModelProperty(value = "增值税服务费")
    private BigDecimal vatServiceFee;
    /**
     * 附加费用
     */
    @Excel(name = "附加费用", width = 15)
    @ApiModelProperty(value = "附加费用")
    private BigDecimal additionalFee;
    /**
     * 总费用
     */
    @Excel(name = "总费用", width = 15)
    @ApiModelProperty(value = "总费用")
    private BigDecimal totalFee;
    /**
     * 物流公司ID
     */
    @Excel(name = "物流公司ID", width = 15, dictTable = "logistic_company", dicText = "name", dicCode = "id")
    @Dict(dictTable = "logistic_company", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "物流公司ID")
    private String logisticCompanyId;
    /**
     * 货物赔偿
     */
    @Excel(name = "货物赔偿", width = 15)
    @ApiModelProperty(value = "货物赔偿")
    private BigDecimal compensation;
}
