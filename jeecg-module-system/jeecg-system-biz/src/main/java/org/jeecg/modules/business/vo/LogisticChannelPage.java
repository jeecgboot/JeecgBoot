package org.jeecg.modules.business.vo;

import java.util.List;

import org.jeecg.modules.business.entity.LogisticChannel;
import org.jeecg.modules.business.entity.LogisticChannelPrice;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 物流渠道
 * @Author: jeecg-boot
 * @Date: 2021-07-08
 * @Version: V1.1
 */
@Data
@ApiModel(value = "logistic_channelPage对象", description = "物流渠道")
public class LogisticChannelPage {

    /**
     * 主键
     */
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
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
     * 内部名称
     */
    @Excel(name = "内部名称", width = 15)
    @ApiModelProperty(value = "内部名称")
    private String internalName;
    /**
     * 中文名称
     */
    @Excel(name = "中文名称", width = 15)
    @ApiModelProperty(value = "中文名称")
    private String zhName;
    /**
     * 英文名称
     */
    @Excel(name = "英文名称", width = 15)
    @ApiModelProperty(value = "英文名称")
    private String enName;
    /**
     * 公司名称
     */
    @Excel(name = "公司名称", width = 15)
    @ApiModelProperty(value = "公司名称")
    private String company;
    /**
     * 是否使用抛重
     */
    @Excel(name = "是否使用抛重", width = 15, dicCode = "volume_weight")
    @Dict(dicCode = "volume_weight")
    @ApiModelProperty(value = "是否使用抛重")
    private Integer useVolumetricWeight;
    /**
     * 抛重系数
     */
    @Excel(name = "抛重系数", width = 15)
    @ApiModelProperty(value = "抛重系数")
    private Integer volumetricWeightFactor;

    @ExcelCollection(name = "物流渠道价格")
    @ApiModelProperty(value = "物流渠道价格")
    private List<LogisticChannelPrice> logisticChannelPriceList;

}
