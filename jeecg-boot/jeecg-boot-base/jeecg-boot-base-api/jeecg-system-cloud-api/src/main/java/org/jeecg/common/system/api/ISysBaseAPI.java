package org.jeecg.common.system.api;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.api.dto.OnlineAuthDTO;
import org.jeecg.common.api.dto.message.*;
import org.jeecg.common.constant.ServiceNameConstants;
import org.jeecg.common.system.api.factory.SysBaseAPIFallbackFactory;
import org.jeecg.common.system.vo.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * 1、cloud接口数量43  local：35 common：9  额外一个特殊queryAllRole一个当两个用
 *  - 相比较local版
 *  - 去掉了一些方法：addLog、getDatabaseType、queryAllDepart、queryAllUser(Wrapper wrapper)、queryAllUser(String[] userIds, int pageNo, int pageSize)
 *  - 修改了一些方法：createLog、sendSysAnnouncement（只保留了一个，其余全部干掉）
 * 2、@ConditionalOnMissingClass("org.jeecg.modules.system.service.impl.SysBaseApiImpl")=> 有实现类的时候，不实例化Feign接口
 * @author: jeecg-boot
 */
@Component
@FeignClient(contextId = "sysBaseRemoteApi", value = ServiceNameConstants.SERVICE_SYSTEM, fallbackFactory = SysBaseAPIFallbackFactory.class)
@ConditionalOnMissingClass("org.jeecg.modules.system.service.impl.SysBaseApiImpl")
public interface ISysBaseAPI extends CommonAPI {

    /**
     * 1发送系统消息
     * @param message 使用构造器赋值参数 如果不设置category(消息类型)则默认为2 发送系统消息
     */
    @PostMapping("/sys/api/sendSysAnnouncement")
    void sendSysAnnouncement(@RequestBody MessageDTO message);

    /**
     * 2发送消息 附带业务参数
     * @param message 使用构造器赋值参数
     */
    @PostMapping("/sys/api/sendBusAnnouncement")
    void sendBusAnnouncement(@RequestBody BusMessageDTO message);

    /**
     * 3通过模板发送消息
     * @param message 使用构造器赋值参数
     */
    @PostMapping("/sys/api/sendTemplateAnnouncement")
    void sendTemplateAnnouncement(@RequestBody TemplateMessageDTO message);

    /**
     * 4通过模板发送消息 附带业务参数
     * @param message 使用构造器赋值参数
     */
    @PostMapping("/sys/api/sendBusTemplateAnnouncement")
    void sendBusTemplateAnnouncement(@RequestBody BusTemplateMessageDTO message);

    /**
     * 5通过消息中心模板，生成推送内容
     * @param templateDTO 使用构造器赋值参数
     * @return
     */
    @PostMapping("/sys/api/parseTemplateByCode")
    String parseTemplateByCode(@RequestBody TemplateDTO templateDTO);

    /**
     * 6根据用户id查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/sys/api/getUserById")
    LoginUser getUserById(@RequestParam("id") String id);

    /**
     * 7通过用户账号查询角色集合
     * @param username
     * @return
     */
    @GetMapping("/sys/api/getRolesByUsername")
    List<String> getRolesByUsername(@RequestParam("username") String username);

    /**
     * 8通过用户账号查询部门集合
     * @param username
     * @return 部门 id
     */
    @GetMapping("/sys/api/getDepartIdsByUsername")
    List<String> getDepartIdsByUsername(@RequestParam("username") String username);

    /**
     * 9通过用户账号查询部门 name
     * @param username
     * @return 部门 name
     */
    @GetMapping("/sys/api/getDepartNamesByUsername")
    List<String> getDepartNamesByUsername(@RequestParam("username") String username);

    /**
     * 10获取数据字典
     * @param code
     * @return
     */
    @Override
    @GetMapping("/sys/api/queryDictItemsByCode")
    List<DictModel> queryDictItemsByCode(@RequestParam("code") String code);

    /**
     * 获取有效的数据字典项
     * @param code
     * @return
     */
    @Override
    @GetMapping("/sys/api/queryEnableDictItemsByCode")
    public List<DictModel> queryEnableDictItemsByCode(@RequestParam("code") String code);

    /** 11查询所有的父级字典，按照create_time排序
     * @return List<DictModel> 字典值集合
     */
    @GetMapping("/sys/api/queryAllDict")
    List<DictModel> queryAllDict();

