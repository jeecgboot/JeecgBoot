package org.jeecg.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeecg.qywx.api.core.common.AccessToken;
import com.jeecg.qywx.api.department.JwDepartmentAPI;
import com.jeecg.qywx.api.department.vo.DepartMsgResponse;
import com.jeecg.qywx.api.department.vo.Department;
import com.jeecg.qywx.api.message.JwMessageAPI;
import com.jeecg.qywx.api.message.vo.*;
import com.jeecg.qywx.api.user.JwUserAPI;
import com.jeecg.qywx.api.user.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.constant.enums.MessageTypeEnum;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RestUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.vo.thirdapp.JwDepartmentTreeVo;
import org.jeecg.modules.system.vo.thirdapp.JwSysUserDepartVo;
import org.jeecg.modules.system.vo.thirdapp.JwUserDepartVo;
import org.jeecg.modules.system.vo.thirdapp.SyncInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 第三方App对接：企业微信实现类
 * @author: jeecg-boot
 */
@Slf4j
@Service
public class ThirdAppWechatEnterpriseServiceImpl implements IThirdAppService {

    @Autowired
    JeecgBaseConfig jeecgBaseConfig;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private ISysThirdAccountService sysThirdAccountService;
    @Autowired
    private ISysUserDepartService sysUserDepartService;
    @Autowired
    private ISysPositionService sysPositionService;
    @Autowired
    private SysAnnouncementSendMapper sysAnnouncementSendMapper;
    @Autowired
    private SysThirdAppConfigMapper configMapper;
    @Autowired
    private SysTenantMapper sysTenantMapper;
    @Autowired
    private SysUserTenantMapper sysUserTenantMapper;
    @Autowired
    private SysThirdAccountMapper sysThirdAccountMapper;
    @Autowired
    private SysTenantMapper tenantMapper;
    
    
    /**
     * errcode
     */
    private static final String ERR_CODE = "errcode";

    /**
     * 第三方APP类型，当前固定为 wechat_enterprise
     */
    public final String THIRD_TYPE = "wechat_enterprise";

    @Override
    public String getAccessToken() {
        //update-begin---author:wangshuai ---date:20230224  for：[QQYUN-3440]新建企业微信和钉钉配置表，通过租户模式隔离------------
        SysThirdAppConfig config = this.getWeChatThirdAppConfig();
        String corpId = config.getClientId();
        String secret = config.getClientSecret();
        //update-end---author:wangshuai ---date:20230224  for：[QQYUN-3440]新建企业微信和钉钉配置表，通过租户模式隔离------------
        AccessToken accessToken = JwAccessTokenAPI.getAccessToken(corpId, secret);
        if (accessToken != null) {
            return accessToken.getAccesstoken();
        }
        log.warn("获取AccessToken失败");
        return null;
    }

    /** 获取APPToken，新版企业微信的秘钥是分开的 */
    public String getAppAccessToken(SysThirdAppConfig config) {
        //update-begin---author:wangshuai ---date:20230224  for：[QQYUN-3440]新建企业微信和钉钉配置表，通过租户模式隔离------------
        String corpId = config.getClientId();
        String secret = config.getAgentAppSecret();
        // 如果没有配置APP秘钥，就说明是老企业，可以通用秘钥
        if (oConvertUtils.isEmpty(secret)) {
            secret = config.getClientSecret();
        //update-end---author:wangshuai ---date:20230224  for：[QQYUN-3440]新建企业微信和钉钉配置表，通过租户模式隔离------------
        }

        AccessToken accessToken = JwAccessTokenAPI.getAccessToken(corpId, secret);
        if (accessToken != null) {
            return accessToken.getAccesstoken();
        }
        log.warn("获取AccessToken失败");
        return null;
    }

    @Override
    public SyncInfoVo syncLocalDepartmentToThirdApp(String ids) {
        SyncInfoVo syncInfo = new SyncInfoVo();
        String accessToken = this.getAccessToken();
        if (accessToken == null) {
            syncInfo.addFailInfo("accessToken获取失败！");
            return syncInfo;
        }
        // 获取企业微信所有的部门
        List<Department> departments = JwDepartmentAPI.getAllDepartment(accessToken);
        if (departments == null) {
            syncInfo.addFailInfo("获取企业微信所有部门失败！");
            return syncInfo;
        }
        // 删除企业微信有但本地没有的部门（以本地部门数据为主）(以为企业微信不能创建同名部门，所以只能先删除）
        List<JwDepartmentTreeVo> departmentTreeList = JwDepartmentTreeVo.listToTree(departments);
        this.deleteDepartRecursion(departmentTreeList, accessToken, true);
        // 获取本地所有部门树结构
        List<SysDepartTreeModel> sysDepartsTree = sysDepartService.queryTreeList();
        // -- 企业微信不能创建新的顶级部门，所以新的顶级部门的parentId就为1
        Department parent = new Department();
        parent.setId("1");
        // 递归同步部门
        departments = JwDepartmentAPI.getAllDepartment(accessToken);
        this.syncDepartmentRecursion(sysDepartsTree, departments, parent, accessToken);
        return syncInfo;
    }

    /**
     * 递归删除部门以及子部门，由于企业微信不允许删除带有成员和子部门的部门，所以需要递归删除下子部门，然后把部门成员移动端根部门下
     * @param children
     * @param accessToken
     * @param ifLocal
     */
    private void deleteDepartRecursion(List<JwDepartmentTreeVo> children, String accessToken, boolean ifLocal) {
        for (JwDepartmentTreeVo departmentTree : children) {
            String depId = departmentTree.getId();
            // 过滤根部门
            if (!"1".equals(depId)) {
                // 判断本地是否有该部门
                if (ifLocal) {
                    LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(SysDepart::getQywxIdentifier, depId);
                    SysDepart sysDepart = sysDepartService.getOne(queryWrapper);
                    // 本地有该部门，不删除
                    if (sysDepart != null) {
                        if (departmentTree.hasChildren()) {
                            this.deleteDepartRecursion(departmentTree.getChildren(), accessToken, true);
                        }
                        continue;
                    }
                }
                // 判断是否有成员，有就移动到根部门
                List<User> departUserList = JwUserAPI.getUsersByDepartid(depId, "1", null, accessToken);
                if (departUserList != null && departUserList.size() > 0) {
                    for (User user : departUserList) {
                        User updateUser = new User();
                        updateUser.setUserid(user.getUserid());
                        updateUser.setDepartment(new Integer[]{1});
                        JwUserAPI.updateUser(updateUser, accessToken);
                    }
                }
                // 有子部门优先删除子部门
                if (departmentTree.hasChildren()) {
                    this.deleteDepartRecursion(departmentTree.getChildren(), accessToken, false);
                }
                // 执行删除操作
                JwDepartmentAPI.deleteDepart(depId, accessToken);
            }
        }
    }

