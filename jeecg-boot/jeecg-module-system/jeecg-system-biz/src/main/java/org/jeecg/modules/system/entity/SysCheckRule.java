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

import java.util.Date;

/**
 * @Description: 编码校验规则
 * @Author: jeecg-boot
 * @Date: 2020-02-04
 * @Version: V1.0
 */
@Data
@TableName("sys_check_rule")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "sys_check_rule对象")
public class SysCheckRule {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    private String id;
    /**
     * 规则名称
     */
    @Excel(name = "规则名称", width = 15)
    @Schema(description = "规则名称")
    private String ruleName;
    /**
     * 规则Code
     */
    @Excel(name = "规则Code", width = 15)
    @Schema(description = "规则Code")
    private String ruleCode;
    /**
     * 规则JSON
     */
    @Excel(name = "规则JSON", width = 15)
    @Schema(description = "规则JSON")
    private String ruleJson;
    /**
     * 规则描述
     */
    @Excel(name = "规则描述", width = 15)
    @Schema(description = "规则描述")
    private String ruleDescription;
    /**
     * 更新人
     */
    @Excel(name = "更新人", width = 15)
    @Schema(description = "更新人")
    private String updateBy;
    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;
    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    @Schema(description = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;
}
