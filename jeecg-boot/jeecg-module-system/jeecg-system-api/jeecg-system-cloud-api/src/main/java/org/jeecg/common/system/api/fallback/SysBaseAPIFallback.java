package org.jeecg.common.system.api.fallback;

import com.alibaba.fastjson.JSONObject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.dto.DataLogDTO;
import org.jeecg.common.api.dto.OnlineAuthDTO;
import org.jeecg.common.api.dto.message.*;
import org.jeecg.common.constant.enums.EmailTemplateEnum;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 进入fallback的方法 检查是否token未设置
 * @author: jeecg-boot
 */
@Slf4j
public class SysBaseAPIFallback implements ISysBaseAPI {

    @Setter
    private Throwable cause;

    @Override
    public void sendSysAnnouncement(MessageDTO message) {
        log.error("发送消息失败 {}", cause);
    }

    @Override
    public void sendBusAnnouncement(BusMessageDTO message) {
        log.error("发送消息失败 {}", cause);
    }

    @Override
    public void sendTemplateAnnouncement(TemplateMessageDTO message) {
        log.error("发送消息失败 {}", cause);
    }

    @Override
    public void sendBusTemplateAnnouncement(BusTemplateMessageDTO message) {
        log.error("发送消息失败 {}", cause);
    }

    @Override
    public String parseTemplateByCode(TemplateDTO templateDTO) {
        log.error("通过模板获取消息内容失败 {}", cause);
        return null;
    }

    @Override
    public LoginUser getUserById(String id) {
        return null;
    }

    @Override
    public List<String> getRolesByUsername(String username) {
        return null;
    }

    @Override
    public List<String> getRolesByUserId(String userId) {
        return null;
    }

    @Override
    public List<String> getDepartIdsByUsername(String username) {
        return null;
    }

    @Override
    public List<String> getDepartIdsByUserId(String userId) {
        return null;
    }

    @Override
    public Set<String> getDepartParentIdsByUsername(String username) {
        return null;
    }

    @Override
    public Set<String> getDepartParentIdsByDepIds(Set<String> depIds) {
        return null;
    }

    @Override
    public List<String> getDepartNamesByUsername(String username) {
        return null;
    }

    @Override
    public List<DictModel> queryDictItemsByCode(String code) {
        return null;
    }

    @Override
    public List<DictModel> queryEnableDictItemsByCode(String code) {
        return null;
    }

    @Override
    public List<DictModel> queryAllDict() {
        log.error("fegin接口queryAllDict失败："+cause.getMessage(), cause);
        return null;
    }

    @Override
    public List<SysCategoryModel> queryAllSysCategory() {
        return null;
    }

    @Override
    public List<DictModel> queryTableDictItemsByCode(String tableFilterSql, String text, String code) {
        return null;
    }

    @Override
    public List<DictModel> queryAllDepartBackDictModel() {
        return null;
    }

    @Override
    public void updateSysAnnounReadFlag(String busType, String busId) {

    }

    @Override
    public List<DictModel> queryFilterTableDictInfo(String table, String text, String code, String filterSql) {
        return null;
    }

