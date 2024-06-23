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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 表单评论文件
 * @Author: jeecg-boot
 * @Date:   2022-07-21
 * @Version: V1.0
 */
@Data
@TableName("sys_form_file")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_form_file对象", description="表单评论文件")
public class SysFormFile {
    
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
	/**关联文件id*/
	@Excel(name = "关联文件id", width = 15)
    @ApiModelProperty(value = "关联文件id")
	private String fileId;
	/**文档类型（folder:文件夹 excel:excel doc:word pp:ppt image:图片  archive:其他文档 video:视频）*/
	@Excel(name = "文档类型（folder:文件夹 excel:excel doc:word pp:ppt image:图片  archive:其他文档 video:视频）", width = 15)
	@ApiModelProperty(value = "文档类型（folder:文件夹 excel:excel doc:word pp:ppt image:图片  archive:其他文档 video:视频）")
	private String fileType;
	/**创建人登录名称*/
	@Excel(name = "创建人登录名称", width = 15)
    @ApiModelProperty(value = "创建人登录名称")
	private String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
	private Date createTime;
}
