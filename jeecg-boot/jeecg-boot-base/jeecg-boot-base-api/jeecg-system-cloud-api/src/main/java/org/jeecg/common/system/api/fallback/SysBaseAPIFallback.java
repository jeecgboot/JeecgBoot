package org.jeecg.common.system.api.fallback;

import com.alibaba.fastjson.JSONObject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.dto.OnlineAuthDTO;
import org.jeecg.common.api.dto.message.*;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 进入fallback的方法 检查是否token未设置
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
    public List<String> getDepartIdsByUsername(String username) {
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
        return null;
    }

    @Override
    public List<SysCategoryModel> queryAllDSysCategory() {
        return null;
    }

    @Override
    public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
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
    public JSONObject queryAllUser(String userIds, Integer pageNo, int pageSize) {
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
    public List<LoginUser> queryAllUserByIds(String[] userIds) {
        return null;
    }

    @Override
    public void meetingSignWebsocket(String userId) {

    }

    @Override
    public List<LoginUser> queryUserByNames(String[] userNames) {
        return null;
    }

    @Override
    public Set<String> getUserRoleSet(String username) {
        return null;
    }

    @Override
    public Set<String> getUserPermissionSet(String username) {
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
    public Set<String> queryUserAuths(String username) {
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
        log.error("通过用户名获取当前登录用户信息 {}", cause);
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

    @Override
    public List<DictModel> translateDictFromTableByKeys(String table, String text, String code, String keys) {
        return null;
    }

    @Override
    public void sendEmailMsg(String email,String title,String content) {

    }

    @Override
    public List<Map> getDeptUserByOrgCode(String orgCode) {
        return null;
    }

    @Override
    public List<JSONObject> queryDepartsByOrgIds(String ids) {
        return null;
    }

    @Override
    public List<String> loadCategoryDictItem(String ids) {
        return null;
    }

    @Override
    public List<String> loadDictItem(String dictCode, String keys) {
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
    public List<DictModel> loadDictItemByKeyword(String dictCode, String keyword, Integer pageSize) {
        return null;
    }
}
