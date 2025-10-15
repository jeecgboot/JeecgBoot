package org.jeecg.modules.system.entity;

import java.util.Date;

import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新人
	 */
	private String updateBy;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 耗时
	 */
	@Excel(name = "耗时（毫秒）", width = 15)
	private Long costTime;

	/**
	 * IP
	 */
	@Excel(name = "IP", width = 15)
	private String ip;

	/**
	 * 请求参数
	 */
	private String requestParam;

	/**
	 * 请求类型
	 */
	private String requestType;

	/**
	 * 请求路径
	 */
	private String requestUrl;
	/**
	 * 请求方法
	 */
	private String method;

	/**
	 * 操作人用户名称
	 */
	private String username;
	/**
	 * 操作人用户账户
	 */
	@Excel(name = "操作人", width = 15)
	private String userid;
	/**
	 * 操作详细日志
	 */
	@Excel(name = "日志内容", width = 50)
	private String logContent;

	/**
	 * 日志类型（1登录日志，2操作日志）
	 */
	@Dict(dicCode = "log_type")
	private Integer logType;

	/**
	 * 操作类型（1查询，2添加，3修改，4删除,5导入，6导出）
	 */
	@Dict(dicCode = "operate_type")
	private Integer operateType;
	
	/**
	 * 客户终端类型 pc:电脑端 app:手机端 h5:移动网页端
	 */
	@Excel(name = "客户端类型", width = 15, dicCode = "client_type")
	@Dict(dicCode = "client_type")
	private String clientType;

	/**
	 * 租户ID
	 */
	private Integer tenantId;

}
