package org.jeecg.modules.api.controller;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.dto.message.*;
import org.jeecg.common.api.dto.OnlineAuthDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.*;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 服务化 system模块 对外接口请求类
 */
@RestController
@RequestMapping("/sys/api")
public class SystemAPIController {

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private ISysUserService sysUserService;


    /**
     * 发送系统消息
     * @param message 使用构造器赋值参数 如果不设置category(消息类型)则默认为2 发送系统消息
     */
    @PostMapping("/sendSysAnnouncement")
    public void sendSysAnnouncement(@RequestBody MessageDTO message){
        sysBaseAPI.sendSysAnnouncement(message);
    }

    /**
     * 发送消息 附带业务参数
     * @param message 使用构造器赋值参数
     */
    @PostMapping("/sendBusAnnouncement")
    public void sendBusAnnouncement(@RequestBody BusMessageDTO message){
        sysBaseAPI.sendBusAnnouncement(message);
    }

    /**
     * 通过模板发送消息
     * @param message 使用构造器赋值参数
     */
    @PostMapping("/sendTemplateAnnouncement")
    public void sendTemplateAnnouncement(@RequestBody TemplateMessageDTO message){
        sysBaseAPI.sendTemplateAnnouncement(message);
    }

    /**
     * 通过模板发送消息 附带业务参数
     * @param message 使用构造器赋值参数
     */
    @PostMapping("/sendBusTemplateAnnouncement")
    public void sendBusTemplateAnnouncement(@RequestBody BusTemplateMessageDTO message){
        sysBaseAPI.sendBusTemplateAnnouncement(message);
    }

    /**
     * 通过消息中心模板，生成推送内容
     * @param templateDTO 使用构造器赋值参数
     * @return
     */
    @PostMapping("/parseTemplateByCode")
    public String parseTemplateByCode(@RequestBody TemplateDTO templateDTO){
        return sysBaseAPI.parseTemplateByCode(templateDTO);
    }

    /**
     * 根据业务类型busType及业务busId修改消息已读
     */
    @GetMapping("/updateSysAnnounReadFlag")
    public void updateSysAnnounReadFlag(@RequestParam("busType") String busType, @RequestParam("busId")String busId){
        sysBaseAPI.updateSysAnnounReadFlag(busType, busId);
    }

    /**
     * 根据用户账号查询用户信息
     * @param username
     * @return
     */
    @GetMapping("/getUserByName")
    public LoginUser getUserByName(@RequestParam("username") String username){
        return sysBaseAPI.getUserByName(username);
    }

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    @GetMapping("/getUserById")
    LoginUser getUserById(@RequestParam("id") String id){
        return sysBaseAPI.getUserById(id);
    }

    /**
     * 通过用户账号查询角色集合
     * @param username
     * @return
     */
    @GetMapping("/getRolesByUsername")
    List<String> getRolesByUsername(@RequestParam("username") String username){
        return sysBaseAPI.getRolesByUsername(username);
    }

    /**
     * 通过用户账号查询部门集合
     * @param username
     * @return 部门 id
     */
    @GetMapping("/getDepartIdsByUsername")
    List<String> getDepartIdsByUsername(@RequestParam("username") String username){
        return sysBaseAPI.getDepartIdsByUsername(username);
    }

    /**
     * 通过用户账号查询部门 name
     * @param username
     * @return 部门 name
     */
    @GetMapping("/getDepartNamesByUsername")
    List<String> getDepartNamesByUsername(@RequestParam("username") String username){
        return sysBaseAPI.getDepartNamesByUsername(username);
    }


    /**
     * 获取数据字典
     * @param code
     * @return
     */
    @GetMapping("/queryDictItemsByCode")
    List<DictModel> queryDictItemsByCode(@RequestParam("code") String code){
        return sysBaseAPI.queryDictItemsByCode(code);
    }