    /**
     * 递归同步部门到第三方APP
     * @param sysDepartsTree
     * @param departments
     * @param parent
     * @param accessToken
     */
    private void syncDepartmentRecursion(List<SysDepartTreeModel> sysDepartsTree, List<Department> departments, Department parent, String accessToken) {
        if (sysDepartsTree != null && sysDepartsTree.size() != 0) {
            for1:
            for (SysDepartTreeModel depart : sysDepartsTree) {
                for (Department department : departments) {
                    // id相同，代表已存在，执行修改操作
                    if (department.getId().equals(depart.getQywxIdentifier())) {
                        this.sysDepartToQwDepartment(depart, department, parent.getId());
                        JwDepartmentAPI.updateDepart(department, accessToken);
                        // 紧接着同步子级
                        this.syncDepartmentRecursion(depart.getChildren(), departments, department, accessToken);
                        // 跳出外部循环
                        continue for1;
                    }
                }
                // 循环到此说明是新部门，直接调接口创建
                Department newDepartment = this.sysDepartToQwDepartment(depart, parent.getId());
                DepartMsgResponse response = JwDepartmentAPI.createDepartment(newDepartment, accessToken);
                // 创建成功，将返回的id绑定到本地
                if (response != null && response.getId() != null) {
                    SysDepart sysDepart = new SysDepart();
                    sysDepart.setId(depart.getId());
                    sysDepart.setQywxIdentifier(response.getId().toString());
                    sysDepartService.updateById(sysDepart);
                    Department newParent = new Department();
                    newParent.setId(response.getId().toString());
                    // 紧接着同步子级
                    this.syncDepartmentRecursion(depart.getChildren(), departments, newParent, accessToken);
                }
                // 收集错误信息
//                this.syncUserCollectErrInfo(errCode, sysUser, errInfo);
            }
        }
    }

    public SyncInfoVo syncThirdAppDepartmentToLocal(Integer tenantId, Map<String,String> map) {
        SyncInfoVo syncInfo = new SyncInfoVo();
        String accessToken = this.getAccessToken();
        if (accessToken == null) {
            syncInfo.addFailInfo("accessToken获取失败！");
            return syncInfo;
        }
        // 获取企业微信所有的部门
        List<Department> departments = JwDepartmentAPI.getAllDepartment(accessToken);
        if (departments == null) {
            syncInfo.addFailInfo("企业微信部门信息获取失败！");
            return syncInfo;
        }
        String username = JwtUtil.getUserNameByToken(SpringContextUtils.getHttpServletRequest());
        // 将list转为tree
        List<JwDepartmentTreeVo> departmentTreeList = JwDepartmentTreeVo.listToTree(departments);
        // 递归同步部门
        this.syncDepartmentToLocalRecursion(departmentTreeList, null, username, syncInfo, tenantId, map);
        return syncInfo;
    }

    /**
     * 递归同步部门到本地
     */
    private void syncDepartmentToLocalRecursion(List<JwDepartmentTreeVo> departmentTreeList, String sysParentId, String username, SyncInfoVo syncInfo,Integer tenantId, Map<String,String> map) {
        if (departmentTreeList != null && departmentTreeList.size() != 0) {
            for (JwDepartmentTreeVo departmentTree : departmentTreeList) {
                String depId = departmentTree.getId();
                LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper<>();
                // 根据 qywxIdentifier 字段和租户id查询，租户id默认为0
                queryWrapper.eq(SysDepart::getQywxIdentifier, depId);
                queryWrapper.eq(SysDepart::getTenantId, tenantId);
                SysDepart sysDepart = sysDepartService.getOne(queryWrapper);
                if (sysDepart != null) {
                    //  执行更新操作
                    SysDepart updateSysDepart = this.qwDepartmentToSysDepart(departmentTree, sysDepart);
                    //update-begin---author:wangshuai---date:2024-04-10---for:【issues/6017】企业微信同步部门时没有最顶层的部门名，同步用户时，用户没有部门信息---
                    if (sysParentId != null && !"0".equals(sysParentId)) {
                    //update-end---author:wangshuai---date:2024-04-10---for:【issues/6017】企业微信同步部门时没有最顶层的部门名，同步用户时，用户没有部门信息---
                        updateSysDepart.setParentId(sysParentId);
                    }
                    try {
                        sysDepartService.updateDepartDataById(updateSysDepart, username);
                        String str = String.format("部门 %s 更新成功！", updateSysDepart.getDepartName());
                        syncInfo.addSuccessInfo(str);
                        map.put(depId,updateSysDepart.getId());
                    } catch (Exception e) {
                        this.syncDepartCollectErrInfo(e, departmentTree, syncInfo);
                    }
                    if (departmentTree.hasChildren()) {
                        // 紧接着同步子级
                        this.syncDepartmentToLocalRecursion(departmentTree.getChildren(), updateSysDepart.getId(), username, syncInfo, tenantId, map);
                    }
                } else {
                    // 执行新增操作
                    SysDepart newSysDepart = this.qwDepartmentToSysDepart(departmentTree, null);
                    if (sysParentId != null && !"0".equals(sysParentId)) {
                        newSysDepart.setParentId(sysParentId);
                        // 2 = 组织机构
                        newSysDepart.setOrgCategory("2");
                    } else {
                        // 1 = 公司
                        newSysDepart.setOrgCategory("1");
                    }
                    newSysDepart.setTenantId(tenantId);
                    try {
                        sysDepartService.saveDepartData(newSysDepart, username);
                        String str = String.format("部门 %s 创建成功！", newSysDepart.getDepartName());
                        syncInfo.addSuccessInfo(str);
                        map.put(depId,newSysDepart.getId());
                    } catch (Exception e) {
                        this.syncDepartCollectErrInfo(e, departmentTree, syncInfo);
                    }
                    // 紧接着同步子级
                    if (departmentTree.hasChildren()) {
                        this.syncDepartmentToLocalRecursion(departmentTree.getChildren(), newSysDepart.getId(), username, syncInfo, tenantId, map);
                    }
                }
            }
        }
    }

