package org.jeecg.common.api;

import org.jeecg.common.system.vo.DynamicDataSourceModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysPermissionDataRuleModel;
import org.jeecg.common.system.vo.SysUserCacheInfo;

import java.util.List;
import java.util.Set;

public interface CommonAPI {

    /**
     * 1查询用户角色信息
     * @param username
     * @return
     */
    Set<String> queryUserRoles(String username);


    /**
     * 2查询用户权限信息
     * @param username
     * @return
     */
    Set<String> queryUserAuths(String username);

    /**
     * 3根据 id 查询数据库中存储的 DynamicDataSourceModel
     *
     * @param dbSourceId
     * @return
     */
    DynamicDataSourceModel getDynamicDbSourceById(String dbSourceId);

    /**
     * 4根据 code 查询数据库中存储的 DynamicDataSourceModel
     *
     * @param dbSourceCode
     * @return
     */
    DynamicDataSourceModel getDynamicDbSourceByCode(String dbSourceCode);

    /**
     * 5根据用户账号查询用户信息
     * @param username
     * @return
     */
    public LoginUser getUserByName(String username);


    /**
     * 6字典表的 翻译
     * @param table
     * @param text
     * @param code
     * @param key
     * @return
     */
    String translateDictFromTable(String table, String text, String code, String key);

    /**
     * 7普通字典的翻译
     * @param code
     * @param key
     * @return
     */
    String translateDict(String code, String key);

    /**
     * 8查询数据权限
     * @return
     */
    List<SysPermissionDataRuleModel> queryPermissionDataRule(String component, String requestPath, String username);


    /**
     * 9查询用户信息
     * @param username
     * @return
     */
    SysUserCacheInfo getCacheUser(String username);

}
