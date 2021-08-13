package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeecg.qywx.api.base.JwAccessTokenAPI;
import com.jeecg.qywx.api.core.common.AccessToken;
import com.jeecg.qywx.api.department.JwDepartmentAPI;
import com.jeecg.qywx.api.department.vo.DepartMsgResponse;
import com.jeecg.qywx.api.department.vo.Department;
import com.jeecg.qywx.api.message.JwMessageAPI;
import com.jeecg.qywx.api.message.vo.Text;
import com.jeecg.qywx.api.message.vo.TextCard;
import com.jeecg.qywx.api.message.vo.TextCardEntity;
import com.jeecg.qywx.api.message.vo.TextEntity;
import com.jeecg.qywx.api.user.JwUserAPI;
import com.jeecg.qywx.api.user.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RestUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.thirdapp.ThirdAppConfig;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.SysAnnouncementSendMapper;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.model.ThirdLoginModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.vo.thirdapp.JwDepartmentTreeVo;
import org.jeecg.modules.system.vo.thirdapp.SyncInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 第三方App对接：企业微信实现类
 */
@Slf4j
@Service
public class ThirdAppWechatEnterpriseServiceImpl implements IThirdAppService {

    @Autowired
    ThirdAppConfig thirdAppConfig;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysThirdAccountService sysThirdAccountService;
    @Autowired
    private ISysUserDepartService sysUserDepartService;
    @Autowired
    private ISysPositionService sysPositionService;
    @Autowired
    private SysAnnouncementSendMapper sysAnnouncementSendMapper;

    // 第三方APP类型，当前固定为 wechat_enterprise
    public final String THIRD_TYPE = ThirdAppConfig.WECHAT_ENTERPRISE.toLowerCase();

    @Override
    public String getAccessToken() {
        String CORP_ID = thirdAppConfig.getWechatEnterprise().getClientId();
        String SECRET = thirdAppConfig.getWechatEnterprise().getClientSecret();
        AccessToken accessToken = JwAccessTokenAPI.getAccessToken(CORP_ID, SECRET);
        if (accessToken != null) {
            return accessToken.getAccesstoken();
        }
        log.warn("获取AccessToken失败");
        return null;
    }

    /** 获取APPToken，新版企业微信的秘钥是分开的 */
    public String getAppAccessToken() {
        String CORP_ID = thirdAppConfig.getWechatEnterprise().getClientId();
        String SECRET = thirdAppConfig.getWechatEnterprise().getAgentAppSecret();
        // 如果没有配置APP秘钥，就说明是老企业，可以通用秘钥
        if (oConvertUtils.isEmpty(SECRET)) {
            SECRET = thirdAppConfig.getWechatEnterprise().getClientSecret();
        }

        AccessToken accessToken = JwAccessTokenAPI.getAccessToken(CORP_ID, SECRET);
        if (accessToken != null) {
            return accessToken.getAccesstoken();
        }
        log.warn("获取AccessToken失败");
        return null;
    }

