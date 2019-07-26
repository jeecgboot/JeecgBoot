package org.jeecg.modules.bjcl.config.entity;

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
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 配置
 * @Author: jeecg-boot
 * @Date:   2019-07-23
 * @Version: V1.0
 */
@Data
@TableName("bjcl_config")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="bjcl_config对象", description="配置")
public class Config {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**1是关于我们，2友情链接*/
	@Excel(name = "1是关于我们，2友情链接", width = 15)
    @ApiModelProperty(value = "1是关于我们，2友情链接")
	private String type;
	/**内容*/
	@Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
	private Object content;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
	private Integer sort;
	/**状态（0正常 1停用）*/
	@Excel(name = "状态（0正常 1停用）", width = 15)
    @ApiModelProperty(value = "状态（0正常 1停用）")
	private String status;
	/**删除标志（0代表存在 2代表删除）*/
	@Excel(name = "删除标志（0代表存在 2代表删除）", width = 15)
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
	private String delFlag;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remark;
}
