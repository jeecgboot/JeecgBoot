package org.jeecg.common.constant;

/**
 * @Description: TenantConstant
 * @author: scott
 * @date: 2022年08月29日 15:29
 */
public interface TenantConstant {
    /*------【低代码应用参数】----------------------------------------------*/
    /**
     * header的lowAppId标识
     */
    String X_LOW_APP_ID = "X-Low-App-ID";
    /**
     * 应用ID——实体字段
     */
    String FIELD_LOW_APP_ID = "lowAppId";
    /**
     * 应用ID——表字段
     */
    String DB_FIELD_LOW_APP_ID = "low_app_id";
    /*------【低代码应用参数】---------------------------------------------*/

    /*--------【租户参数】-----------------------------------------------*/
    /**
     * 租户ID（实体字段名 和 url参数名）
     */
    String TENANT_ID = "tenantId";
    /**
     * 租户ID 数据库字段名
     */
    String TENANT_ID_TABLE = "tenant_id";
    /*-------【租户参数】-----------------------------------------------*/

    /**
     * 超级管理员
     */
    String SUPER_ADMIN = "superAdmin";

    /**
     * 组织账户管理员
     */
    String ACCOUNT_ADMIN = "accountAdmin";

    /**
     * 组织应用管理员
     */
    String APP_ADMIN = "appAdmin";

}
