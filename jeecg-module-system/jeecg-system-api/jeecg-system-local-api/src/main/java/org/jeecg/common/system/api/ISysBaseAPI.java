package org.jeecg.common.system.api;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.api.dto.DataLogDTO;
import org.jeecg.common.api.dto.OnlineAuthDTO;
import org.jeecg.common.api.dto.message.*;
import org.jeecg.common.constant.enums.EmailTemplateEnum;
import org.jeecg.common.system.vo.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description  底层共通业务API，提供其他独立模块调用
 * @Author  scott
 * @Date 2019-4-20
 * @Version V1.0
 */
public interface ISysBaseAPI extends CommonAPI {

    //=======OLD 系统消息推送接口============================
    /**
     * 1发送系统消息
     * @param message 使用构造器赋值参数 如果不设置category(消息类型)则默认为2 发送系统消息
     */
    void sendSysAnnouncement(MessageDTO message);

    /**
     * 2发送消息 附带业务参数
     * @param message 使用构造器赋值参数
     */
    void sendBusAnnouncement(BusMessageDTO message);

    /**
     * 3通过模板发送消息
     * @param message 使用构造器赋值参数
     */
    void sendTemplateAnnouncement(TemplateMessageDTO message);

    /**
     * 4通过模板发送消息 附带业务参数
     * @param message 使用构造器赋值参数
     */
    void sendBusTemplateAnnouncement(BusTemplateMessageDTO message);
    
    /**
     * 5通过消息中心模板，生成推送内容
     * @param templateDTO 使用构造器赋值参数
     * @return
     */
    String parseTemplateByCode(TemplateDTO templateDTO);
    //=======OLD 系统消息推送接口============================

    //=======TY NEW 自定义消息推送接口，邮件、钉钉、企业微信、系统消息============================
    /**
     * NEW发送模板消息【新，支持自定义推送类型: 邮件、钉钉、企业微信、系统消息】
     * @param message
     */
    void sendTemplateMessage(MessageDTO message);

    /**
     * NEW根据模板编码获取模板内容【新，支持自定义推送类型】
     * @param templateCode
     * @return
     */
    String getTemplateContent(String templateCode);
    //=======TY NEW 自定义消息推送接口，邮件、钉钉、企业微信、系统消息============================
    
    /**
     * 6根据用户id查询用户信息
     * @param id
     * @return
     */
    LoginUser getUserById(String id);

    /**
     * 7通过用户账号查询角色集合
     * @param username
     * @return
     */
    List<String> getRolesByUsername(String username);
    
    /**
     * 7通过用户账号查询角色集合
     * @param userId
     * @return
     */
    List<String> getRolesByUserId(String userId);

    /**
     * 8通过用户账号查询部门集合
     * @param username
     * @return 部门 id
     */
    List<String> getDepartIdsByUsername(String username);
    /**
     * 8通过用户账号查询部门集合
     * @param userId
     * @return 部门 id
     */
    List<String> getDepartIdsByUserId(String userId);

    /**
     * 8.2 通过用户账号查询部门父ID集合
     * @param username
     * @return 部门 parentIds
     */
    Set<String> getDepartParentIdsByUsername(String username);

    /**
     * 8.2 查询部门父ID集合
     * @param depIds
     * @return 部门 parentIds
     */
    Set<String> getDepartParentIdsByDepIds(Set<String> depIds);

    /**
     * 9通过用户账号查询部门 name
     * @param username
     * @return 部门 name
     */
    List<String> getDepartNamesByUsername(String username);



    /** 11查询所有的父级字典，按照create_time排序
     * @return List<DictModel> 字典集合
     */
    public List<DictModel> queryAllDict();

    /**
     * 12查询所有分类字典
     * @return
     */
    public List<SysCategoryModel> queryAllSysCategory();


    /**
     * 14查询所有部门 作为字典信息 id -->value,departName -->text
     * @return
     */
    public List<DictModel> queryAllDepartBackDictModel();

    /**
     * 15根据业务类型及业务id修改消息已读
     * @param busType
     * @param busId
     */
    public void updateSysAnnounReadFlag(String busType, String busId);

    /**
     * 16查询表字典 支持过滤数据
     * @param table
     * @param text
     * @param code
     * @param filterSql
     * @return
     */
    public List<DictModel> queryFilterTableDictInfo(String table, String text, String code, String filterSql);

