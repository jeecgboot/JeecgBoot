package org.jeecg.modules.online.cgreport.def;
/**
 * 
 * @Title:CgReportConstant
 * @description:动态报表常量类
 * @author 赵俊夫
 * @date Jul 30, 2013 9:16:18 AM
 * @version V1.0
 */
public class CgReportConstant {
	/** 抬头配置 */
	public static final String MAIN=  "main";
	/** 明细配置*/
	public static final String ITEMS=  "items";
	/** 动态报表参数*/
	public static final String PARAMS=  "params";
	/**	配置的id*/
	public static final String CONFIG_ID="config_id";
	/**	配置的名称*/
	public static final String CONFIG_NAME="config_name";
	/**	字段列表*/
	public static final String CONFIG_FIELDLIST = "config_fieldList";
	/**	查询字段*/
	public static final String CONFIG_QUERYLIST ="config_queryList";
	/**	动态报表参数*/
	public static final String CONFIG_PARAMS ="config_params";
	/**字典数据*/
	public static final String FIELD_DICTLIST = "field_dictlist";
	/**系统字典表*/
	public static final String SYS_DIC="t_s_type";
	/**系统字典分组表*/
	public static final String SYS_DICGROUP="t_s_typegroup";
	/** 抬头配置-查询SQL*/
	public static final String CONFIG_SQL=  "cgreport_sql";
	/** 字典编码*/
	public static final String ITEM_DICCODE= "dict_code";
	/** 取值表达式*/
	public static final String ITEM_REPLACE = "replace_value";
	/** 明细配置-字段名*/
	public static final String ITEM_FIELDNAME =  "field_name";
	/** 明细配置-是否查询*/
	public static final String ITEM_ISQUERY =  "search_flag";
	/** 明细配置-字段类型*/
	public static final String ITEM_FIELDTYPE =  "field_type";
	/** 明细配置-查询模式*/
	public static final String ITEM_QUERYMODE =  "search_mode";
	/**逻辑true*/
	public static final String BOOL_TRUE= "Y";
	/**逻辑false*/
	public static final String BOOL_FALSE= "N";
	/**查询操作=*/
	public static final String OP_EQ = " = ";
	/**查询操作>=*/
	public static final String OP_RQ = " >= ";
	/**查询操作<=*/
	public static final String OP_LQ = " <= ";
	/**查询操作like*/
	public static final String OP_LIKE = " LIKE ";
	/**显示模式Date*/
	public static final String TYPE_DATE = "Date";
	/**显示模式String*/
	public static final String TYPE_STRING = "String";
	/**显示模式Integer*/
	public static final String TYPE_INTEGER = "Integer";
	/**显示模式Double*/
	public static final String TYPE_DOUBLE = "Double";
	
	/** iframe */
	public static final String CONFIG_IFRAME = "config_iframe";
}