    /**
     * 12查询所有分类字典
     * @return
     */
    @GetMapping("/sys/api/queryAllSysCategory")
    List<SysCategoryModel> queryAllSysCategory();

    /**
     * 13获取表数据字典
     * @param table
     * @param text
     * @param code
     * @return
     */
    @Override
    @GetMapping("/sys/api/queryTableDictItemsByCode")
    List<DictModel> queryTableDictItemsByCode(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code);

    /**
     * 14查询所有部门 作为字典信息 id -->value,departName -->text
     * @return
     */
    @GetMapping("/sys/api/queryAllDepartBackDictModel")
    List<DictModel> queryAllDepartBackDictModel();

    /**
     * 15根据业务类型 busType 及业务 busId 修改消息已读
     * @param busType 业务类型
     * @param busId 业务id
     */
    @GetMapping("/sys/api/updateSysAnnounReadFlag")
    public void updateSysAnnounReadFlag(@RequestParam("busType") String busType, @RequestParam("busId")String busId);

    /**
     * 16查询表字典 支持过滤数据
     * @param table
     * @param text
     * @param code
     * @param filterSql
     * @return
     */
    @GetMapping("/sys/api/queryFilterTableDictInfo")
    List<DictModel> queryFilterTableDictInfo(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code, @RequestParam("filterSql") String filterSql);

    /**
     * 17查询指定table的 text code 获取字典，包含text和value
     * @param table
     * @param text
     * @param code
     * @param keyArray
     * @return
     */
    @Deprecated
    @GetMapping("/sys/api/queryTableDictByKeys")
    public List<String> queryTableDictByKeys(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code, @RequestParam("keyArray") String[] keyArray);

    /**
     * 18查询所有用户 返回ComboModel
     * @return
     */
    @GetMapping("/sys/api/queryAllUserBackCombo")
    public List<ComboModel> queryAllUserBackCombo();

    /**
     * 19分页查询用户 返回JSONObject
     * @param userIds 多个用户id
     * @param pageNo 当前页数
     * @param pageSize 每页条数
     * @return
     */
    @GetMapping("/sys/api/queryAllUser")
    public JSONObject queryAllUser(@RequestParam(name="userIds",required=false)String userIds, @RequestParam(name="pageNo",required=false) Integer pageNo,@RequestParam(name="pageSize",required=false) int pageSize);


    /**
     * 20获取所有角色 带参
     * @param roleIds 默认选中角色
     * @return
     */
    @GetMapping("/sys/api/queryAllRole")
    public List<ComboModel> queryAllRole(@RequestParam(name = "roleIds",required = false)String[] roleIds);

    /**
     * 21通过用户账号查询角色Id集合
     * @param username
     * @return
     */
    @GetMapping("/sys/api/getRoleIdsByUsername")
    public List<String> getRoleIdsByUsername(@RequestParam("username")String username);

    /**
     * 22通过部门编号查询部门id
     * @param orgCode
     * @return
     */
    @GetMapping("/sys/api/getDepartIdsByOrgCode")
    public String getDepartIdsByOrgCode(@RequestParam("orgCode")String orgCode);

    /**
     * 23查询所有部门
     * @return
     */
    @GetMapping("/sys/api/getAllSysDepart")
    public List<SysDepartModel> getAllSysDepart();

    /**
     * 24查找父级部门
     * @param departId
     * @return
     */
    @GetMapping("/sys/api/getParentDepartId")
    DictModel getParentDepartId(@RequestParam("departId")String departId);

    /**
     * 25根据部门Id获取部门负责人
     * @param deptId
     * @return
     */
    @GetMapping("/sys/api/getDeptHeadByDepId")
    public List<String> getDeptHeadByDepId(@RequestParam("deptId") String deptId);

    /**
     * 26给指定用户发消息
     * @param userIds
     * @param cmd
     */
    @GetMapping("/sys/api/sendWebSocketMsg")
    public void sendWebSocketMsg(@RequestParam("userIds")String[] userIds, @RequestParam("cmd") String cmd);

    /**
     * 27根据id获取所有参与用户
     * @param userIds 多个用户id
     * @return
     */
    @GetMapping("/sys/api/queryAllUserByIds")
    public List<LoginUser> queryAllUserByIds(@RequestParam("userIds") String[] userIds);

