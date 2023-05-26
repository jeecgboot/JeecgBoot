package org.jeecg.modules.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.modules.business.entity.ParcelTrace;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 包裹
 * @Author: jeecg-boot
 * @Date: 2022-02-18
 * @Version: V1.0
 */
@Data
@ApiModel(value = "parcelPage对象", description = "包裹")
public class ParcelPage {

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
     * 运单编号
     */
    @Excel(name = "运单编号", width = 15)
    @ApiModelProperty(value = "运单编号")
    private java.lang.String billCode;
    /**
     * 目的国家
     */
    @Excel(name = "目的国家", width = 15)
    @ApiModelProperty(value = "目的国家")
    private java.lang.String country;
    /**
     * 末端派送单号
     */
    @Excel(name = "末端派送单号", width = 15)
    @ApiModelProperty(value = "末端派送单号")
    private java.lang.String thirdBillCode;
    /**
     * 订单编号
     */
    @Excel(name = "订单编号", width = 15)
    @ApiModelProperty(value = "订单编号")
    private java.lang.String orderNo;

    @ExcelCollection(name = "包裹轨迹")
    @ApiModelProperty(value = "包裹轨迹")
    private List<ParcelTrace> parcelTraceList;

}
