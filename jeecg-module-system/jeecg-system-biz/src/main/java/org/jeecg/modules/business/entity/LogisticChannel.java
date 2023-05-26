package org.jeecg.modules.business.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;

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

/**
 * @Description: 物流渠道
 * @Author: jeecg-boot
 * @Date: 2022-05-31
 * @Version: V1.1
 */
@ApiModel(value = "logistic_channel对象", description = "物流渠道")
@Data
@TableName("logistic_channel")
public class LogisticChannel implements Serializable {
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
     * 公司ID
     */
    @Excel(name = "公司ID", width = 15, dictTable = "logistic_company", dicText = "name", dicCode = "id")
    @Dict(dictTable = "logistic_company", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "公司ID")
    private String logisticCompanyId;
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
    /**
     * 是否活跃
     */
    @Excel(name = "是否活跃", width = 15)
    @ApiModelProperty(value = "是否活跃")
    private String active;
    /**
     * 是否活跃
     */
    @Excel(name = "渠道编码", width = 15)
    @ApiModelProperty(value = "渠道编码")
    private String code;
    /**
     * 仓库是否在中国
     */
    @Excel(name = "中国仓库", width = 15)
    @ApiModelProperty(value = "中国仓库")
    private String warehouseInChina;
}
