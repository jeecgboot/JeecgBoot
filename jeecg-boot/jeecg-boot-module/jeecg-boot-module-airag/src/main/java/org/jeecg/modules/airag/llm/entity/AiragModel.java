package org.jeecg.modules.airag.llm.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: AiRag模型配置
 * @Author: jeecg-boot
 * @Date: 2025-02-17
 * @Version: V1.0
 */
@Data
@TableName("airag_model")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="AiRag模型配置")
public class AiragModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private String id;
    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private Date updateTime;
    /**
     * 所属部门
     */
    @Schema(description = "所属部门")
    private String sysOrgCode;
    /**
     * 租户id
     */
    @Excel(name = "租户id", width = 15)
    @Schema(description = "租户id")
    private String tenantId;
    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    @Schema(description = "名称")
    private String name;
    /**
     * 供应者
     */
    @Excel(name = "供应者", width = 15, dicCode = "model_provider")
    @Dict(dicCode = "model_provider")
    @Schema(description = "供应者")
    private String provider;
    /**
     * 模型类型
     */
    @Excel(name = "模型类型", width = 15, dicCode = "model_type")
    @Dict(dicCode = "model_type")
    @Schema(description = "模型类型")
    private String modelType;
    /**
     * 模型名称
     */
    @Excel(name = "模型名称", width = 15)
    @Schema(description = "模型名称")
    private String modelName;
    /**
     * API域名
     */
    @Excel(name = "API域名", width = 15)
    @Schema(description = "API域名")
    private String baseUrl;
    /**
     * 凭证信息
     */
    @Excel(name = "凭证信息", width = 15)
    @Schema(description = "凭证信息")
    private String credential;
    /**
     * 模型参数
     */
    @Excel(name = "模型参数", width = 15)
    @Schema(description = "模型参数")
    private String modelParams;
}