    /**
     * 28将会议签到信息推动到预览
     * userIds
     * @return
     * @param userId
     */
    @GetMapping("/sys/api/meetingSignWebsocket")
    void meetingSignWebsocket(@RequestParam("userId")String userId);

    /**
     * 29根据name获取所有参与用户
     * @param userNames 多个用户账号
     * @return
     */
    @GetMapping("/sys/api/queryUserByNames")
    List<LoginUser> queryUserByNames(@RequestParam("userNames")String[] userNames);


    /**
     * 30获取用户的角色集合
     * @param username
     * @return
     */
    @GetMapping("/sys/api/getUserRoleSet")
    Set<String> getUserRoleSet(@RequestParam("username")String username);

    /**
     * 31获取用户的权限集合
     * @param username
     * @return
     */
    @GetMapping("/sys/api/getUserPermissionSet")
    Set<String> getUserPermissionSet(@RequestParam("username") String username);

    /**
     * 32判断是否有online访问的权限
     * @param onlineAuthDTO
     * @return
     */
    @PostMapping("/sys/api/hasOnlineAuth")
    boolean hasOnlineAuth(@RequestBody OnlineAuthDTO onlineAuthDTO);

    /**
     * 33通过部门id获取部门全部信息
     * @param id 部门id
     * @return SysDepartModel 部门信息
     */
    @GetMapping("/sys/api/selectAllById")
    SysDepartModel selectAllById(@RequestParam("id") String id);

    /**
     * 34根据用户id查询用户所属公司下所有用户ids
     * @param userId
     * @return
     */
    @GetMapping("/sys/api/queryDeptUsersByUserId")
    List<String> queryDeptUsersByUserId(@RequestParam("userId") String userId);


    //---

    /**
     * 35查询用户角色信息
     * @param username
     * @return
     */
    @Override
    @GetMapping("/sys/api/queryUserRoles")
    Set<String> queryUserRoles(@RequestParam("username")String username);

    /**
     * 36查询用户权限信息
     * @param username
     * @return
     */
    @Override
    @GetMapping("/sys/api/queryUserAuths")
    Set<String> queryUserAuths(@RequestParam("username")String username);

    /**
     * 37根据 id 查询数据库中存储的 DynamicDataSourceModel
     *
     * @param dbSourceId
     * @return
     */
    @Override
    @GetMapping("/sys/api/getDynamicDbSourceById")
    DynamicDataSourceModel getDynamicDbSourceById(@RequestParam("dbSourceId") String dbSourceId);

    /**
     * 38根据 code 查询数据库中存储的 DynamicDataSourceModel
     *
     * @param dbSourceCode
     * @return
     */
    @Override
    @GetMapping("/sys/api/getDynamicDbSourceByCode")
    DynamicDataSourceModel getDynamicDbSourceByCode(@RequestParam("dbSourceCode") String dbSourceCode);

    /**
     * 39根据用户账号查询用户信息 CommonAPI中定义
     * @param username
     * @return LoginUser 用户信息
     */
    @Override
    @GetMapping("/sys/api/getUserByName")
    LoginUser getUserByName(@RequestParam("username") String username);

    /**
     * 40字典表的 翻译
     * @param table
     * @param text
     * @param code
     * @param key
     * @return
     */
    @Override
    @GetMapping("/sys/api/translateDictFromTable")
    String translateDictFromTable(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code, @RequestParam("key") String key);

    /**
     * 41普通字典的翻译
     * @param code
     * @param key
     * @return
     */
    @Override
    @GetMapping("/sys/api/translateDict")
    String translateDict(@RequestParam("code") String code, @RequestParam("key") String key);

    /**
     * 42查询数据权限
     * @param component
     * @param requestPath
     * @param username 用户姓名
     * @return
     */
    @Override
    @GetMapping("/sys/api/queryPermissionDataRule")
    List<SysPermissionDataRuleModel> queryPermissionDataRule(@RequestParam("component") String component, @RequestParam("requestPath")String requestPath, @RequestParam("username") String username);

    /**
     * 43查询用户信息
     * @param username
     * @return
     */
    @Override
    @GetMapping("/sys/api/getCacheUser")
    SysUserCacheInfo getCacheUser(@RequestParam("username") String username);

    /**
     * 36根据多个用户账号(逗号分隔)，查询返回多个用户信息
     * @param usernames
     * @return
     */
    @GetMapping("/sys/api/queryUsersByUsernames")
    List<JSONObject> queryUsersByUsernames(@RequestParam("usernames") String usernames);

