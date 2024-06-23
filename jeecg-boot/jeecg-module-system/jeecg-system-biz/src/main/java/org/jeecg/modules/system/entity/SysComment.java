package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 系统评论回复表
 * @Author: jeecg-boot
 * @Date:   2022-07-19
 * @Version: V1.0
 */
@Data
@TableName("sys_comment")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_comment对象", description="系统评论回复表")
public class SysComment implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**表名*/
	@Excel(name = "表名", width = 15)
    @ApiModelProperty(value = "表名")
    private String tableName;
	/**数据id*/
	@Excel(name = "数据id", width = 15)
    @ApiModelProperty(value = "数据id")
    private String tableDataId;
	/**来源用户id*/
	@Excel(name = "来源用户id", width = 15)
    @ApiModelProperty(value = "来源用户id")
    @Dict(dictTable = "sys_user", dicCode = "id", dicText = "realname")
    private String fromUserId;
	/**发送给用户id(允许为空)*/
	@Excel(name = "发送给用户id(允许为空)", width = 15)
    @ApiModelProperty(value = "发送给用户id(允许为空)")
    @Dict(dictTable = "sys_user", dicCode = "id", dicText = "realname")
    private String toUserId;
	/**评论id(允许为空，不为空时，则为回复)*/
	@Excel(name = "评论id(允许为空，不为空时，则为回复)", width = 15)
    @ApiModelProperty(value = "评论id(允许为空，不为空时，则为回复)")
    @Dict(dictTable = "sys_comment", dicCode = "id", dicText = "comment_content")
    private String commentId;
	/**回复内容*/
	@Excel(name = "回复内容", width = 15)
    @ApiModelProperty(value = "回复内容")
    private String commentContent;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
}