    /**
     * 17查询指定table的 text code 获取字典，包含text和value
     * @param table
     * @param text
     * @param code
     * @param keyArray
     * @return
     */
    @Deprecated
    public List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray);

    /**
     * 18查询所有用户 返回ComboModel
     * @return
     */
    public List<ComboModel> queryAllUserBackCombo();

    /**
     * 19分页查询用户 返回JSONObject
     * @param userIds 多个用户id
     * @param pageNo 当前页数
     * @param pageSize 每页显示条数
     * @return
     */
    public JSONObject queryAllUser(String userIds, Integer pageNo, Integer pageSize);

    /**
     * 20获取所有角色
     * @return
     */
    public List<ComboModel> queryAllRole();

    /**
     * 21获取所有角色 带参
     * @param roleIds 默认选中角色
     * @return
     */
    public List<ComboModel> queryAllRole(String[] roleIds );

    /**
     * 22通过用户账号查询角色Id集合
     * @param username
     * @return
     */
    public List<String> getRoleIdsByUsername(String username);

    /**
     * 23通过部门编号查询部门id
     * @param orgCode
     * @return
     */
    public String getDepartIdsByOrgCode(String orgCode);

    /**
     * 24查询所有部门
     * @return
     */
    public List<SysDepartModel> getAllSysDepart();

    /**
     * 25查找父级部门
     * @param departId
     * @return
     */
    DictModel getParentDepartId(String departId);

    /**
     * 26根据部门Id获取部门负责人
     * @param deptId
     * @return
     */
    public List<String> getDeptHeadByDepId(String deptId);

    /**
     * 27给指定用户发消息
     * @param userIds
     * @param cmd
     */
    public void sendWebSocketMsg(String[] userIds, String cmd);

    /**
     * 28根据id获取所有参与用户
     * @param userIds 多个用户id
     * @return
     */
    public List<UserAccountInfo> queryAllUserByIds(String[] userIds);

    /**
     * 29将会议签到信息推动到预览
     * userIds
     * @return
     * @param userId
     */
    void meetingSignWebsocket(String userId);

    /**
     * 30根据name获取所有参与用户
     * @param userNames 多个用户账户
     * @return
     */
    List<UserAccountInfo> queryUserByNames(String[] userNames);


    /**
     * 根据高级查询条件查询用户
     * @param superQuery
     * @param matchType
     * @return
     */
    List<JSONObject> queryUserBySuperQuery(String superQuery,String matchType);


    /**
     * 根据ID查询用户
     * @param id
     * @return
     */
    JSONObject queryUserById(String id);


    /**
     * 根据高级查询条件查询部门
     * @param superQuery
     * @param matchType
     * @return
     */
    List<JSONObject> queryDeptBySuperQuery(String superQuery,String matchType);

    /**
     * 根据高级查询条件查询角色
     * @param superQuery
     * @param matchType
     * @return
     */
    List<JSONObject> queryRoleBySuperQuery(String superQuery,String matchType);


    /**
     * 根据租户ID查询用户ID
     * @param tenantId 租户ID
     * @return List<String>
     */
    List<String> selectUserIdByTenantId(String tenantId);



    /**
     * 31获取用户的角色集合
     * @param username
     * @return
     */
    Set<String> getUserRoleSet(String username);
    /**
     * 31获取用户的角色集合
     * @param useId
     * @return
     */
    Set<String> getUserRoleSetById(String useId);
    
    /**
     * 32获取用户的权限集合
     * @param userId
     * @return
     */
    Set<String> getUserPermissionSet(String userId);

    /**
     * 33判断是否有online访问的权限
     * @param onlineAuthDTO
     * @return
     */
    boolean hasOnlineAuth(OnlineAuthDTO onlineAuthDTO);

    /**
     * 34通过部门id获取部门全部信息
     * @param id 部门id
     * @return SysDepartModel对象
     */
    SysDepartModel selectAllById(String id);

    /**
     * 35根据用户id查询用户所属公司下所有用户ids
     * @param userId
     * @return
     */
    List<String> queryDeptUsersByUserId(String userId);

    /**
     * 36根据多个用户账号(逗号分隔)，查询返回多个用户信息
     * @param usernames
     * @return
     */
    List<JSONObject> queryUsersByUsernames(String usernames);

    /**
     * 37根据多个用户ID(逗号分隔)，查询返回多个用户信息
     * @param ids
     * @return
     */
    List<JSONObject> queryUsersByIds(String ids);

    /**
     * 38根据多个部门编码(逗号分隔)，查询返回多个部门信息
     * @param orgCodes
     * @return
     */
    List<JSONObject> queryDepartsByOrgcodes(String orgCodes);

    /**
     * 39根据多个部门id(逗号分隔)，查询返回多个部门信息
     * @param ids
     * @return
     */
    List<JSONObject> queryDepartsByIds(String ids);

    /**
     * 40发送邮件消息
     * @param email
     * @param title
     * @param content
     */
    void sendEmailMsg(String email,String title,String content);

    /**
     * 40发送模版邮件消息
     *
     * @param email             接收邮箱
     * @param title             邮件标题
     * @param emailTemplateEnum 邮件模版枚举
     * @param params            模版参数
     */
    void sendHtmlTemplateEmail(String email, String title, EmailTemplateEnum emailTemplateEnum, JSONObject params);
    /**
     * 41 获取公司下级部门和公司下所有用户信息
     * @param orgCode
     * @return List<Map>
     */
    List<Map> getDeptUserByOrgCode(String orgCode);

    /**
     * 查询分类字典翻译
     * @param ids 多个分类字典id
     * @return List<String>
     */
    List<String> loadCategoryDictItem(String ids);

    /**
     * 反向翻译分类字典，用于导入
     *
     * @param names 名称，逗号分割
     */
    List<String> loadCategoryDictItemByNames(String names, boolean delNotExist);

    /**
     * 根据字典code加载字典text
     *
     * @param dictCode 顺序：tableName,text,code
     * @param keys     要查询的key
     * @return
     */
    List<String> loadDictItem(String dictCode, String keys);

    /**
     * 复制应用下的所有字典配置到新的租户下
     * 
     * @param originalAppId  原始低代码应用ID
     * @param appId   新的低代码应用ID
     * @param tenantId  新的租户ID
     * @return  Map<String, String>  Map<原字典编码, 新字典编码> 
     */
    Map<String, String> copyLowAppDict(String originalAppId, String appId, String tenantId);

    /**
     * 根据字典code查询字典项
     *
     * @param dictCode 顺序：tableName,text,code
     * @param dictCode 要查询的key
     * @return
     */
    List<DictModel> getDictItems(String dictCode);

    /**
     *  根据多个字典code查询多个字典项
     * @param dictCodeList
     * @return key = dictCode ； value=对应的字典项
     */
    Map<String, List<DictModel>> getManyDictItems(List<String> dictCodeList);

    /**
     * 【JSearchSelectTag下拉搜索组件专用接口】
     * 大数据量的字典表 走异步加载  即前端输入内容过滤数据
     *
     * @param dictCode 字典code格式：table,text,code
     * @param keyword 过滤关键字
     * @param pageSize 分页条数
     * @return
     */
    List<DictModel> loadDictItemByKeyword(String dictCode, String keyword, Integer pageSize);

    /**
     * 新增数据日志
     * @param dataLogDto
     */
    void saveDataLog(DataLogDTO dataLogDto);
    /**
     * 更新头像
     * @param loginUser
     */
    void updateAvatar(LoginUser loginUser);

    /**
     * 向app端 websocket推送聊天刷新消息
     * @param userId
     */
    void sendAppChatSocket(String userId);

    /**
     * 根据角色id查询角色code
     * @param id
     * @return
     */
    String getRoleCodeById(String id);

    /**
     * 根据roleCode查询角色信息，可逗号分隔多个
     *
     * @param roleCodes
     * @return
     */
    List<DictModel> queryRoleDictByCode(String roleCodes);

    /**
     * 根据部门ID查询用户ID
     * @param deptIds
     * @return
     */
    List<String> queryUserIdsByDeptIds(List<String> deptIds);
    
    /**
     * 根据部门ID查询用户账号
     * @param deptIds
     * @return
     */
    List<String> queryUserAccountsByDeptIds(List<String> deptIds);

    /**
     * 根据角色编码 查询用户ID
     * @param roleCodes
     * @return
     */
    List<String> queryUserIdsByRoleds(List<String> roleCodes);

    /**
     * 根据职务ID查询用户ID
     * @param positionIds
     * @return
     */
    List<String> queryUserIdsByPositionIds(List<String> positionIds);

    /**
     * 根据部门和子部门下的所有用户账号
     *
     * @param orgCode 部门编码
     * @return
     */
    public List<String> getUserAccountsByDepCode(String orgCode);

    /**
     * 检查查询sql的表和字段是否在白名单中
     *
     * @param selectSql
     * @return
     */
    boolean dictTableWhiteListCheckBySql(String selectSql);

    /**
     * 根据字典表或者字典编码，校验是否在白名单中
     *
     * @param tableOrDictCode 表名或dictCode
     * @param fields          如果传的是dictCode，则该参数必须传null
     * @return
     */
    boolean dictTableWhiteListCheckByDict(String tableOrDictCode, String... fields);

}