    @Override
    public SyncInfoVo syncLocalUserToThirdApp(String ids) {
        SyncInfoVo syncInfo = new SyncInfoVo();
        String accessToken = this.getAccessToken();
        if (accessToken == null) {
            syncInfo.addFailInfo("accessToken获取失败！");
            return syncInfo;
        }
        // 获取企业微信所有的用户
//        List<User> qwUsers = JwUserAPI.getDetailUsersByDepartid("1", null, null, accessToken);
        // 获取企业微信所有的用户（只能获取userid）
        List<User> qwUsers = JwUserAPI.getUserIdList(accessToken);

        if (qwUsers == null) {
            syncInfo.addFailInfo("企业微信用户列表查询失败！");
            return syncInfo;
        }
        List<SysUser> sysUsers;
        if (StringUtils.isNotBlank(ids)) {
            String[] idList = ids.split(",");
            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(SysUser::getId, (Object[]) idList);
            // 获取本地指定用户
            sysUsers = userMapper.selectList(queryWrapper);
        } else {
            // 获取本地所有用户
            sysUsers = userMapper.selectList(Wrappers.emptyWrapper());
        }

        // 循环判断新用户和需要更新的用户
        for1:
        for (SysUser sysUser : sysUsers) {
            // 外部模拟登陆临时账号，不同步
            if ("_reserve_user_external".equals(sysUser.getUsername())) {
                continue;
            }
            /*
             * 判断是否同步过的逻辑：
             * 1. 查询 sys_third_account（第三方账号表）是否有数据，如果有代表已同步
             * 2. 本地表里没有，就先用手机号判断，不通过再用username判断。
             */
            User qwUser;
            SysThirdAccount sysThirdAccount = sysThirdAccountService.getOneBySysUserId(sysUser.getId(), THIRD_TYPE);
            for (User qwUserTemp : qwUsers) {
                if (sysThirdAccount == null || oConvertUtils.isEmpty(sysThirdAccount.getThirdUserId()) || !sysThirdAccount.getThirdUserId().equals(qwUserTemp.getUserid())) {
                    // sys_third_account 表匹配失败，尝试用手机号匹配
                    // 新版企业微信调整了API，现在只能通过userid来判断是否同步过了
//                    String phone = sysUser.getPhone();
//                    if (!(oConvertUtils.isEmpty(phone) || phone.equals(qwUserTemp.getMobile()))) {
                        // 手机号匹配失败，再尝试用username匹配
                        String username = sysUser.getUsername();
                        if (!(oConvertUtils.isEmpty(username) || username.equals(qwUserTemp.getUserid()))) {
                            // username 匹配失败，直接跳到下一次循环继续
                            continue;
                        }
//                    }
                }
                // 循环到此说明用户匹配成功，进行更新操作
                qwUser = this.sysUserToQwUser(sysUser, qwUserTemp);
                int errCode = JwUserAPI.updateUser(qwUser, accessToken);
                // 收集错误信息
                this.syncUserCollectErrInfo(errCode, sysUser, syncInfo);
                this.thirdAccountSaveOrUpdate(sysThirdAccount, sysUser.getId(), qwUser.getUserid(),qwUser.getName(), null);
                // 更新完成，直接跳到下一次外部循环继续
                continue for1;
            }
            // 循环到此说明是新用户，直接调接口创建
            qwUser = this.sysUserToQwUser(sysUser);
            int errCode = JwUserAPI.createUser(qwUser, accessToken);
            // 收集错误信息
            boolean apiSuccess = this.syncUserCollectErrInfo(errCode, sysUser, syncInfo);
            if (apiSuccess) {
                this.thirdAccountSaveOrUpdate(sysThirdAccount, sysUser.getId(), qwUser.getUserid(),qwUser.getName(), null);
            }
        }
        return syncInfo;
    }

//    @Override
//    public SyncInfoVo syncThirdAppUserToLocal() {
//        SyncInfoVo syncInfo = new SyncInfoVo();
//        String accessToken = this.getAccessToken();
//        if (accessToken == null) {
//            syncInfo.addFailInfo("accessToken获取失败！");
//            return syncInfo;
//        }
//        // 获取企业微信所有的用户
//        List<User> qwUsersList = JwUserAPI.getDetailUsersByDepartid("1", null, null, accessToken);
//        if (qwUsersList == null) {
//            syncInfo.addFailInfo("企业微信用户列表查询失败！");
//            return syncInfo;
//        }
//        //查询本地用户
//        List<SysUser> sysUsersList = userMapper.selectList(Wrappers.emptyWrapper());
//        // 循环判断新用户和需要更新的用户
//        for (User qwUser : qwUsersList) {
//            /*
//             * 判断是否同步过的逻辑：
//             * 1. 查询 sys_third_account（第三方账号表）是否有数据，如果有代表已同步
//             * 2. 本地表里没有，就先用手机号判断，不通过再用username判断。
//             */
//            SysThirdAccount sysThirdAccount = sysThirdAccountService.getOneByThirdUserId(qwUser.getUserid(), THIRD_TYPE);
//            List<SysUser> collect = sysUsersList.stream().filter(user -> (qwUser.getMobile().equals(user.getPhone()) || qwUser.getUserid().equals(user.getUsername()))
//                                                                ).collect(Collectors.toList());
//
//            if (collect != null && collect.size() > 0) {
//                SysUser sysUserTemp = collect.get(0);
//                // 循环到此说明用户匹配成功，进行更新操作
//                SysUser updateSysUser = this.qwUserToSysUser(qwUser, sysUserTemp);
//                try {
//                    userMapper.updateById(updateSysUser);
//                    String str = String.format("用户 %s(%s) 更新成功！", updateSysUser.getRealname(), updateSysUser.getUsername());
//                    syncInfo.addSuccessInfo(str);
//                } catch (Exception e) {
//                    this.syncUserCollectErrInfo(e, qwUser, syncInfo);
//                }
//
//                this.thirdAccountSaveOrUpdate(sysThirdAccount, updateSysUser.getId(), qwUser.getUserid());
//                // 更新完成，直接跳到下一次外部循环继续
//            }else{
//                // 没匹配到用户则走新增逻辑
//                SysUser newSysUser = this.qwUserToSysUser(qwUser);
//                try {
//                    userMapper.insert(newSysUser);
//                    String str = String.format("用户 %s(%s) 创建成功！", newSysUser.getRealname(), newSysUser.getUsername());
//                    syncInfo.addSuccessInfo(str);
//                } catch (Exception e) {
//                    this.syncUserCollectErrInfo(e, qwUser, syncInfo);
//                }
//                this.thirdAccountSaveOrUpdate(sysThirdAccount, newSysUser.getId(), qwUser.getUserid());
//            }
//        }
//        return syncInfo;
//    }