    /** 查询所有的父级字典，按照create_time排序 */
    @GetMapping("/queryAllDict")
    List<DictModel> queryAllDict(){
        return sysBaseAPI.queryAllDict();
    }

    /**
     * 查询所有分类字典
     * @return
     */
    @GetMapping("/queryAllDSysCategory")
    List<SysCategoryModel> queryAllDSysCategory(){
        return sysBaseAPI.queryAllDSysCategory();
    }

    /**
     * 获取表数据字典
     * @param table
     * @param text
     * @param code
     * @return
     */
    @GetMapping("/queryTableDictItemsByCode")
    List<DictModel> queryTableDictItemsByCode(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code){
        return sysBaseAPI.queryTableDictItemsByCode(table, text, code);
    }

    /**
     * 查询所有部门 作为字典信息 id -->value,departName -->text
     * @return
     */
    @GetMapping("/queryAllDepartBackDictModel")
    List<DictModel> queryAllDepartBackDictModel(){
        return sysBaseAPI.queryAllDepartBackDictModel();
    }


    /**
     * 查询表字典 支持过滤数据
     * @param table
     * @param text
     * @param code
     * @param filterSql
     * @return
     */
    @GetMapping("/queryFilterTableDictInfo")
    List<DictModel> queryFilterTableDictInfo(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code, @RequestParam("filterSql") String filterSql){
        return sysBaseAPI.queryFilterTableDictInfo(table, text, code, filterSql);
    }

    /**
     * 查询指定table的 text code 获取字典，包含text和value
     * @param table
     * @param text
     * @param code
     * @param keyArray
     * @return
     */
    @Deprecated
    @GetMapping("/queryTableDictByKeys")
    public List<String> queryTableDictByKeys(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code, @RequestParam("keyArray") String[] keyArray){
        return sysBaseAPI.queryTableDictByKeys(table, text, code, keyArray);
    }

    /**
     * 获取所有角色 带参
     * roleIds 默认选中角色
     * @return
     */
    @GetMapping("/queryAllRole")
    public List<ComboModel> queryAllRole(@RequestParam(name = "roleIds",required = false)String[] roleIds){
        if(roleIds==null || roleIds.length==0){
            return sysBaseAPI.queryAllRole();
        }else{
            return sysBaseAPI.queryAllRole(roleIds);
        }
    }

    /**
     * 通过用户账号查询角色Id集合
     * @param username
     * @return
     */
    @GetMapping("/getRoleIdsByUsername")
    public List<String> getRoleIdsByUsername(@RequestParam("username")String username){
        return sysBaseAPI.getRoleIdsByUsername(username);
    }

    /**
     * 通过部门编号查询部门id
     * @param orgCode
     * @return
     */
    @GetMapping("/getDepartIdsByOrgCode")
    public String getDepartIdsByOrgCode(@RequestParam("orgCode")String orgCode){
        return sysBaseAPI.getDepartIdsByOrgCode(orgCode);
    }

    /**
     * 查询所有部门
     * @return
     */
    @GetMapping("/getAllSysDepart")
    public List<SysDepartModel> getAllSysDepart(){
        return sysBaseAPI.getAllSysDepart();
    }

    /**
     * 根据 id 查询数据库中存储的 DynamicDataSourceModel
     *
     * @param dbSourceId
     * @return
     */
    @GetMapping("/getDynamicDbSourceById")
    DynamicDataSourceModel getDynamicDbSourceById(@RequestParam("dbSourceId")String dbSourceId){
        return sysBaseAPI.getDynamicDbSourceById(dbSourceId);
    }



    /**
     * 根据部门Id获取部门负责人
     * @param deptId
     * @return
     */
    @GetMapping("/getDeptHeadByDepId")
    public List<String> getDeptHeadByDepId(@RequestParam("deptId") String deptId){
        return sysBaseAPI.getDeptHeadByDepId(deptId);
    }

