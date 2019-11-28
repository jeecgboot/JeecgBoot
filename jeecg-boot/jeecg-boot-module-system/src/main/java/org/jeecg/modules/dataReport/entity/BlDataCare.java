package org.jeecg.modules.dataReport.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 数据比对
 * @Author: Zhao
 * @Date:   2019-11-13
 * @Version: V1.0
 */
@Data
@TableName("bl_data_care")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="bl_data_care对象", description="数据比对")
public class BlDataCare {

    /**id*/
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**提单号*/
    @Excel(name = "提单号", width = 15)
    @ApiModelProperty(value = "提单号")
    private java.lang.String bolcode;
    /**海关放行日期*/
    @Excel(name = "海关放行日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "海关放行日期")
    private java.util.Date tmOdate;
    /**接单时间*/
    @Excel(name = "接单时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "接单时间")
    private java.util.Date odate;
    /**调度中心*/
    @Excel(name = "调度中心", width = 15)
    @ApiModelProperty(value = "调度中心")
    private java.lang.String sched;
    /**计划柜量*/
    @Excel(name = "计划柜量", width = 15)
    @ApiModelProperty(value = "计划柜量")
    private java.lang.Integer connum;
    /**完成箱量*/
    @Excel(name = "完成箱量", width = 15)
    @ApiModelProperty(value = "完成箱量")
    private java.lang.String containertype;

    /**数据状态*/
    @Excel(name = "数据状态", width = 15, dicCode = "data_status")
    @ApiModelProperty(value = "数据状态")
    @Dict(dicCode = "data_status")
    private java.lang.Integer datastatus;
    /**付款方*/
    @Excel(name = "付款方", width = 15)
    @ApiModelProperty(value = "付款方")
    private java.lang.String payer;
    /**业务员*/
    @Excel(name = "业务员", width = 15)
    @ApiModelProperty(value = "业务员")
    private java.lang.String salesman;
    /**业务类型*/
    @Excel(name = "业务类型", width = 15)
    @ApiModelProperty(value = "业务类型")
    private java.lang.String salestype;
    /**码头*/
    @Excel(name = "码头", width = 15)
    @ApiModelProperty(value = "码头")
    private java.lang.String wharf;
    /**OMS订单号*/
    @Excel(name = "OMS订单号", width = 15)
    @ApiModelProperty(value = "OMS订单号")
    private java.lang.String ordercode;
}
