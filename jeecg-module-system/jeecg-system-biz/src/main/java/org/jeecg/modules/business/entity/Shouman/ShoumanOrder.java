package org.jeecg.modules.business.entity.Shouman;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: 首曼订单
 * @Author: jeecg-boot
 * @Date: 2023-12-05
 * @Version: V1.1
 */
@Data
@TableName("shouman_order")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "shouman_order对象", description = "首曼订单")
public class ShoumanOrder implements Serializable {
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * 平台订单ID
     */
    @Excel(name = "平台订单ID", width = 15, dictTable = "platform_order", dicText = "platform_order_id", dicCode = "id")
    @Dict(dictTable = "platform_order", dicText = "platform_order_id", dicCode = "id")
    @ApiModelProperty(value = "平台订单ID")
    private java.lang.String platformOrderId;
    /**
     * 订单内容JSON
     */
    @Excel(name = "订单内容JSON", width = 15)
    @ApiModelProperty(value = "订单内容JSON")
    private java.lang.String orderJson;
    /**
     * 请求成功
     */
    @Excel(name = "请求成功", width = 15, dictTable = "yn", dicText = "", dicCode = "")
    @Dict(dictTable = "yn", dicText = "", dicCode = "")
    @ApiModelProperty(value = "请求成功")
    private java.lang.String success;
    /**
     * 签名字符串值
     */
    @Excel(name = "签名字符串值", width = 15)
    @ApiModelProperty(value = "签名字符串值")
    private java.lang.String signatureString;
    /**
     * 签名MD5值
     */
    @Excel(name = "签名MD5值", width = 15)
    @ApiModelProperty(value = "签名MD5值")
    private java.lang.String signatureMd5;
}