    @Override
    public boolean syncLocalDepartmentToThirdApp(String ids) {
        String accessToken = this.getAccessToken();
        if (accessToken == null) {
            return false;
        }
        // 获取企业微信所有的部门
        List<Department> departments = JwDepartmentAPI.getAllDepartment(accessToken);
        if (departments == null) {
            return false;
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
        return true;
    }

    // 递归删除部门以及子部门，由于企业微信不允许删除带有成员和子部门的部门，所以需要递归删除下子部门，然后把部门成员移动端根部门下
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

    // 递归同步部门到第三方APP
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

    @Override
    public SyncInfoVo syncThirdAppDepartmentToLocal(String ids) {
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
        this.syncDepartmentToLocalRecursion(departmentTreeList, null, username, syncInfo);
        return syncInfo;
    }

    /**
     * 递归同步部门到本地
     */
    private void syncDepartmentToLocalRecursion(List<JwDepartmentTreeVo> departmentTreeList, String sysParentId, String username, SyncInfoVo syncInfo) {
        if (departmentTreeList != null && departmentTreeList.size() != 0) {
            for (JwDepartmentTreeVo departmentTree : departmentTreeList) {
                String depId = departmentTree.getId();
                LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper<>();
                // 根据 qywxIdentifier 字段查询
                queryWrapper.eq(SysDepart::getQywxIdentifier, depId);
                SysDepart sysDepart = sysDepartService.getOne(queryWrapper);
                if (sysDepart != null) {
                    //  执行更新操作
                    SysDepart updateSysDepart = this.qwDepartmentToSysDepart(departmentTree, sysDepart);
                    if (sysParentId != null) {
                        updateSysDepart.setParentId(sysParentId);
                    }
                    try {
                        sysDepartService.updateDepartDataById(updateSysDepart, username);
                        String str = String.format("部门 %s 更新成功！", updateSysDepart.getDepartName());
                        syncInfo.addSuccessInfo(str);
                    } catch (Exception e) {
                        this.syncDepartCollectErrInfo(e, departmentTree, syncInfo);
                    }
                    if (departmentTree.hasChildren()) {
                        // 紧接着同步子级
                        this.syncDepartmentToLocalRecursion(departmentTree.getChildren(), updateSysDepart.getId(), username, syncInfo);
                    }
                } else {
                    // 执行新增操作
                    SysDepart newSysDepart = this.qwDepartmentToSysDepart(departmentTree, null);
                    if (sysParentId != null) {
                        newSysDepart.setParentId(sysParentId);
                    }
                    try {
                        sysDepartService.saveDepartData(newSysDepart, username);
                        String str = String.format("部门 %s 创建成功！", newSysDepart.getDepartName());
                        syncInfo.addSuccessInfo(str);
                    } catch (Exception e) {
                        this.syncDepartCollectErrInfo(e, departmentTree, syncInfo);
                    }
                    // 紧接着同步子级
                    if (departmentTree.hasChildren()) {
                        this.syncDepartmentToLocalRecursion(departmentTree.getChildren(), newSysDepart.getId(), username, syncInfo);
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
        List<User> qwUsers = JwUserAPI.getDetailUsersByDepartid("1", null, null, accessToken);
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
            sysUsers = sysUserService.list(queryWrapper);
        } else {
            // 获取本地所有用户
            sysUsers = sysUserService.list();
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
                    String phone = sysUser.getPhone();
                    if (!(oConvertUtils.isEmpty(phone) || phone.equals(qwUserTemp.getMobile()))) {
                        // 手机号匹配失败，再尝试用username匹配
                        String username = sysUser.getUsername();
                        if (!(oConvertUtils.isEmpty(username) || username.equals(qwUserTemp.getUserid()))) {
                            // username 匹配失败，直接跳到下一次循环继续
                            continue;
                        }
                    }
                }
                // 循环到此说明用户匹配成功，进行更新操作
                qwUser = this.sysUserToQwUser(sysUser, qwUserTemp);
                int errCode = JwUserAPI.updateUser(qwUser, accessToken);
                // 收集错误信息
                this.syncUserCollectErrInfo(errCode, sysUser, syncInfo);
                this.thirdAccountSaveOrUpdate(sysThirdAccount, sysUser.getId(), qwUser.getUserid());
                // 更新完成，直接跳到下一次外部循环继续
                continue for1;
            }
            // 循环到此说明是新用户，直接调接口创建
            qwUser = this.sysUserToQwUser(sysUser);
            int errCode = JwUserAPI.createUser(qwUser, accessToken);
            // 收集错误信息
            boolean apiSuccess = this.syncUserCollectErrInfo(errCode, sysUser, syncInfo);
            if (apiSuccess) {
                this.thirdAccountSaveOrUpdate(sysThirdAccount, sysUser.getId(), qwUser.getUserid());
            }
        }
        return syncInfo;
    }

    @Override
    public SyncInfoVo syncThirdAppUserToLocal() {
        SyncInfoVo syncInfo = new SyncInfoVo();
        String accessToken = this.getAccessToken();
        if (accessToken == null) {
            syncInfo.addFailInfo("accessToken获取失败！");
            return syncInfo;
        }
        // 获取企业微信所有的用户
        List<User> qwUsersList = JwUserAPI.getDetailUsersByDepartid("1", null, null, accessToken);
        if (qwUsersList == null) {
            syncInfo.addFailInfo("企业微信用户列表查询失败！");
            return syncInfo;
        }
        //查询本地用户
        List<SysUser> sysUsersList = sysUserService.list();
        // 循环判断新用户和需要更新的用户
        for (User qwUser : qwUsersList) {
            /*
             * 判断是否同步过的逻辑：
             * 1. 查询 sys_third_account（第三方账号表）是否有数据，如果有代表已同步
             * 2. 本地表里没有，就先用手机号判断，不通过再用username判断。
             */
            SysThirdAccount sysThirdAccount = sysThirdAccountService.getOneByThirdUserId(qwUser.getUserid(), THIRD_TYPE);
            List<SysUser> collect = sysUsersList.stream().filter(user -> (qwUser.getMobile().equals(user.getPhone()) || qwUser.getUserid().equals(user.getUsername()))
                                                                ).collect(Collectors.toList());

            if (collect != null && collect.size() > 0) {
                SysUser sysUserTemp = collect.get(0);
                // 循环到此说明用户匹配成功，进行更新操作
                SysUser updateSysUser = this.qwUserToSysUser(qwUser, sysUserTemp);
                try {
                    sysUserService.updateById(updateSysUser);
                    String str = String.format("用户 %s(%s) 更新成功！", updateSysUser.getRealname(), updateSysUser.getUsername());
                    syncInfo.addSuccessInfo(str);
                } catch (Exception e) {
                    this.syncUserCollectErrInfo(e, qwUser, syncInfo);
                }

                this.thirdAccountSaveOrUpdate(sysThirdAccount, updateSysUser.getId(), qwUser.getUserid());
                // 更新完成，直接跳到下一次外部循环继续
            }else{
                // 没匹配到用户则走新增逻辑
                SysUser newSysUser = this.qwUserToSysUser(qwUser);
                try {
                    sysUserService.save(newSysUser);
                    String str = String.format("用户 %s(%s) 创建成功！", newSysUser.getRealname(), newSysUser.getUsername());
                    syncInfo.addSuccessInfo(str);
                } catch (Exception e) {
                    this.syncUserCollectErrInfo(e, qwUser, syncInfo);
                }
                this.thirdAccountSaveOrUpdate(sysThirdAccount, newSysUser.getId(), qwUser.getUserid());
            }
        }
        return syncInfo;
    }

    /**
     * 保存或修改第三方登录表
     *
     * @param sysThirdAccount 第三方账户表对象，为null就新增数据，否则就修改
     * @param sysUserId       本地系统用户ID
     * @param qwUserId        企业微信用户ID
     */
    private void thirdAccountSaveOrUpdate(SysThirdAccount sysThirdAccount, String sysUserId, String qwUserId) {
        if (sysThirdAccount == null) {
            sysThirdAccount = new SysThirdAccount();
            sysThirdAccount.setSysUserId(sysUserId);
            sysThirdAccount.setStatus(1);
            sysThirdAccount.setDelFlag(0);
            sysThirdAccount.setThirdType(THIRD_TYPE);
        }
        sysThirdAccount.setThirdUserId(qwUserId);
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
        if (oConvertUtils.isNotEmpty(sysUser.getPost())) {
            SysPosition position = sysPositionService.getByCode(sysUser.getPost());
            if (position != null) {
                user.setPosition(position.getName());
            }
        }
        if (sysUser.getSex() != null) {
            user.setGender(sysUser.getSex().toString());
        }
        user.setEmail(sysUser.getEmail());
        // 启用/禁用成员（状态），规则不同，需要转换
        // 企业微信规则：1表示启用成员，0表示禁用成员
        // JEECG规则：1正常，2冻结
        if (sysUser.getStatus() != null) {
            if (sysUser.getStatus() == 1 || sysUser.getStatus() == 2) {
                user.setEnable(sysUser.getStatus() == 1 ? 1 : 0);
            } else {
                user.setEnable(1);
            }
        }
        user.setTelephone(sysUser.getTelephone());// 座机号
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
        sysUser.setTelephone(qwUser.getTelephone());// 座机号

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
        if (!thirdAppConfig.isWechatEnterpriseEnabled()) {
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
        JSONObject response = this.sendMessageResponse(message, verifyConfig);
        if (response != null) {
            return response.getIntValue("errcode") == 0;
        }
        return false;
    }

    public JSONObject sendMessageResponse(MessageDTO message, boolean verifyConfig) {
        if (verifyConfig && !thirdAppConfig.isWechatEnterpriseEnabled()) {
            return null;
        }
        String accessToken = this.getAppAccessToken();
        if (accessToken == null) {
            return null;
        }
        Text text = new Text();
        text.setMsgtype("text");
        text.setTouser(this.getTouser(message.getToUser(), message.isToAll()));
        TextEntity entity = new TextEntity();
        entity.setContent(message.getContent());
        text.setText(entity);
        text.setAgentid(thirdAppConfig.getWechatEnterprise().getAgentIdInt());
        return JwMessageAPI.sendTextMessage(text, accessToken);
    }

    /**
     * 发送文本卡片消息（SysAnnouncement定制）
     *
     * @param announcement
     * @param verifyConfig 是否验证配置（未启用的APP会拒绝发送）
     * @return
     */
    public JSONObject sendTextCardMessage(SysAnnouncement announcement, boolean verifyConfig) {
        if (verifyConfig && !thirdAppConfig.isWechatEnterpriseEnabled()) {
            return null;
        }
        String accessToken = this.getAppAccessToken();
        if (accessToken == null) {
            return null;
        }
        TextCard textCard = new TextCard();
        textCard.setAgentid(thirdAppConfig.getWechatEnterprise().getAgentIdInt());
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
            List<String> usernameList = sysUserService.userIdToUsername(Arrays.asList(userIds));
            usernameString = String.join(",", usernameList);
        }

        textCard.setTouser(this.getTouser(usernameString, isToAll));
        TextCardEntity entity = new TextCardEntity();
        entity.setTitle(announcement.getTitile());
        entity.setDescription(oConvertUtils.getString(announcement.getMsgAbstract(),"空"));
        entity.setUrl(RestUtil.getBaseUrl() + "/sys/annountCement/show/" + announcement.getId());
        textCard.setTextcard(entity);
        return JwMessageAPI.sendTextCardMessage(textCard, accessToken);
    }

    private String getTouser(String origin, boolean toAll) {
        if (toAll) {
            return "@all";
        } else {
            String[] toUsers = origin.split(",");
            // 通过第三方账号表查询出第三方userId
            List<SysThirdAccount> thirdAccountList = sysThirdAccountService.listThirdUserIdByUsername(toUsers, THIRD_TYPE);
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
    public String getUserIdByThirdCode(String code, String accessToken) {
        JSONObject response = JwUserAPI.getUserInfoByCode(code, accessToken);
        if (response != null) {
            log.info("response: " + response.toJSONString());
            if (response.getIntValue("errcode") == 0) {
                return response.getString("UserId");
            }
        }
        return null;
    }

    /**
     * OAuth2登录，成功返回登录的SysUser，失败返回null
     */
    public SysUser oauth2Login(String code) {
        String accessToken = this.getAppAccessToken();
        if (accessToken == null) {
            return null;
        }
        String appUserId = this.getUserIdByThirdCode(code, accessToken);
        if (appUserId != null) {
            // 判断第三方用户表有没有这个人
            LambdaQueryWrapper<SysThirdAccount> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysThirdAccount::getThirdUserUuid, appUserId);
            queryWrapper.or().eq(SysThirdAccount::getThirdUserId, appUserId);
            queryWrapper.eq(SysThirdAccount::getThirdType, THIRD_TYPE);
            SysThirdAccount thirdAccount = sysThirdAccountService.getOne(queryWrapper);
            if (thirdAccount != null) {
                return this.getSysUserByThird(thirdAccount, null, appUserId, accessToken);
            } else {
                // 直接创建新账号
                User appUser = JwUserAPI.getUserByUserid(appUserId, accessToken);
                ThirdLoginModel tlm = new ThirdLoginModel(THIRD_TYPE, appUser.getUserid(), appUser.getName(), appUser.getAvatar());
                thirdAccount = sysThirdAccountService.saveThirdUser(tlm);
                return this.getSysUserByThird(thirdAccount, appUser, null, null);
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
     * @return
     */
    private SysUser getSysUserByThird(SysThirdAccount thirdAccount, User appUser, String appUserId, String accessToken) {
        String sysUserId = thirdAccount.getSysUserId();
        if (oConvertUtils.isNotEmpty(sysUserId)) {
            return sysUserService.getById(sysUserId);
        } else {
            // 如果没有 sysUserId ，说明没有绑定账号，获取到手机号之后进行绑定
            if (appUser == null) {
                appUser = JwUserAPI.getUserByUserid(appUserId, accessToken);
            }
            // 判断系统里是否有这个手机号的用户
            SysUser sysUser = sysUserService.getUserByPhone(appUser.getMobile());
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
                return sysThirdAccountService.createUser(appUser.getMobile(), appUser.getUserid());
            }

        }
    }

}
