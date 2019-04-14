package org.jeecg.modules.message.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 消息模板
 * @author： jeecg-boot
 * @date：   2019-04-09
 * @version： V1.0
 */
@Data
@TableName("sys_sms_template")
public class SysMessageTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**模板CODE*/
	@Excel(name = "模板CODE", width = 15)
	private java.lang.String templateCode;
	/**模板内容*/
	@Excel(name = "模板内容", width = 15)
	private java.lang.String templateContent;
	/**模板标题*/
	@Excel(name = "模板标题", width = 15)
	private java.lang.String templateName;
	/**模板测试json*/
	@Excel(name = "模板测试json", width = 15)
	private java.lang.String templateTestJson;
	/**模板类型*/
	@Excel(name = "模板类型", width = 15)
	private java.lang.String templateType;
	/**创建人登录名称*/
	@Excel(name = "创建人登录名称", width = 15)
	private java.lang.String createBy;
	/**创建日期*/
	@Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**更新人登录名称*/
	@Excel(name = "更新人登录名称", width = 15)
	private java.lang.String updateBy;
	/**更新日期*/
	@Excel(name = "更新日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
}