    @Override
    public List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray) {
        log.error("queryTableDictByKeys查询失败 {}", cause);
        return null;
    }

    @Override
    public List<ComboModel> queryAllUserBackCombo() {
        return null;
    }

    @Override
    public JSONObject queryAllUser(String userIds, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public List<ComboModel> queryAllRole(String[] roleIds) {
        log.error("获取角色信息失败 {}", cause);
        return null;
    }

    @Override
    public List<String> getRoleIdsByUsername(String username) {
        return null;
    }

    @Override
    public String getDepartIdsByOrgCode(String orgCode) {
        return null;
    }

    @Override
    public List<SysDepartModel> getAllSysDepart() {
        return null;
    }

    @Override
    public DictModel getParentDepartId(String departId) {
        return null;
    }

    @Override
    public List<String> getDeptHeadByDepId(String deptId) {
        return null;
    }

    @Override
    public void sendWebSocketMsg(String[] userIds, String cmd) {

    }

    @Override
    public List<UserAccountInfo> queryAllUserByIds(String[] userIds) {
        return null;
    }

    @Override
    public void meetingSignWebsocket(String userId) {

    }

    @Override
    public List<UserAccountInfo> queryUserByNames(String[] userNames) {
        return null;
    }

    @Override
    public Set<String> getUserRoleSet(String username) {
        return null;
    }

    @Override
    public Set<String> getUserRoleSetById(String userId) {
        return null;
    }

    @Override
    public Set<String> getUserPermissionSet(String userId) {
        return null;
    }

    @Override
    public boolean hasOnlineAuth(OnlineAuthDTO onlineAuthDTO) {
        return false;
    }

    @Override
    public SysDepartModel selectAllById(String id) {
        return null;
    }

    @Override
    public List<String> queryDeptUsersByUserId(String userId) {
        return null;
    }

    @Override
    public Set<String> queryUserRoles(String username) {
        return null;
    }

    @Override
    public Set<String> queryUserRolesById(String userId) {
        return null;
    }

    @Override
    public Set<String> queryUserAuths(String userId) {
        return null;
    }

    @Override
    public DynamicDataSourceModel getDynamicDbSourceById(String dbSourceId) {
        return null;
    }

    @Override
    public DynamicDataSourceModel getDynamicDbSourceByCode(String dbSourceCode) {
        return null;
    }

    @Override
    public LoginUser getUserByName(String username) {
        log.error("jeecg-system服务节点不通，导致获取登录用户信息失败： " + cause.getMessage(), cause);
        return null;
    }

    @Override
    public String getUserIdByName(String username) {
        return null;
    }

    @Override
    public String translateDictFromTable(String table, String text, String code, String key) {
        return null;
    }

    @Override
    public String translateDict(String code, String key) {
        return null;
    }

    @Override
    public List<SysPermissionDataRuleModel> queryPermissionDataRule(String component, String requestPath, String username) {
        return null;
    }

    @Override
    public SysUserCacheInfo getCacheUser(String username) {
        log.error("获取用户信息失败 {}", cause);
        return null;
    }

    @Override
    public List<JSONObject> queryUsersByUsernames(String usernames) {
        return null;
    }

    @Override
    public List<JSONObject> queryUsersByIds(String ids) {
        return null;
    }

    @Override
    public List<JSONObject> queryDepartsByOrgcodes(String orgCodes) {
        return null;
    }

    @Override
    public List<JSONObject> queryDepartsByIds(String ids) {
        return null;
    }

    @Override
    public Map<String, List<DictModel>> translateManyDict(String dictCodes, String keys) {
        return null;
    }

    //update-begin---author:chenrui ---date:20231221  for：[issues/#5643]解决分布式下表字典跨库无法查询问题------------
    @Override
    public List<DictModel> translateDictFromTableByKeys(String table, String text, String code, String keys, String dataSource) {
        return null;
    }
    //update-end---author:chenrui ---date:20231221  for：[issues/#5643]解决分布式下表字典跨库无法查询问题------------

    @Override
    public void sendTemplateMessage(MessageDTO message) {
    }

    @Override
    public String getTemplateContent(String code) {
        return null;
    }

    @Override
    public void saveDataLog(DataLogDTO dataLogDto) {

    }

    @Override
    public void sendEmailMsg(String email,String title,String content) {

    }

    @Override
    public void sendHtmlTemplateEmail(String email, String title, EmailTemplateEnum emailTemplateEnum, JSONObject params) {

    }

    @Override
    public List<Map> getDeptUserByOrgCode(String orgCode) {
        return null;
    }

//    @Override
//    public List<JSONObject> queryDepartsByOrgIds(String ids) {
//        return null;
//    }

    @Override
    public List<String> loadCategoryDictItem(String ids) {
        return null;
    }

    @Override
    public List<String> loadCategoryDictItemByNames(String names, boolean delNotExist) {
        return null;
    }

    @Override
    public List<String> loadDictItem(String dictCode, String keys) {
        return null;
    }

    @Override
    public Map<String, String> copyLowAppDict(String originalAppId, String appId, String tenantId) {
        return null;
    }

    @Override
    public List<DictModel> getDictItems(String dictCode) {
        return null;
    }

    @Override
    public Map<String, List<DictModel>> getManyDictItems(List<String> dictCodeList) {
        return null;
    }

    @Override
    public List<DictModel> loadDictItemByKeyword(String dictCode, String keyword, Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public void updateAvatar(LoginUser loginUser) { }

    @Override
    public void sendAppChatSocket(String userId) {
        
    }

    @Override
    public String getRoleCodeById(String id) {
        return null;
    }

    @Override
    public List<DictModel> queryRoleDictByCode(String roleCodes) {
        return null;
    }

    @Override
    public List<JSONObject> queryUserBySuperQuery(String superQuery, String matchType) {
        return null;
    }

    @Override
    public JSONObject queryUserById(String id) {
        return null;
    }

    @Override
    public List<JSONObject> queryDeptBySuperQuery(String superQuery, String matchType) {
        return null;
    }

    @Override
    public List<JSONObject> queryRoleBySuperQuery(String superQuery, String matchType) {
        return null;
    }

    @Override
    public List<String> selectUserIdByTenantId(String tenantId) {
        return null;
    }

    @Override
    public List<String> queryUserIdsByDeptIds(List<String> deptIds) {
        return null;
    }

    @Override
    public List<String> queryUserAccountsByDeptIds(List<String> deptIds) {
        return null;
    }

    @Override
    public List<String> queryUserIdsByRoleds(List<String> roleCodes) {
        return null;
    }

    @Override
    public List<String> queryUserIdsByPositionIds(List<String> positionIds) {
        return null;
    }

    @Override
    public List<String> getUserAccountsByDepCode(String orgCode) {
        return null;
    }

    @Override
    public boolean dictTableWhiteListCheckBySql(String selectSql) {
        return false;
    }

    @Override
    public boolean dictTableWhiteListCheckByDict(String tableOrDictCode, String... fields) {
        return false;
    }

}
