package org.jeecg.modules.airag.prompts.entity;

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
 * @Description: airag_ext_data
 * @Author: jeecg-boot
 * @Date:   2025-12-12
 * @Version: V1.0
 */
@Data
@TableName("airag_ext_data")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="airag_ext_data")
public class AiragExtData implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID")
    private java.lang.String id;
	/**业务类型标识(evaluator:评估器；track:测试追踪)*/
	@Excel(name = "业务类型标识(evaluator:评估器；track:测试追踪)", width = 15)
    @Schema(description = "业务类型标识(evaluator:评估器；track:测试追踪)")
    private java.lang.String bizType;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @Schema(description = "名称")
    private java.lang.String name;
	/**描述信息*/
	@Excel(name = "描述信息", width = 15)
    @Schema(description = "描述信息")
    private java.lang.String descr;
	/**标签，多个用逗号分隔*/
	@Excel(name = "标签，多个用逗号分隔", width = 15)
    @Schema(description = "标签，多个用逗号分隔")
    private java.lang.String tags;
	/**实际存储内容，json*/
	@Excel(name = "实际存储内容，json", width = 15)
    @Schema(description = "实际存储内容，json")
    private java.lang.String dataValue;
	/**元数据，用于存储补充业务数据信息*/
	@Excel(name = "元数据，用于存储补充业务数据信息", width = 15)
    @Schema(description = "元数据，用于存储补充业务数据信息")
    private java.lang.String metadata;
	/**评测集数据*/
	@Excel(name = "评测集数据", width = 15)
    @Schema(description = "评测集数据")
    private java.lang.String datasetValue;
	/**创建人*/
    @Schema(description = "创建人")
    private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private java.util.Date createTime;
	/**修改人*/
    @Schema(description = "修改人")
    private java.lang.String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "修改时间")
    private java.util.Date updateTime;
	/**所属部门*/
    @Schema(description = "所属部门")
    private java.lang.String sysOrgCode;
	/**租户id*/
	@Excel(name = "租户id", width = 15)
    @Schema(description = "租户id")
    private java.lang.String tenantId;
	/**状态*/
	@Excel(name = "状态（run:进行中 completed：已完成）", width = 15)
    @Schema(description = "状态（run:进行中 completed：已完成）")
    private java.lang.String status;
	/**版本*/
	@Excel(name = "版本", width = 15)
    @Schema(description = "版本")
    private java.lang.Integer version;
}