    /**
     * 查找父级部门
     * @param departId
     * @return
     */
    @GetMapping("/getParentDepartId")
    public DictModel getParentDepartId(@RequestParam("departId")String departId){
        return sysBaseAPI.getParentDepartId(departId);
    }

    /**
     * 根据 code 查询数据库中存储的 DynamicDataSourceModel
     *
     * @param dbSourceCode
     * @return
     */
    @GetMapping("/getDynamicDbSourceByCode")
    public DynamicDataSourceModel getDynamicDbSourceByCode(@RequestParam("dbSourceCode") String dbSourceCode){
        return sysBaseAPI.getDynamicDbSourceByCode(dbSourceCode);
    }

    /**
     * 给指定用户发消息
     * @param userIds
     * @param cmd
     */
    @GetMapping("/sendWebSocketMsg")
    public void sendWebSocketMsg(String[] userIds, String cmd){
        sysBaseAPI.sendWebSocketMsg(userIds, cmd);
    }


    /**
     * 根据id获取所有参与用户
     * userIds
     * @return
     */
    @GetMapping("/queryAllUserByIds")
    public List<LoginUser> queryAllUserByIds(@RequestParam("userIds") String[] userIds){
        return sysBaseAPI.queryAllUserByIds(userIds);
    }

    /**
     * 查询所有用户 返回ComboModel
     * @return
     */
    @GetMapping("/queryAllUserBackCombo")
    public List<ComboModel> queryAllUserBackCombo(){
        return sysBaseAPI.queryAllUserBackCombo();
    }

    /**
     * 分页查询用户 返回JSONObject
     * @return
     */
    @GetMapping("/queryAllUser")
    public JSONObject queryAllUser(@RequestParam(name="userIds",required=false)String userIds, @RequestParam(name="pageNo",required=false) Integer pageNo,@RequestParam(name="pageSize",required=false) int pageSize){
        return sysBaseAPI.queryAllUser(userIds, pageNo, pageSize);
    }



    /**
     * 将会议签到信息推动到预览
     * userIds
     * @return
     * @param userId
     */
    @GetMapping("/meetingSignWebsocket")
    public void meetingSignWebsocket(@RequestParam("userId")String userId){
        sysBaseAPI.meetingSignWebsocket(userId);
    }

    /**
     * 根据name获取所有参与用户
     * userNames
     * @return
     */
    @GetMapping("/queryUserByNames")
    public List<LoginUser> queryUserByNames(@RequestParam("userNames")String[] userNames){
        return sysBaseAPI.queryUserByNames(userNames);
    }

    /**
     * 获取用户的角色集合
     * @param username
     * @return
     */
    @GetMapping("/getUserRoleSet")
    public Set<String> getUserRoleSet(@RequestParam("username")String username){
        return sysBaseAPI.getUserRoleSet(username);
    }

    /**
     * 获取用户的权限集合
     * @param username
     * @return
     */
    @GetMapping("/getUserPermissionSet")
    public Set<String> getUserPermissionSet(@RequestParam("username") String username){
        return sysBaseAPI.getUserPermissionSet(username);
    }

    //-----

    /**
     * 判断是否有online访问的权限
     * @param onlineAuthDTO
     * @return
     */
    @PostMapping("/hasOnlineAuth")
    public boolean hasOnlineAuth(@RequestBody OnlineAuthDTO onlineAuthDTO){
        return sysBaseAPI.hasOnlineAuth(onlineAuthDTO);
    }

    /**
     * 查询用户角色信息
     * @param username
     * @return
     */
    @GetMapping("/queryUserRoles")
    public Set<String> queryUserRoles(@RequestParam("username") String username){
        return sysUserService.getUserRolesSet(username);
    }


    /**
     * 查询用户权限信息
     * @param username
     * @return
     */
    @GetMapping("/queryUserAuths")
    public Set<String> queryUserAuths(@RequestParam("username") String username){
        return sysUserService.getUserPermissionsSet(username);
    }

