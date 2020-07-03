package org.jeecg.common.constant;
/**
 * 数据库上下文常量
 */
public interface DataBaseConstant {
	//*********数据库类型****************************************
	String DB_TYPE_MYSQL = "MYSQL";
	String DB_TYPE_ORACLE = "ORACLE";
	String DB_TYPE_POSTGRESQL = "POSTGRESQL";
	String DB_TYPE_SQLSERVER = "SQLSERVER";

	// 数据库类型，对应 database_type 字典
	String DB_TYPE_MYSQL_NUM = "1";
	String DB_TYPE_ORACLE_NUM = "2";
	String DB_TYPE_SQLSERVER_NUM = "3";
	String DB_TYPE_POSTGRESQL_NUM = "4";
	
	
	//*********系统建表标准字段****************************************
	/**
	 * 创建者登录名称
	 */
	String CREATE_BY_TABLE = "create_by";
	/**
	 * 创建者登录名称
	 */
	String CREATE_BY = "createBy";
	/**
	 * 创建日期时间
	 */
	String CREATE_TIME_TABLE = "create_time";
	/**
	 * 创建日期时间
	 */
	String CREATE_TIME = "createTime";
	/**
	 * 更新用户登录名称
	 */
	String UPDATE_BY_TABLE = "update_by";
	/**
	 * 更新用户登录名称
	 */
	String UPDATE_BY = "updateBy";
	/**
	 * 更新日期时间
	 */
	String UPDATE_TIME = "updateTime";
	/**
	 * 更新日期时间
	 */
	String UPDATE_TIME_TABLE = "update_time";
	
	/**
	 * 业务流程状态
	 */
	String BPM_STATUS = "bpmStatus";
	/**
	 * 业务流程状态
	 */
	String BPM_STATUS_TABLE = "bpm_status";
	//*********系统建表标准字段****************************************
}
