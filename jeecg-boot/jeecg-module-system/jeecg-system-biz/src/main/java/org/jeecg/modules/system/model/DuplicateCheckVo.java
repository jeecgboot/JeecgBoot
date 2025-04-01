package org.jeecg.modules.system.model;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Title: DuplicateCheckVo
 * @Description: 重复校验VO
 * @Author 张代浩
 * @Date 2019-03-25
 * @Version V1.0
 */
@Data
@Schema(description="重复校验数据模型")
public class DuplicateCheckVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 表名
	 */
	@Schema(description="表名",name="tableName",example="sys_log")
	private String tableName;
	
	/**
	 * 字段名
	 */
	@Schema(description="字段名",name="fieldName",example="id")
	private String fieldName;
	
	/**
	 * 字段值
	 */
	@Schema(description="字段值",name="fieldVal",example="1000")
	private String fieldVal;
	
	/**
	 * 数据ID
	*/
	@Schema(description="数据ID",name="dataId",example="2000")
	private String dataId;

}