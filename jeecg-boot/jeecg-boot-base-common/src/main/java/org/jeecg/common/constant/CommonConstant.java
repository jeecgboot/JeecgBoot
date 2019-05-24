package org.jeecg.common.constant;

public interface CommonConstant {

	/**
	 * 正常状态
	 */
	Integer STATUS_NORMAL = 0;

	/**
	 * 禁用状态
	 */
	Integer STATUS_DISABLE = -1;

	/**
	 * 删除标志
	 */
	Integer DEL_FLAG_1 = 1;

	/**
	 * 未删除
	 */
	Integer DEL_FLAG_0 = 0;

	/**
	 * 系统日志类型： 登录
	 */
	int LOG_TYPE_1 = 1;

	/**
	 * 系统日志类型： 操作
	 */
	int LOG_TYPE_2 = 2;
	
	
	/** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_OK_200 = 200;

    /** 登录用户拥有角色缓存KEY前缀 */
    public static String LOGIN_USER_CACHERULES_ROLE = "loginUser_cacheRules::Roles_";
    /** 登录用户拥有权限缓存KEY前缀 */
    public static String LOGIN_USER_CACHERULES_PERMISSION  = "loginUser_cacheRules::Permissions_";
    /** 登录用户令牌缓存KEY前缀 */
    public static int  TOKEN_EXPIRE_TIME  = 3600; //3600秒即是一小时
    
    public static String PREFIX_USER_TOKEN  = "PREFIX_USER_TOKEN_";
    
    /**
     *  0：一级菜单
     */
    public static Integer MENU_TYPE_0  = 0;
   /**
    *  1：子菜单 
    */
    public static Integer MENU_TYPE_1  = 1;
    /**
     *  2：按钮权限
     */
    public static Integer MENU_TYPE_2  = 2;
    
    /**通告对象类型（USER:指定用户，ALL:全体用户）*/
    public static String MSG_TYPE_UESR  = "USER";
    public static String MSG_TYPE_ALL  = "ALL";
    
    /**发布状态（0未发布，1已发布，2已撤销）*/
    public static String NO_SEND  = "0";
    public static String HAS_SEND  = "1";
    public static String HAS_CANCLE  = "2";
    
    /**阅读状态（0未读，1已读）*/
    public static String HAS_READ_FLAG  = "1";
    public static String NO_READ_FLAG  = "0";
    
    /**优先级（L低，M中，H高）*/
    public static String PRIORITY_L  = "L";
    public static String PRIORITY_M  = "M ";
    public static String PRIORITY_H  = "H";
}