    /**
     * 37根据多个用户ID(逗号分隔)，查询返回多个用户信息
     * @param ids
     * @return
     */
    @RequestMapping("/sys/api/queryUsersByIds")
    List<JSONObject> queryUsersByIds(@RequestParam("ids") String ids);

    /**
     * 38根据多个部门编码(逗号分隔)，查询返回多个部门信息
     * @param orgCodes
     * @return
     */
    @RequestMapping("/sys/api/queryDepartsByOrgcodes")
    List<JSONObject> queryDepartsByOrgcodes(@RequestParam("orgCodes") String orgCodes);

    /**
     * 39根据多个部门编码(逗号分隔)，查询返回多个部门信息
     * @param ids
     * @return
     */
    @GetMapping("/sys/api/queryDepartsByOrgIds")
    List<JSONObject> queryDepartsByOrgIds(@RequestParam("ids") String ids);
    
    /**
     * 40发送邮件消息
     * @param email
     * @param title
     * @param content
     */
    @GetMapping("/sys/api/sendEmailMsg")
    void sendEmailMsg(@RequestParam("email")String email,@RequestParam("title")String title,@RequestParam("content")String content);
    /**
     * 41 获取公司下级部门和公司下所有用户id
     * @param orgCode 部门编号
     * @return List<Map>
     */
    @GetMapping("/sys/api/getDeptUserByOrgCode")
    List<Map> getDeptUserByOrgCode(@RequestParam("orgCode")String orgCode);

    /**
     * 42 查询分类字典翻译
     * @param ids 多个分类字典id
     * @return List<String>
     */
    @GetMapping("/sys/api/loadCategoryDictItem")
    List<String> loadCategoryDictItem(@RequestParam("ids") String ids);

    /**
     * 43 根据字典code加载字典text
     *
     * @param dictCode 顺序：tableName,text,code
     * @param keys     要查询的key
     * @return
     */
    @GetMapping("/sys/api/loadDictItem")
    List<String> loadDictItem(@RequestParam("dictCode") String dictCode, @RequestParam("keys") String keys);

    /**
     * 44 根据字典code查询字典项
     *
     * @param dictCode 顺序：tableName,text,code
     * @param dictCode 要查询的key
     * @return
     */
    @GetMapping("/sys/api/getDictItems")
    List<DictModel> getDictItems(@RequestParam("dictCode") String dictCode);

    /**
     * 45 根据多个字典code查询多个字典项
     *
     * @param dictCodeList
     * @return key = dictCode ； value=对应的字典项
     */
    @RequestMapping("/sys/api/getManyDictItems")
    Map<String, List<DictModel>> getManyDictItems(@RequestParam("dictCodeList") List<String> dictCodeList);

    /**
     * 46 【JSearchSelectTag下拉搜索组件专用接口】
     * 大数据量的字典表 走异步加载  即前端输入内容过滤数据
     *
     * @param dictCode 字典code格式：table,text,code
     * @param keyword  过滤关键字
     * @param pageSize 每页条数
     * @return
     */
    @GetMapping("/sys/api/loadDictItemByKeyword")
    List<DictModel> loadDictItemByKeyword(@RequestParam("dictCode") String dictCode, @RequestParam("keyword") String keyword, @RequestParam(value = "pageSize", required = false) Integer pageSize);

    /**
     * 47 根据多个部门id(逗号分隔)，查询返回多个部门信息
     * @param ids
     * @return
     */
    @GetMapping("/sys/api/queryDepartsByIds")
    List<JSONObject> queryDepartsByIds(@RequestParam("ids") String ids);

    /**
     * 48 普通字典的翻译，根据多个dictCode和多条数据，多个以逗号分割
     * @param dictCodes
     * @param keys
     * @return
     */
    @Override
    @GetMapping("/sys/api/translateManyDict")
    Map<String, List<DictModel>> translateManyDict(@RequestParam("dictCodes") String dictCodes, @RequestParam("keys") String keys);

    /**
     * 49 字典表的 翻译，可批量
     * @param table
     * @param text
     * @param code
     * @param keys 多个用逗号分割
     * @return
     */
    @Override
    @GetMapping("/sys/api/translateDictFromTableByKeys")
    List<DictModel> translateDictFromTableByKeys(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code, @RequestParam("keys") String keys);

}
