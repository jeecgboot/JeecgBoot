package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 填值规则
 * @Author: jeecg-boot
 * @Date: 2019-11-07
 * @Version: V1.0
 */
@Data
@TableName("sys_fill_rule")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema( description = "填值规则")
public class SysFillRule {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private java.lang.String id;
    /**
     * 规则名称
     */
    @Excel(name = "规则名称", width = 15)
    @Schema(description = "规则名称")
    private java.lang.String ruleName;
    /**
     * 规则Code
     */
    @Excel(name = "规则Code", width = 15)
    @Schema(description = "规则Code")
    private java.lang.String ruleCode;
    /**
     * 规则实现类
     */
    @Excel(name = "规则实现类", width = 15)
    @Schema(description = "规则实现类")
    private java.lang.String ruleClass;
    /**
     * 规则参数
     */
    @Excel(name = "规则参数", width = 15)
    @Schema(description = "规则参数")
    private java.lang.String ruleParams;
    /**
     * 修改人
     */
    @Excel(name = "修改人", width = 15)
    @Schema(description = "修改人")
    private java.lang.String updateBy;
    /**
     * 修改时间
     */
    @Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "修改时间")
    private java.util.Date updateTime;
    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    @Schema(description = "创建人")
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private java.util.Date createTime;
}