    /**
     * 保存或修改第三方登录表
     *
     * @param sysThirdAccount 第三方账户表对象，为null就新增数据，否则就修改
     * @param sysUserId       本地系统用户ID
     * @param qwUserId        企业微信用户ID
     * @param wechatRealName  企业微信用户真实姓名
     */
    private void thirdAccountSaveOrUpdate(SysThirdAccount sysThirdAccount, String sysUserId, String qwUserId, String wechatRealName, Integer tenantId) {
        if (sysThirdAccount == null) {
            sysThirdAccount = new SysThirdAccount();
            sysThirdAccount.setSysUserId(sysUserId);
            sysThirdAccount.setStatus(1);
            sysThirdAccount.setDelFlag(0);
            sysThirdAccount.setThirdType(THIRD_TYPE);
            if(oConvertUtils.isNotEmpty(tenantId)){
                sysThirdAccount.setTenantId(tenantId);
            }
        }
        sysThirdAccount.setThirdUserId(qwUserId);
        sysThirdAccount.setThirdUserUuid(qwUserId);
        sysThirdAccount.setRealname(wechatRealName);
        sysThirdAccountService.saveOrUpdate(sysThirdAccount);
    }

    /**
     * 【同步用户】收集同步过程中的错误信息
     */
    private boolean syncUserCollectErrInfo(int errCode, SysUser sysUser, SyncInfoVo syncInfo) {
        if (errCode != 0) {
            String msg = "";
            // https://open.work.weixin.qq.com/api/doc/90000/90139/90313
            switch (errCode) {
                case 40003:
                    msg = "无效的UserID";
                    break;
                case 60129:
                    msg = "手机和邮箱不能都为空";
                    break;
                case 60102:
                    msg = "UserID已存在";
                    break;
                case 60103:
                    msg = "手机号码不合法";
                    break;
                case 60104:
                    msg = "手机号码已存在";
                    break;
                default:
            }
            String str = String.format("用户 %s(%s) 同步失败！错误码：%s——%s", sysUser.getUsername(), sysUser.getRealname(), errCode, msg);
            syncInfo.addFailInfo(str);
            return false;
        } else {
            String str = String.format("用户 %s(%s) 同步成功！", sysUser.getUsername(), sysUser.getRealname());
            syncInfo.addSuccessInfo(str);
            return true;
        }
    }

    private boolean syncUserCollectErrInfo(Exception e, User qwUser, SyncInfoVo syncInfo) {
        String msg;
        if (e instanceof DuplicateKeyException) {
            msg = e.getCause().getMessage();
        } else {
            msg = e.getMessage();
        }
        String str = String.format("用户 %s(%s) 同步失败！错误信息：%s", qwUser.getUserid(), qwUser.getName(), msg);
        syncInfo.addFailInfo(str);
        return false;
    }

    private boolean syncDepartCollectErrInfo(Exception e, Department department, SyncInfoVo syncInfo) {
        String msg;
        if (e instanceof DuplicateKeyException) {
            msg = e.getCause().getMessage();
        } else {
            msg = e.getMessage();
        }
        String str = String.format("部门 %s(%s) 同步失败！错误信息：%s", department.getName(), department.getId(), msg);
        syncInfo.addFailInfo(str);
        return false;
    }

    /**
     * 【同步用户】将SysUser转为企业微信的User对象（创建新用户）
     */
    private User sysUserToQwUser(SysUser sysUser) {
        User user = new User();
        // 通过 username 来关联
        user.setUserid(sysUser.getUsername());
        return this.sysUserToQwUser(sysUser, user);
    }

    /**
     * 【同步用户】将SysUser转为企业微信的User对象（更新旧用户）
     */
    private User sysUserToQwUser(SysUser sysUser, User user) {
        user.setName(sysUser.getRealname());
        user.setMobile(sysUser.getPhone());
        // 查询并同步用户部门关系
        List<SysDepart> departList = this.getUserDepart(sysUser);
        if (departList != null) {
            List<Integer> departmentIdList = new ArrayList<>();
            // 企业微信 1表示为上级，0表示非上级
            List<Integer> isLeaderInDept = new ArrayList<>();
            // 当前用户管理的部门
            List<String> manageDepartIdList = new ArrayList<>();
            if (oConvertUtils.isNotEmpty(sysUser.getDepartIds())) {
                manageDepartIdList = Arrays.asList(sysUser.getDepartIds().split(","));
            }
            for (SysDepart sysDepart : departList) {
                // 企业微信的部门id
                if (oConvertUtils.isNotEmpty(sysDepart.getQywxIdentifier())) {
                    try {
                        departmentIdList.add(Integer.parseInt(sysDepart.getQywxIdentifier()));
                    } catch (NumberFormatException ignored) {
                        continue;
                    }
                    // 判断用户身份，是否为上级
                    if (CommonConstant.USER_IDENTITY_2.equals(sysUser.getUserIdentity())) {
                        // 判断当前部门是否为该用户管理的部门
                        isLeaderInDept.add(manageDepartIdList.contains(sysDepart.getId()) ? 1 : 0);
                    } else {
                        isLeaderInDept.add(0);
                    }
                }
            }
            user.setDepartment(departmentIdList.toArray(new Integer[]{}));
            // 个数必须和参数department的个数一致，表示在所在的部门内是否为上级。1表示为上级，0表示非上级。在审批等应用里可以用来标识上级审批人
            user.setIs_leader_in_dept(isLeaderInDept.toArray(new Integer[]{}));
        }
        if (user.getDepartment() == null || user.getDepartment().length == 0) {
            // 没有找到匹配部门，同步到根部门下
            user.setDepartment(new Integer[]{1});
            user.setIs_leader_in_dept(new Integer[]{0});
        }
        // 职务翻译
        //update-begin---author:wangshuai ---date:20230220  for：[QQYUN-3980]组织管理中 职位功能 职位表加租户id 加职位-用户关联表------------
        List<SysPosition> positionList = sysPositionService.getPositionList(sysUser.getId());
        if(null != positionList && positionList.size()>0){
            String positionName = positionList.stream().map(SysPosition::getName).collect(Collectors.joining(SymbolConstant.COMMA));
            user.setPosition(positionName);
        }
        //update-end---author:wangshuai ---date:20230220  for：[QQYUN-3980]组织管理中 职位功能 职位表加租户id 加职位-用户关联表------------
        if (sysUser.getSex() != null) {
            user.setGender(sysUser.getSex().toString());
        }
        user.setEmail(sysUser.getEmail());
        // 启用/禁用成员（状态），规则不同，需要转换
        // 企业微信规则：1表示启用成员，0表示禁用成员
        // JEECG规则：1正常，2冻结
        if (sysUser.getStatus() != null) {
            if (CommonConstant.USER_UNFREEZE.equals(sysUser.getStatus()) || CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
                user.setEnable(sysUser.getStatus() == 1 ? 1 : 0);
            } else {
                user.setEnable(1);
            }
        }
        // 座机号
        user.setTelephone(sysUser.getTelephone());
        // --- 企业微信没有逻辑删除的功能
        // update-begin--Author:sunjianlei Date:20210520 for：本地逻辑删除的用户，在企业微信里禁用 -----
        if (CommonConstant.DEL_FLAG_1.equals(sysUser.getDelFlag())) {
            user.setEnable(0);
        }
        // update-end--Author:sunjianlei Date:20210520 for：本地逻辑删除的用户，在企业微信里冻结 -----

        return user;
    }

