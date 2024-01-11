package org.jeecg.modules.business.entity;

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
import java.util.Date;

@Data
@ApiModel(value = "default_logistic_channel", description = "default logistic channel")
@TableName("default_logistic_channel")
public class LogisticChannelChoice implements Serializable
{
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
     * 店铺ID
     */
    @Excel(name = "店铺ID", width = 15, dictTable = "shop", dicText = "erp_code", dicCode = "id")
    @Dict(dictTable = "shop", dicText = "erp_code", dicCode = "id")
    @ApiModelProperty(value = "店铺ID")
    private String shopId;
    /**
     * 物流渠道ID
     */
    @Excel(name = "物流渠道ID", width = 15, dictTable = "logistic_channel", dicText = "zh_name", dicCode = "id")
    @Dict(dictTable = "logistic_channel", dicText = "zh_name", dicCode = "id")
    @ApiModelProperty(value = "物流渠道ID")
    private String logisticChannelId;
    /**
     * 订单收件人国家ID
     */
    @Excel(name = "订单收件人国家ID", width = 15, dictTable = "country", dicText = "code", dicCode = "id")
    @Dict(dictTable = "country", dicText = "code", dicCode = "id")
    @ApiModelProperty(value = "订单收件人国家ID")
    private String countryId;
    /**
     * sensitive attribute id
     */
    @Excel(name = "sensitive attribute id", width = 15, dictTable = "sensitive_attribute", dicText = "zh_name", dicCode = "id")
    @Dict(dictTable = "sensitive_attribute", dicText = "sensitive_attribute", dicCode = "id")
    @ApiModelProperty(value = "sensitive attribute id")
    private String sensitiveAttributeId;
}
