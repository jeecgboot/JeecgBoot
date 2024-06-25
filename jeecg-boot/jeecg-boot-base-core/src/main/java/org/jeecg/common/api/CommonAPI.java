package org.jeecg.common.api;

import org.jeecg.common.system.vo.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 通用api
 * @author: jeecg-boot
 */
public interface CommonAPI {

    /**
     * 1查询用户角色信息
     * @param username
     * @return
     */
    Set<String> queryUserRoles(String username);
    
    /**
     * 1查询用户角色信息
     * @param userId
     * @return
     */
    Set<String> queryUserRolesById(String userId);


    /**
     * 2查询用户权限信息
     * @param userId
     * @return
     */
    Set<String> queryUserAuths(String userId);

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
     * 5根据用户账号查询用户Id
     * @param username
     * @return
     */
    public String getUserIdByName(String username);


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
     * @param component 组件
     * @param username 用户名
     * @param requestPath 前段请求地址
     * @return
     */
    List<SysPermissionDataRuleModel> queryPermissionDataRule(String component, String requestPath, String username);


    /**
     * 9查询用户信息
     * @param username
     * @return
     */
    SysUserCacheInfo getCacheUser(String username);

    /**
     * 10获取数据字典
     * @param code
     * @return
     */
    public List<DictModel> queryDictItemsByCode(String code);

    /**
     * 获取有效的数据字典项
     * @param code
     * @return
     */
    public List<DictModel> queryEnableDictItemsByCode(String code);

    /**
     * 13获取表数据字典
     * @param tableFilterSql
     * @param text
     * @param code
     * @return
     */
    List<DictModel> queryTableDictItemsByCode(String tableFilterSql, String text, String code);

    /**
     * 14 普通字典的翻译，根据多个dictCode和多条数据，多个以逗号分割
     * @param dictCodes 例如：user_status,sex
     * @param keys 例如：1,2,0
     * @return
     */
    Map<String, List<DictModel>> translateManyDict(String dictCodes, String keys);

    //update-begin---author:chenrui ---date:20231221  for：[issues/#5643]解决分布式下表字典跨库无法查询问题------------
    /**
     * 15 字典表的 翻译，可批量
     * @param table
     * @param text
     * @param code
     * @param keys 多个用逗号分割
     * @param dataSource 数据源
     * @return
     */
    List<DictModel> translateDictFromTableByKeys(String table, String text, String code, String keys, String dataSource);
    //update-end---author:chenrui ---date:20231221  for：[issues/#5643]解决分布式下表字典跨库无法查询问题------------

}