    /**
     * 查询用户和部门的关系
     */
    private List<SysDepart> getUserDepart(SysUser sysUser) {
        // 根据用户部门关系表查询出用户的部门
        LambdaQueryWrapper<SysUserDepart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserDepart::getUserId, sysUser.getId());
        List<SysUserDepart> sysUserDepartList = sysUserDepartService.list(queryWrapper);
        if (sysUserDepartList.size() == 0) {
            return null;
        }
        // 根据用户部门
        LambdaQueryWrapper<SysDepart> departQueryWrapper = new LambdaQueryWrapper<>();
        List<String> departIdList = sysUserDepartList.stream().map(SysUserDepart::getDepId).collect(Collectors.toList());
        departQueryWrapper.in(SysDepart::getId, departIdList);
        List<SysDepart> departList = sysDepartService.list(departQueryWrapper);
        return departList.size() == 0 ? null : departList;
    }

    /**
     * 【同步用户】将企业微信的User对象转为SysUser（创建新用户）
     */
    private SysUser qwUserToSysUser(User user) {
        SysUser sysUser = new SysUser();
        sysUser.setDelFlag(0);
        sysUser.setStatus(1);
        // 通过 username 来关联
        sysUser.setUsername(user.getUserid());
        // 密码默认为 “123456”，随机加盐
        String password = "123456", salt = oConvertUtils.randomGen(8);
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
        sysUser.setSalt(salt);
        sysUser.setPassword(passwordEncode);
        return this.qwUserToSysUser(user, sysUser);
    }

    /**
     * 【同步用户】将企业微信的User对象转为SysUser（更新旧用户）
     */
    private SysUser qwUserToSysUser(User qwUser, SysUser oldSysUser) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(oldSysUser, sysUser);
        sysUser.setRealname(qwUser.getName());
        sysUser.setPost(qwUser.getPosition());
        // 设置工号，由于企业微信没有工号的概念，所以只能用 userId 代替
        if (oConvertUtils.isEmpty(sysUser.getWorkNo())) {
            sysUser.setWorkNo(qwUser.getUserid());
        }
        try {
            sysUser.setSex(Integer.parseInt(qwUser.getGender()));
        } catch (NumberFormatException ignored) {
        }
        // 因为唯一键约束的原因，如果原数据和旧数据相同，就不更新
        if (oConvertUtils.isNotEmpty(qwUser.getEmail()) && !qwUser.getEmail().equals(sysUser.getEmail())) {
            sysUser.setEmail(qwUser.getEmail());
        } else {
            sysUser.setEmail(null);
        }
        // 因为唯一键约束的原因，如果原数据和旧数据相同，就不更新
        if (oConvertUtils.isNotEmpty(qwUser.getMobile()) && !qwUser.getMobile().equals(sysUser.getPhone())) {
            sysUser.setPhone(qwUser.getMobile());
        } else {
            sysUser.setPhone(null);
        }

        // 启用/禁用成员（状态），规则不同，需要转换
        // 企业微信规则：1表示启用成员，0表示禁用成员
        // JEECG规则：1正常，2冻结
        if (qwUser.getEnable() != null) {
            sysUser.setStatus(qwUser.getEnable() == 1 ? 1 : 2);
        }
        // 座机号
        sysUser.setTelephone(qwUser.getTelephone());

        // --- 企业微信没有逻辑删除的功能
        // sysUser.setDelFlag()
        return sysUser;
    }

    /**
     * 【同步部门】将SysDepartTreeModel转为企业微信的Department对象（创建新部门）
     */
    private Department sysDepartToQwDepartment(SysDepartTreeModel departTree, String parentId) {
        Department department = new Department();
        return this.sysDepartToQwDepartment(departTree, department, parentId);
    }

    /**
     * 【同步部门】将SysDepartTreeModel转为企业微信的Department对象
     */
    private Department sysDepartToQwDepartment(SysDepartTreeModel departTree, Department department, String parentId) {
        department.setName(departTree.getDepartName());
        department.setParentid(parentId);
        if (departTree.getDepartOrder() != null) {
            department.setOrder(departTree.getDepartOrder().toString());
        }
        return department;
    }


    /**
     * 【同步部门】将企业微信的Department对象转为SysDepart
     */
    private SysDepart qwDepartmentToSysDepart(Department department, SysDepart oldSysDepart) {
        SysDepart sysDepart = new SysDepart();
        if (oldSysDepart != null) {
            BeanUtils.copyProperties(oldSysDepart, sysDepart);
        }
        sysDepart.setQywxIdentifier(department.getId());
        sysDepart.setDepartName(department.getName());
        try {
            sysDepart.setDepartOrder(Integer.parseInt(department.getOrder()));
        } catch (NumberFormatException ignored) {
        }
        return sysDepart;
    }

    @Override
    public int removeThirdAppUser(List<String> userIdList) {
        // 判断启用状态
        SysThirdAppConfig config = this.getWeChatThirdAppConfig();
        if (null == config) {
            return -1;
        }
        int count = 0;
        if (userIdList != null && userIdList.size() > 0) {
            String accessToken = this.getAccessToken();
            if (accessToken == null) {
                return count;
            }
            LambdaQueryWrapper<SysThirdAccount> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysThirdAccount::getThirdType, THIRD_TYPE);
            queryWrapper.in(SysThirdAccount::getSysUserId, userIdList);
            // 根据userId，获取第三方用户的id
            List<SysThirdAccount> thirdAccountList = sysThirdAccountService.list(queryWrapper);
            List<String> thirdUserIdList = thirdAccountList.stream().map(SysThirdAccount::getThirdUserId).collect(Collectors.toList());

            for (String thirdUserId : thirdUserIdList) {
                if (oConvertUtils.isNotEmpty(thirdUserId)) {
                    // 没有批量删除的接口
                    int err = JwUserAPI.deleteUser(thirdUserId, accessToken);
                    if (err == 0) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    @Override
    public boolean sendMessage(MessageDTO message) {
        return this.sendMessage(message, false);
    }

    @Override
    public boolean sendMessage(MessageDTO message, boolean verifyConfig) {
        JSONObject response;
        if (message.isMarkdown()) {
            response = this.sendMarkdownResponse(message, verifyConfig);
        } else {
            response = this.sendMessageResponse(message, verifyConfig);
        }
        if (response != null) {
            return response.getIntValue("errcode") == 0;
        }
        return false;
    }

    public JSONObject sendMessageResponse(MessageDTO message, boolean verifyConfig) {
        SysThirdAppConfig config = this.getWeChatThirdAppConfig();
        if (verifyConfig && null == config) {
            return null;
        }
        String accessToken = this.getAppAccessToken(config);
        if (accessToken == null) {
            return null;
        }
        Text text = new Text();
        text.setMsgtype("text");
        text.setTouser(this.getTouser(message.getToUser(), message.getToAll()));
        TextEntity entity = new TextEntity();
        entity.setContent(message.getContent());
        text.setText(entity);
        text.setAgentid(Integer.parseInt(config.getAgentId()));
        return JwMessageAPI.sendTextMessage(text, accessToken);
    }

    public JSONObject sendMarkdownResponse(MessageDTO message, boolean verifyConfig) {
        SysThirdAppConfig config = this.getWeChatThirdAppConfig();
        if (verifyConfig && null == config) {
            return null;
        }
        String accessToken = this.getAppAccessToken(config);
        if (accessToken == null) {
            return null;
        }
        Markdown markdown = new Markdown();
        markdown.setTouser(this.getTouser(message.getToUser(), message.getToAll()));
        MarkdownEntity entity = new MarkdownEntity();
        entity.setContent(message.getContent());
        markdown.setMarkdown(entity);
        markdown.setAgentid(Integer.parseInt(config.getAgentId()));
        return JwMessageAPI.sendMarkdownMessage(markdown, accessToken);
    }

    /**
     * 发送文本卡片消息（SysAnnouncement定制）
     *
     * @param announcement
     * @param verifyConfig 是否验证配置（未启用的APP会拒绝发送）
     * @return
     */
    public JSONObject sendTextCardMessage(SysAnnouncement announcement, boolean verifyConfig) {
        SysThirdAppConfig config = this.getWeChatThirdAppConfig();
        if (verifyConfig && null == config) {
            return null;
        }
        String accessToken = this.getAppAccessToken(config);
        if (accessToken == null) {
            return null;
        }
        TextCard textCard = new TextCard();
        textCard.setAgentid(Integer.parseInt(config.getAgentId()));
        boolean isToAll = CommonConstant.MSG_TYPE_ALL.equals(announcement.getMsgType());
        String usernameString = "";
        if (!isToAll) {
            // 将userId转为username
            String userId = announcement.getUserIds();
            String[] userIds = null;
            if(oConvertUtils.isNotEmpty(userId)){
                userIds = userId.substring(0, (userId.length() - 1)).split(",");
            }else{
                LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SysAnnouncementSend::getAnntId, announcement.getId());
                SysAnnouncementSend sysAnnouncementSend = sysAnnouncementSendMapper.selectOne(queryWrapper);
                userIds = new String[] {sysAnnouncementSend.getUserId()};
            }

            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(SysUser::getId, userIds);
            List<SysUser> userList = userMapper.selectList(queryWrapper);
            List<String> usernameList = userList.stream().map(SysUser::getUsername).collect(Collectors.toList());
            usernameString = String.join(",", usernameList);
        }

        textCard.setTouser(this.getTouser(usernameString, isToAll));
        TextCardEntity entity = new TextCardEntity();
        entity.setTitle(announcement.getTitile());
        entity.setDescription(oConvertUtils.getString(announcement.getMsgAbstract(),"空"));
        String baseUrl = null;

        //优先通过请求获取basepath，获取不到读取 jeecg.domainUrl.pc
        try {
            baseUrl = RestUtil.getBaseUrl();
        } catch (Exception e) {
            log.warn(e.getMessage());
            baseUrl =  jeecgBaseConfig.getDomainUrl().getPc();
            //e.printStackTrace();
        }

        entity.setUrl(baseUrl + "/sys/annountCement/show/" + announcement.getId());
        textCard.setTextcard(entity);
        return JwMessageAPI.sendTextCardMessage(textCard, accessToken);
    }

    private String getTouser(String origin, boolean toAll) {
        if (toAll) {
            return "@all";
        } else {
            String[] toUsers = origin.split(",");
            // 通过第三方账号表查询出第三方userId
            int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), CommonConstant.TENANT_ID_DEFAULT_VALUE);
            List<SysThirdAccount> thirdAccountList = sysThirdAccountService.listThirdUserIdByUsername(toUsers, THIRD_TYPE,tenantId);
            List<String> toUserList = thirdAccountList.stream().map(SysThirdAccount::getThirdUserId).collect(Collectors.toList());
            // 多个接收者用‘|’分隔
            return String.join("|", toUserList);
        }
    }

    /**
     * 根据第三方登录获取到的code来获取第三方app的用户ID
     *
     * @param code
     * @return
     */
    public Map<String,String> getUserIdByThirdCode(String code, String accessToken) {
        JSONObject response = JwUserAPI.getUserInfoByCode(code, accessToken);
        if (response != null) {
            Map<String,String> map = new HashMap<>(5);
            log.info("response: " + response.toJSONString());
            if (response.getIntValue(ERR_CODE) == 0) {
                //将userTicket也返回，用于获取手机号
                String userTicket = response.getString("user_ticket");
                String appUserId = response.getString("UserId");
                map.put("userTicket",userTicket);
                map.put("appUserId",appUserId);
                return map;
            }
        }
        return null;
    }

    /**
     * OAuth2登录，成功返回登录的SysUser，失败返回null
     */
    public SysUser oauth2Login(String code,Integer tenantId) {
        Long count = tenantMapper.tenantIzExist(tenantId);
        if(ObjectUtil.isEmpty(count) || 0 == count){
            throw new JeecgBootException("租户不存在！");
        }
        //update-begin---author:wangshuai ---date:20230224  for：[QQYUN-3440]新建企业微信和钉钉配置表，通过租户模式隔离------------
        SysThirdAppConfig config = configMapper.getThirdConfigByThirdType(tenantId, MessageTypeEnum.QYWX.getType());
        String accessToken = this.getAppAccessToken(config);
        //update-end---author:wangshuai ---date:20230224  for：[QQYUN-3440]新建企业微信和钉钉配置表，通过租户模式隔离------------
        if (accessToken == null) {
            return null;
        }
        Map<String,String> map = this.getUserIdByThirdCode(code, accessToken);
        if (null != map) {
            //企业微信需要通过userTicket获取用户信息
            String appUserId = map.get("appUserId");
            String userTicket = map.get("userTicket");
            // 判断第三方用户表有没有这个人
            LambdaQueryWrapper<SysThirdAccount> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysThirdAccount::getThirdUserId, appUserId);
            queryWrapper.eq(SysThirdAccount::getThirdType, THIRD_TYPE);
            queryWrapper.eq(SysThirdAccount::getTenantId, tenantId);
            SysThirdAccount thirdAccount = sysThirdAccountService.getOne(queryWrapper);
            if (thirdAccount != null) {
                return this.getSysUserByThird(thirdAccount, null, appUserId, accessToken, userTicket,tenantId);
            } else {
                throw new JeecgBootException("该用户尚未同步，请同步后再次登录！");
            }
        }
        return null;
    }

    /**
     * 根据第三方账号获取本地账号，如果不存在就创建
     *
     * @param thirdAccount
     * @param appUser
     * @param appUserId
     * @param accessToken
     * @param userTicket 获取访问用户敏感信息
     * @return
     */
    private SysUser getSysUserByThird(SysThirdAccount thirdAccount, User appUser, String appUserId, String accessToken, String userTicket,Integer tenantId) {
        String sysUserId = thirdAccount.getSysUserId();
        if (oConvertUtils.isNotEmpty(sysUserId)) {
            return userMapper.selectById(sysUserId);
        } else {
            // 如果没有 sysUserId ，说明没有绑定账号，获取到手机号之后进行绑定
            if (appUser == null) {
                appUser = this.getUserByUserTicket(userTicket, accessToken);
            }
            // 判断系统里是否有这个手机号的用户
            SysUser sysUser = userMapper.getUserByPhone(appUser.getMobile());
            if (sysUser != null) {
                thirdAccount.setAvatar(appUser.getAvatar());
                thirdAccount.setRealname(appUser.getName());
                thirdAccount.setThirdUserId(appUser.getUserid());
                thirdAccount.setThirdUserUuid(appUser.getUserid());
                thirdAccount.setSysUserId(sysUser.getId());
                sysThirdAccountService.updateById(thirdAccount);
                return sysUser;
            } else {
                // 没有就走创建逻辑
                return sysThirdAccountService.createUser(appUser.getMobile(), appUser.getUserid(),tenantId);
            }

        }
    }

    /**
     * 根据类型和租户id获取企业微信配置
     * @return
     */
    private SysThirdAppConfig getWeChatThirdAppConfig(){
        int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
        return configMapper.getThirdConfigByThirdType(tenantId, MessageTypeEnum.QYWX.getType());
    }

    /**
     * 获取企业微信第三方用户信息
     * @param userTicket
     * @param accessToken
     * @return
     */
    private User getUserByUserTicket(String userTicket, String accessToken){
        Map<String, String> map = new HashMap<>(5);
        map.put("user_ticket",userTicket);
        //建立连接
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://qyapi.weixin.qq.com/cgi-bin/auth/getuserdetail?access_token="+accessToken);
            RequestConfig requestConfig = RequestConfig.custom()
             .setConnectTimeout(10000).setConnectionRequestTimeout(10000).setSocketTimeout(10000)
             .build();
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new StringEntity(JSONObject.toJSONString(map), ContentType.create("application/json", "utf-8")));
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            JSONObject jsonObject = JSONObject.parseObject(result);
            Integer errcode = jsonObject.getInteger("errcode");
            if(0 == errcode){
                return JSONObject.toJavaObject(jsonObject, User.class);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取企业微信绑定的用户信息
     * @return
     */
    public JwSysUserDepartVo getThirdUserByWechat(Integer tenantId) {
        JwSysUserDepartVo sysUserDepartVo = new JwSysUserDepartVo();
        //step1 获取用户id和部门id
        String accessToken = this.getAccessToken();
        if (accessToken == null) {
            throw new JeecgBootException("accessToken获取失败！");
        }
        //获取当前租户下的用户
        List<JwUserDepartVo> userList = sysUserTenantMapper.getUsersByTenantIdAndName(tenantId);
        // 获取企业微信所有的用户（只能获取userid）
        List<User> qwUsers = JwUserAPI.getUsersByDepartid("1","1",null,accessToken);
        if(oConvertUtils.isEmpty(qwUsers)){
            throw new JeecgBootException("企业微信下没查询到用户！");
        }
        List<String> userIds = new ArrayList<>();
        List<JwUserDepartVo> userWechatList = new ArrayList<>();

        for (int i = 0; i < qwUsers.size(); i++) {
            User user = qwUsers.get(i);
            String userId = qwUsers.get(i).getUserid();
            //保证用户唯一
            if(!userIds.contains(userId)){
                //step2 查看是否已经同步过了,同步过的不做处理
                SysThirdAccount oneBySysUserId = sysThirdAccountService.getOneByUuidAndThirdType(userId, THIRD_TYPE,tenantId, userId);
                if(null != oneBySysUserId){
                    userIds.add(qwUsers.get(i).getUserid());
                    userList = userList.stream().filter(item -> !item.getUserId().equals(oneBySysUserId.getSysUserId())).collect(Collectors.toList());;
                    continue;
                }
                AtomicBoolean excludeUser = new AtomicBoolean(false);
                if(ObjectUtil.isNotEmpty(qwUsers)){
                    //step3 通过名称匹配敲敲云
                    userList.forEach(item ->{
                        if(item.getRealName().equals(user.getName())){
                            item.setWechatUserId(user.getUserid());
                            item.setWechatRealName(user.getName());
                            if(ObjectUtil.isNotEmpty(user.getDepartment())){
                                item.setWechatDepartId(Arrays.toString(user.getDepartment()));
                            }
                            excludeUser.set(true);
                        }
                    });
                    userIds.add(user.getUserid());
                }
                if(!excludeUser.get()){
                    JwUserDepartVo userDepartVo = new JwUserDepartVo();
                    userDepartVo.setWechatRealName(user.getName());
                    userDepartVo.setWechatUserId(user.getUserid());
                    if(ObjectUtil.isNotEmpty(user.getDepartment())){
                        userDepartVo.setWechatDepartId(Arrays.toString(user.getDepartment()));
                    }
                    userWechatList.add(userDepartVo);
                }
            }
        }
        //step4 返回用户信息
        sysUserDepartVo.setUserList(userWechatList);
        sysUserDepartVo.setJwUserDepartVos(userList);
        return sysUserDepartVo;
    }

    /**
     * 同步企业微信和部门
     * @param jwUserDepartJson
     * @return
     */
    public SyncInfoVo syncWechatEnterpriseDepartAndUserToLocal(String jwUserDepartJson, Integer tenantId) {
        //step 1 同步部门
        //存放部门id的map
        Map<String,String> idsMap = new HashMap<>();
        SyncInfoVo syncInfoVo = this.syncThirdAppDepartmentToLocal(tenantId, idsMap);
        //step 2 同步用户及用户部门
        this.syncDepartAndUser(syncInfoVo, tenantId, idsMap, jwUserDepartJson);
        //step 3 返回同步成功或者同步失败的消息
        return syncInfoVo;
    }

    /**
     * 同步用户和部门
     *  @param syncInfoVo 存放错误信息的日志
     * @param tenantId 租户id
     * @param idsMap 部门id集合 key为企业微信的id value 为系统部门的id
     * @param jwUserDepartJson
     */
    private void syncDepartAndUser(SyncInfoVo syncInfoVo, Integer tenantId, Map<String, String> idsMap, String jwUserDepartJson) {
        if (oConvertUtils.isNotEmpty(jwUserDepartJson)) {
            JSONArray jsonArray = JSONObject.parseArray(jwUserDepartJson);
            for (Object object : jsonArray) {
                JSONObject jsonObject = JSONObject.parseObject(object.toString());
                Object userId = jsonObject.get("userId");
                String wechatUserId = jsonObject.getString("wechatUserId");
                String wechatRealName = jsonObject.getString("wechatRealName");
                Object wechatDepartId = jsonObject.get("wechatDepartId");
                String sysUserId = "";
                //step 1 新建或更新用户
                //用户id为空说明需要创建用户
                if (null == userId) {
                    SysTenant sysTenant = sysTenantMapper.selectById(tenantId);
                    String houseNumber = "";
                    //空说明没有租户直接用用户名
                    if (null != sysTenant) {
                        houseNumber = sysTenant.getHouseNumber();
                    }
                    //用户名和密码用门牌号+用户id的格式，避免用户名重复
                    String username = houseNumber + wechatUserId;
                    //新建用户
                    sysUserId = this.saveUser(username, wechatRealName, syncInfoVo, wechatUserId);
                } else {
                    //根据id查询用户
                    SysUser sysUser = userMapper.selectById(userId.toString());
                    if (null != sysUser) {
                        sysUserId = sysUser.getId();
                        //如果真实姓名为空的情况下，才会改真实姓名
                        if(oConvertUtils.isEmpty(sysUser.getRealname())){
                            sysUser.setRealname(wechatRealName);
                            //更新用户
                            userMapper.updateById(sysUser);
                        }
                        String str = String.format("用户 %s(%s) 更新成功！", sysUser.getRealname(), sysUser.getUsername());
                        syncInfoVo.addSuccessInfo(str);
                    }else{
                       syncInfoVo.addFailInfo("企业微信用户 "+wechatRealName+" 对应的组织用户没有匹配到！");
                       continue; 
                    }
                }
                if (oConvertUtils.isNotEmpty(sysUserId)) {
                    //step 2 新增租户用户表
                    this.createUserTenant(sysUserId,false,tenantId);
                    //step 3 新建或更新第三方账号表
                    SysThirdAccount sysThirdAccount = sysThirdAccountService.getOneByUuidAndThirdType(wechatUserId, THIRD_TYPE, tenantId, wechatUserId);
                    this.thirdAccountSaveOrUpdate(sysThirdAccount,sysUserId,wechatUserId,wechatRealName,tenantId);
                    //step 4 新建或更新用户部门关系表
                    if(oConvertUtils.isNotEmpty(wechatDepartId)){
                        String wechatDepartIds = wechatDepartId.toString();
                        String[] departIds = wechatDepartIds.substring(1, wechatDepartIds.length() - 1).split(",");
                        this.userDepartSaveOrUpdate(idsMap,sysUserId,departIds);
                    }
                }
            }
        } else {
            syncInfoVo.addFailInfo("用户同同步失败，请查看企业微信是否存在用户！");
        }

    }
    
    /**
     * 保存用户
     *
     * @param username 用户名
     * @param wechatRealName 企业微信用户真实姓名
     * @param syncInfo 存放成功或失败的信息
     * @param wechatUserId wechatUserId 企业微信对应的id
     * @return
     */
    private String saveUser(String username, String wechatRealName, SyncInfoVo syncInfo, String wechatUserId) {
        SysUser sysUser = new SysUser();
        sysUser.setRealname(wechatRealName);
        sysUser.setPassword(username);
        sysUser.setUsername(username);
        sysUser.setDelFlag(CommonConstant.DEL_FLAG_0);
        //设置创建时间
        sysUser.setCreateTime(new Date());
        String salt = oConvertUtils.randomGen(8);
        sysUser.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), sysUser.getPassword(), salt);
        sysUser.setPassword(passwordEncode);
        sysUser.setStatus(1);
        sysUser.setDelFlag(CommonConstant.DEL_FLAG_0);
        //用户表字段org_code不能在这里设置他的值
        sysUser.setOrgCode(null);
        try {
            userMapper.insert(sysUser);
            String str = String.format("用户 %s(%s) 创建成功！", sysUser.getRealname(), sysUser.getUsername());
            syncInfo.addSuccessInfo(str);
            return sysUser.getId();
        } catch (Exception e) {
            User user = new User();
            user.setUserid(wechatUserId);
            user.setName(wechatRealName);
            this.syncUserCollectErrInfo(e, user, syncInfo);
        }

        return "";
    }

    /**
     * 新增用户租户
     *
     * @param userId
     * @param isUpdate 是否是新增
     * @param tenantId
     */
    private void createUserTenant(String userId, Boolean isUpdate, Integer tenantId) {
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            //判断当前用户是否已在该租户下面
            Integer count = sysUserTenantMapper.userTenantIzExist(userId, tenantId);
            //count 为0 新增租户用户,否则不用新增
            if (count == 0) {
                SysUserTenant userTenant = new SysUserTenant();
                userTenant.setTenantId(tenantId);
                userTenant.setUserId(userId);
                userTenant.setStatus(isUpdate ? CommonConstant.USER_TENANT_UNDER_REVIEW : CommonConstant.USER_TENANT_NORMAL);
                sysUserTenantMapper.insert(userTenant);
            }
        }
    }

    /**
     * 新建或更新用户部门关系表
     * @param idsMap 部门id集合 key为企业微信的id value 为系统部门的id
     * @param sysUserId 系统对应的用户id
     */
    private void userDepartSaveOrUpdate(Map<String, String> idsMap, String sysUserId, String[] departIds) {
        LambdaQueryWrapper<SysUserDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysUserDepart::getUserId,sysUserId);
        for (String departId:departIds) {
            departId = departId.trim();
            if(idsMap.containsKey(departId)){
                String value = idsMap.get(departId);
                //查询用户是否在部门里面
                query.eq(SysUserDepart::getDepId,value);
                long count = sysUserDepartService.count(query);
                if(count == 0){
                    //不存在，则新增部门用户关系
                    SysUserDepart sysUserDepart = new SysUserDepart(null,sysUserId,value);
                    sysUserDepartService.save(sysUserDepart);
                }
            }    
        }
    }

    public List<JwUserDepartVo> getThirdUserBindByWechat(int tenantId) {
        return sysThirdAccountMapper.getThirdUserBindByWechat(tenantId,THIRD_TYPE);
    }
}