    /**
     * 通过部门id获取部门全部信息
     */
    @GetMapping("/selectAllById")
    public SysDepartModel selectAllById(@RequestParam("id") String id){
        return sysBaseAPI.selectAllById(id);
    }

    /**
     * 根据用户id查询用户所属公司下所有用户ids
     * @param userId
     * @return
     */
    @GetMapping("/queryDeptUsersByUserId")
    public List<String> queryDeptUsersByUserId(@RequestParam("userId") String userId){
        return sysBaseAPI.queryDeptUsersByUserId(userId);
    }


    /**
     * 查询数据权限
     * @return
     */
    @GetMapping("/queryPermissionDataRule")
    public List<SysPermissionDataRuleModel> queryPermissionDataRule(@RequestParam("component") String component, @RequestParam("requestPath")String requestPath, @RequestParam("username") String username){
        return sysBaseAPI.queryPermissionDataRule(component, requestPath, username);
    }

    /**
     * 查询用户信息
     * @param username
     * @return
     */
    @GetMapping("/getCacheUser")
    public SysUserCacheInfo getCacheUser(@RequestParam("username") String username){
        return sysBaseAPI.getCacheUser(username);
    }

    /**
     * 字典表的 翻译
     * @param table
     * @param text
     * @param code
     * @param key
     * @return
     */
    @GetMapping("/translateDictFromTable")
    public String translateDictFromTable(@RequestParam("table") String table, @RequestParam("text") String text, @RequestParam("code") String code, @RequestParam("key") String key){
        return sysBaseAPI.translateDictFromTable(table, text, code, key);
    }

    /**
     * 普通字典的翻译
     * @param code
     * @param key
     * @return
     */
    @GetMapping("/translateDict")
    public String translateDict(@RequestParam("code") String code, @RequestParam("key") String key){
        return sysBaseAPI.translateDict(code, key);
    }


    /**
     * 36根据多个用户账号(逗号分隔)，查询返回多个用户信息
     * @param usernames
     * @return
     */
    @GetMapping("/queryUsersByUsernames")
    List<JSONObject> queryUsersByUsernames(String usernames){
        return this.sysBaseAPI.queryUsersByUsernames(usernames);
    }

    /**
     * 37根据多个用户id(逗号分隔)，查询返回多个用户信息
     * @param usernames
     * @return
     */
    @GetMapping("/queryUsersByIds")
    List<JSONObject> queryUsersByIds(String ids){
        return this.sysBaseAPI.queryUsersByIds(ids);
    }

    /**
     * 38根据多个部门编码(逗号分隔)，查询返回多个部门信息
     * @param orgCodes
     * @return
     */
    @GetMapping("/queryDepartsByOrgcodes")
    List<JSONObject> queryDepartsByOrgcodes(String orgCodes){
        return this.sysBaseAPI.queryDepartsByOrgcodes(orgCodes);
    }

    /**
     * 39根据多个部门ID(逗号分隔)，查询返回多个部门信息
     * @param orgCodes
     * @return
     */
    @GetMapping("/queryDepartsByIds")
    List<JSONObject> queryDepartsByIds(String orgCodes){
        return this.sysBaseAPI.queryDepartsByIds(orgCodes);
    }

    /**
     * 40发送邮件消息
     * @param email
     * @param title
     * @param content
     */
    @GetMapping("/sendEmailMsg")
    public void sendEmailMsg(@RequestParam("email")String email,@RequestParam("title")String title,@RequestParam("content")String content){
         this.sysBaseAPI.sendEmailMsg(email,title,content);
    };
    /**
     * 41 获取公司下级部门和公司下所有用户信息
     * @param orgCode
     */
    @GetMapping("/getDeptUserByOrgCode")
    List<Map> getDeptUserByOrgCode(@RequestParam("orgCode")String orgCode){
       return this.sysBaseAPI.getDeptUserByOrgCode(orgCode);
    }
}
