package org.jeecg.modules.system.constant;

/**
 * 默认首页常量
 */
public interface DefIndexConst {

    /**
     * 默认首页的roleCode
     */
    String DEF_INDEX_ALL = "DEF_INDEX_ALL";

    /**
     * 默认首页的缓存key
     */
    String CACHE_KEY = "sys:cache:def_index";
    /**
     * 缓存默认首页的类型前缀
     */
    String CACHE_TYPE = "sys:cache:home_type::";
    /**
     * 默认首页类型
     */
    String HOME_TYPE_SYSTEM = "system";
    String HOME_TYPE_PERSONAL = "personal";
    String HOME_TYPE_MENU = "menuHome";

    /**
     * 默认首页的初始值
     */
    String DEF_INDEX_NAME = "首页";
    String DEF_INDEX_URL = "/dashboard/analysis";
    String DEF_INDEX_COMPONENT = "dashboard/Analysis";

}
