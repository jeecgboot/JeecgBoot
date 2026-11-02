package org.jeecg.modules.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import com.xkcoding.justauth.autoconfigure.JustAuthProperties;
import me.zhyd.oauth.config.AuthConfig;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.enums.MessageTypeEnum;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.SysAnnouncementSendMapper;
import org.jeecg.modules.system.mapper.SysThirdAppConfigMapper;
import org.jeecg.modules.system.mapper.SysTenantMapper;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.mapper.SysUserTenantMapper;
import org.jeecg.modules.system.model.ThirdLoginModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.vo.thirdapp.SyncInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.*;

//update-begin---author:jeecg ---date:2026-05-13  for：【QQYUN-12767】飞书集成-----------
/**
 * 第三方App对接：飞书实现类
 *
 * @author jeecg-boot
 * @date 2026-05-13
 */
@Slf4j
@Service
public class ThirdAppFeishuServiceImpl implements IThirdAppService {

    // ==================== 飞书 OpenAPI 常量 ====================

    private static final String FEISHU_BASE = "https://open.feishu.cn";
    /** 获取应用 app_access_token（用于 OAuth2 登录时换取用户 token） */
    private static final String APP_ACCESS_TOKEN_URL = FEISHU_BASE + "/open-apis/auth/v3/app_access_token/internal";
    /** 获取应用 tenant_access_token（用于通讯录同步、消息发送等企业级接口） */
    private static final String APP_TOKEN_URL     = FEISHU_BASE + "/open-apis/auth/v3/tenant_access_token/internal";
    /** OAuth2 code 换 user_access_token（新版 OIDC 接口，Authorization: Bearer app_access_token） */
    private static final String USER_TOKEN_URL    = FEISHU_BASE + "/open-apis/authen/v1/oidc/access_token";
    /** 获取已登录用户信息 */
    private static final String USER_INFO_URL     = FEISHU_BASE + "/open-apis/authen/v1/user_info";
    /** OAuth2 授权地址（新版，替代已废弃的 /authen/v1/index） */
    private static final String AUTHORIZE_URL     = FEISHU_BASE + "/open-apis/authen/v1/authorize";
    /** 发送消息（单发，receive_id_type=open_id） */
    private static final String SEND_MSG_URL      = FEISHU_BASE + "/open-apis/im/v1/messages";
    /** 获取子部门列表（分页） */
    private static final String DEPT_CHILDREN_URL = FEISHU_BASE + "/open-apis/contact/v3/departments/%s/children";
    /** 获取单个部门详情（用于获取根部门 open_department_id） */
    private static final String DEPT_DETAIL_URL   = FEISHU_BASE + "/open-apis/contact/v3/departments/%s";
    /** 获取租户信息（含公司名称，虚拟根部门"0"本身无名称，需从此接口取公司名） */
    private static final String TENANT_INFO_URL   = FEISHU_BASE + "/open-apis/tenant/v2/tenant/query";
    /** 获取部门下直属用户（分页） */
    private static final String DEPT_USERS_URL    = FEISHU_BASE + "/open-apis/contact/v3/users";
    //update-begin---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，改用feishuIdentifier字段存储部门ID，废弃memo前缀方案-----------
    //update-end---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，改用feishuIdentifier字段存储部门ID，废弃memo前缀方案-----------

    /** 第三方类别固定值，对应 sys_third_app_config.third_type */
    public static final String THIRD_TYPE = MessageTypeEnum.FS.getType();

    // ==================== 依赖注入 ====================

    @Autowired
    private SysThirdAppConfigMapper configMapper;
    @Autowired
    private SysTenantMapper tenantMapper;
    @Autowired
    private ISysThirdAccountService sysThirdAccountService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISysUserDepartService sysUserDepartService;
    //update-begin---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，注入公告发送记录mapper-----------
    @Autowired
    private SysAnnouncementSendMapper sysAnnouncementSendMapper;
    //update-end---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，注入公告发送记录mapper-----------
    //update-begin---author:jeecg ---date:2026-05-18  for：【QQYUN-12767】飞书集成，注入justauth配置用于yml fallback-----------
    @Autowired(required = false)
    private JustAuthProperties justAuthProperties;
    //update-end---author:jeecg ---date:2026-05-18  for：【QQYUN-12767】飞书集成，注入justauth配置用于yml fallback-----------
    @Autowired
    private SysUserTenantMapper userTenantMapper;

    // ==================== IThirdAppService 接口实现 ====================

    @Override
    public String getAccessToken() {
        //update-begin---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，改用统一配置获取方法-----------
        SysThirdAppConfig config = getFeishuThirdAppConfig();
        //update-end---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，改用统一配置获取方法-----------
        if (config != null) {
            return getTenantAccessToken(config);
        }
        log.warn("当前租户未配置飞书应用");
        return null;
    }

    /** 本地→飞书 部门同步（飞书暂不支持写回，预留接口） */
    @Override
    public SyncInfoVo syncLocalDepartmentToThirdApp(String ids) {
        SyncInfoVo syncInfo = new SyncInfoVo();
        syncInfo.addFailInfo("飞书暂不支持本地→飞书方向的部门同步，请在飞书管理后台维护通讯录，再执行飞书→本地同步");
        return syncInfo;
    }

    /** 本地→飞书 用户同步（飞书暂不支持写回，预留接口） */
    @Override
    public SyncInfoVo syncLocalUserToThirdApp(String ids) {
        SyncInfoVo syncInfo = new SyncInfoVo();
        syncInfo.addFailInfo("飞书暂不支持本地→飞书方向的用户同步，请在飞书管理后台维护通讯录，再执行飞书→本地同步");
        return syncInfo;
    }

    @Override
    public int removeThirdAppUser(List<String> userIdList) {
        return 0;
    }

    @Override
    public boolean sendMessage(MessageDTO message) {
        return sendMessage(message, false);
    }

    @Override
    public boolean sendMessage(MessageDTO message, boolean verifyConfig) {

        try {
            int tenantId = MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL
                    ? oConvertUtils.getInt(TenantContext.getTenant(), 0) : 0;
            SysThirdAppConfig config = getFeishuThirdAppConfig();
            if (config == null) {
                if (verifyConfig) {
                    log.warn("飞书应用未配置，跳过消息发送");
                }
                return false;
            }
            String appToken = getTenantAccessToken(config);
            if (appToken == null) {
                log.error("飞书获取 tenant_access_token 失败，消息发送中止");
                return false;
            }
            String toUser = message.getToUser();
            if (StringUtils.isEmpty(toUser)) {
                log.warn("飞书消息接收者为空，跳过发送");
                return false;
            }
            boolean allSuccess = true;
            for (String username : toUser.split(",")) {
                if (StringUtils.isEmpty(username.trim())) {
                    continue;
                }
                SysUser sysUser = sysUserService.getUserByName(username.trim());
                if (sysUser == null) {
                    log.warn("飞书消息发送：用户不存在，username={}", username);
                    allSuccess = false;
                    continue;
                }
                LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
                query.eq(SysThirdAccount::getThirdType, THIRD_TYPE);
                query.eq(SysThirdAccount::getTenantId, tenantId);
                query.eq(SysThirdAccount::getSysUserId, sysUser.getId());
                //update-begin---author:liusq ---date:2026-10-19  for：【QQYUN-12767】存在重复的飞书第三方账号时不再抛 TooManyResultsException，取第一条发送-----------
                SysThirdAccount thirdAccount = sysThirdAccountService.getOne(query, false);
                //update-end---author:liusq ---date:2026-10-19  for：【QQYUN-12767】存在重复的飞书第三方账号时不再抛 TooManyResultsException，取第一条发送-----------
                if (thirdAccount == null || StringUtils.isEmpty(thirdAccount.getThirdUserUuid())) {
                    log.warn("飞书消息发送：用户[{}]未绑定飞书账号", username);
                    allSuccess = false;
                    continue;
                }
                //update-begin---author:scott ---date:2026-10-09  for：【敲敲云】简流 通知 对接飞书，Markdown 内容改发卡片消息，避免飞书里显示 Markdown 原文-----------
                boolean sent = message.isMarkdown()
                        ? doSendMarkdownCardMessage(appToken, thirdAccount.getThirdUserUuid(), message.getTitle(), message.getContent())
                        : doSendTextMessage(appToken, thirdAccount.getThirdUserUuid(), message.getContent());
                if (!sent) {
                    allSuccess = false;
                }
                //update-end---author:scott ---date:2026-10-09  for：【敲敲云】简流 通知 对接飞书，Markdown 内容改发卡片消息，避免飞书里显示 Markdown 原文-----------
            }
            return allSuccess;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== 飞书 OAuth2 登录 ====================

    /**
     * 构建飞书 OAuth2 扫码登录授权地址（新版 /authen/v1/authorize）
     * <p>替代 JustAuth 1.16.1 使用的已废弃 /authen/v1/index 端点</p>
     *
     * @param config      数据库中的飞书应用配置
     * @param redirectUri 回调地址（须与飞书开发者后台安全设置一致）
     * @param state       随机状态值，用于防 CSRF 及透传租户信息
     * @return 飞书授权页面 URL
     */
    public String buildAuthorizeUrl(SysThirdAppConfig config, String redirectUri, String state) {
        try {
            return AUTHORIZE_URL
                    + "?app_id=" + URLEncoder.encode(config.getClientId(), "UTF-8")
                    + "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8")
                    + "&state=" + URLEncoder.encode(state, "UTF-8");
        } catch (Exception e) {
            log.error("构建飞书授权地址失败", e);
            return null;
        }
    }

    /**
     * 飞书 OAuth2 登录主流程：code → user_access_token → 用户信息 → 系统用户绑定
     * <p>直接调用飞书新版 API，绕过 JustAuth 1.16.1 中已废弃的接口</p>
     *
     * @param code        飞书回调返回的授权码
     * @param tenantId    当前租户 ID
     * @param redirectUri 回调地址（须与授权请求中一致）
     * @return 绑定的系统用户
     */
    public SysUser oauth2Login(String code, Integer tenantId, String redirectUri) {
        //update-begin---author:liusq ---date:2026-05-15  for：【QQYUN-12767】飞书扫码登录改用yml配置，委托给带配置参数的重载方法（null=读数据库）-----------
        return oauth2Login(code, tenantId, redirectUri, null);
        //update-end---author:liusq ---date:2026-05-15  for：【QQYUN-12767】飞书扫码登录改用yml配置，委托给带配置参数的重载方法（null=读数据库）-----------
    }

    //update-begin---author:liusq ---date:2026-05-15  for：【QQYUN-12767】飞书扫码登录改用yml配置，新增重载方法支持外部传入配置-----------
    /**
     * 飞书 OAuth2 登录主流程：支持外部传入应用配置
     * <p>扫码登录传入从 yml 读取的配置；OAuth2 内嵌登录传 null，自动从 sys_third_app_config 查询</p>
     *
     * @param code           飞书回调返回的授权码
     * @param tenantId       当前租户 ID
     * @param redirectUri    回调地址（须与授权请求中一致）
     * @param externalConfig 外部传入配置（null 时从数据库查询）
     * @return 绑定的系统用户
     */
    public SysUser oauth2Login(String code, Integer tenantId, String redirectUri, SysThirdAppConfig externalConfig) {
        SysThirdAppConfig config = (externalConfig != null) ? externalConfig : configMapper.getThirdConfigByThirdType(tenantId, THIRD_TYPE);
        if (config == null) {
            throw new JeecgBootException("飞书应用未配置，请联系管理员");
        }
        // Step1: code 换 user_access_token
        String userAccessToken = getUserAccessToken(config, code, redirectUri);
        if (userAccessToken == null) {
            throw new JeecgBootException("飞书获取 user_access_token 失败，请检查应用配置及回调地址");
        }
        // Step2: 获取用户信息
        JSONObject userInfo = getFeishuUserInfo(userAccessToken);
        if (userInfo == null) {
            throw new JeecgBootException("飞书获取用户信息失败，请检查应用权限");
        }
        String openId = userInfo.getString("open_id");
        String name   = userInfo.getString("name");
        String avatar = userInfo.getString("avatar_url");
        log.info("【飞书】OAuth2 登录，用户: openId={}, name={}", openId, name);

        // Step3: 检查租户有效性
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            Long count = tenantMapper.tenantIzExist(tenantId);
            if (ObjectUtil.isEmpty(count) || 0 == count) {
                throw new JeecgBootException("租户不存在！");
            }
        }
        // Step4: 查找或创建第三方账号记录
        LambdaQueryWrapper<SysThirdAccount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysThirdAccount::getThirdType, THIRD_TYPE);
        queryWrapper.eq(SysThirdAccount::getTenantId, tenantId);
        queryWrapper.and(w -> w.eq(SysThirdAccount::getThirdUserUuid, openId)
                .or().eq(SysThirdAccount::getThirdUserId, openId));
        List<SysThirdAccount> accountList = sysThirdAccountService.list(queryWrapper);
        SysThirdAccount thirdAccount = accountList != null && !accountList.isEmpty() ? accountList.get(0) : null;
        if (thirdAccount == null) {
            ThirdLoginModel tlm = new ThirdLoginModel(THIRD_TYPE, openId, name, avatar);
            thirdAccount = sysThirdAccountService.saveThirdUser(tlm, tenantId);
        }
        // Step5: 检查是否已绑定系统用户
        if (StringUtils.isEmpty(thirdAccount.getSysUserId())) {
            throw new JeecgBootException("该飞书账号尚未绑定系统用户，请先同步或绑定！openId=" + openId);
        }
        SysUser sysUser = sysUserService.getById(thirdAccount.getSysUserId());
        if (sysUser == null || sysUser.getStatus() == null || sysUser.getStatus() == 2) {
            throw new JeecgBootException("对应系统用户已停用或不存在，请联系管理员");
        }
        return sysUser;
    }
    //update-end---author:liusq ---date:2026-05-15  for：【QQYUN-12767】飞书扫码登录改用yml配置，新增重载方法支持外部传入配置-----------

    /**
     * 用授权码换取 user_access_token（飞书新版 OIDC 接口）
     * <p>飞书要求：Authorization: Bearer app_access_token（先获取 app_access_token，再换用户 token）</p>
     */
    private String getUserAccessToken(SysThirdAppConfig config, String code, String redirectUri) {
        // Step1: 获取 app_access_token（应用身份 token，区别于 tenant_access_token）
        String appAccessToken = getAppAccessToken(config);
        if (appAccessToken == null) {
            log.error("飞书获取 app_access_token 失败，无法换取用户 token");
            return null;
        }
        // Step2: 用 app_access_token 换取 user_access_token
        JSONObject body = new JSONObject();
        body.put("grant_type", "authorization_code");
        body.put("code", code);
        if (StringUtils.isNotEmpty(redirectUri)) {
            body.put("redirect_uri", redirectUri);
        }
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(USER_TOKEN_URL);
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
            httpPost.setHeader("Authorization", "Bearer " + appAccessToken);
            httpPost.setEntity(new StringEntity(body.toJSONString(), ContentType.APPLICATION_JSON));
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.debug("飞书 user_access_token 响应: {}", responseBody);
                JSONObject result = JSON.parseObject(responseBody);
                if (result != null && result.getIntValue("code") == 0) {
                    JSONObject data = result.getJSONObject("data");
                    return data != null ? data.getString("access_token") : null;
                }
                log.error("飞书获取 user_access_token 失败: {}", responseBody);
                return null;
            }
        } catch (Exception e) {
            log.error("飞书获取 user_access_token 异常", e);
            return null;
        }
    }

    /**
     * 获取飞书应用级 app_access_token（用于 OAuth2 登录换用户 token）
     */
    private String getAppAccessToken(SysThirdAppConfig config) {
        JSONObject body = new JSONObject();
        body.put("app_id", config.getClientId());
        body.put("app_secret", config.getClientSecret());
        JSONObject result = doPost(APP_ACCESS_TOKEN_URL, null, body.toJSONString());
        if (result != null && result.getIntValue("code") == 0) {
            return result.getString("app_access_token");
        }
        log.error("飞书获取 app_access_token 失败: {}", result);
        return null;
    }

    /**
     * 用 user_access_token 获取当前登录用户信息
     */
    private JSONObject getFeishuUserInfo(String userAccessToken) {
        JSONObject result = doGet(USER_INFO_URL, userAccessToken);
        if (result != null && result.getIntValue("code") == 0) {
            return result.getJSONObject("data");
        }
        log.error("飞书获取用户信息失败: {}", result);
        return null;
    }

    // ==================== 飞书→本地 通讯录同步 ====================

    /**
     * 飞书通讯录同步到本地（部门 + 用户）
     * <p>
     * 所需飞书应用权限（在飞书开放平台 → 应用详情 → 权限管理中开通）：<br>
     * - 获取部门信息：contact:department:read<br>
     * - 获取用户基本信息（姓名/邮箱/头像）：contact:user.base:readonly<br>
     * - 获取用户手机号：contact:user.phone:readonly（同步手机号必须开通）<br>
     * - 获取租户信息（公司名称）：tenant:readable<br>
     * 若手机号未同步，请检查是否已开通 contact:user.phone:readonly 权限并重新发布应用版本。
     *
     * @param tenantId 租户 ID
     */
    public SyncInfoVo syncFeishuDeptAndUserToLocal(Integer tenantId) {
        SyncInfoVo syncInfo = new SyncInfoVo();
        SysThirdAppConfig config = configMapper.getThirdConfigByThirdType(tenantId, THIRD_TYPE);
        if (config == null) {
            syncInfo.addFailInfo("飞书应用未配置，请先在第三方集成中配置飞书应用");
            return syncInfo;
        }
        String appToken = getTenantAccessToken(config);
        if (appToken == null) {
            syncInfo.addFailInfo("获取飞书 tenant_access_token 失败，请检查 AppID 和 AppSecret");
            return syncInfo;
        }
        log.info("开始飞书通讯录同步，tenantId={}", tenantId);

        //update-begin---author:liusq ---date:2026-06-02  for：【QQYUN-12767】修复根部门（公司节点）及其直属成员未被同步的问题
        // Step1: 构建「飞书 open_department_id → 本地 SysDepart」映射
        // 先同步根部门本身（北京敲敲云科技有限公司），再以其本地 id 作为父节点递归同步子部门
        Map<String, SysDepart> feishuDeptMap = new LinkedHashMap<>();
        SysDepart rootDepart = syncFeishuRootDept(appToken, tenantId, syncInfo);
        String rootLocalId = null;
        if (rootDepart != null) {
            feishuDeptMap.put(rootDepart.getFeishuIdentifier(), rootDepart);
            rootLocalId = rootDepart.getId();
            log.info("飞书根部门同步完成：{}", rootDepart.getDepartName());
        }
        syncFeishuDepts(appToken, "0", rootLocalId, tenantId, feishuDeptMap, syncInfo);
        //update-end---author:liusq ---date:2026-06-02  for：【QQYUN-12767】修复根部门（公司节点）及其直属成员未被同步的问题
        log.info("飞书部门同步完成，共处理 {} 个部门", feishuDeptMap.size());

        // Step2: 遍历部门，同步用户
        Set<String> syncedOpenIds = new HashSet<>();
        for (Map.Entry<String, SysDepart> entry : feishuDeptMap.entrySet()) {
            syncFeishuDeptUsers(appToken, entry.getKey(), entry.getValue(), tenantId, syncedOpenIds, syncInfo);
        }
        log.info("飞书用户同步完成，共处理 {} 个用户", syncedOpenIds.size());

        //update-begin---author:liusq ---date:2026-05-18  for：【QQYUN-12767】飞书同步增加删除逻辑：飞书侧已删除的部门/用户同步清理本地数据
        // Step3: 删除本地有 feishuIdentifier 但飞书中已不存在的部门
        // 仅在本次同步无 API 调用失败时执行，避免因接口异常导致数据被误删
        if (syncInfo.getFailInfo().isEmpty()) {
            LambdaQueryWrapper<SysDepart> deptQuery = new LambdaQueryWrapper<SysDepart>()
                    .isNotNull(SysDepart::getFeishuIdentifier)
                    .ne(SysDepart::getFeishuIdentifier, "")
                    .eq(SysDepart::getDelFlag, "0");
            if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
                deptQuery.eq(SysDepart::getTenantId, tenantId);
            }
            List<SysDepart> localFeishuDepts = sysDepartService.list(deptQuery);
            for (SysDepart dept : localFeishuDepts) {
                if (!feishuDeptMap.containsKey(dept.getFeishuIdentifier())) {
                    sysDepartService.deleteDepart(dept.getId());
                    syncInfo.addSuccessInfo("删除部门：" + dept.getDepartName());
                    log.info("飞书同步删除部门：{}，feishuIdentifier={}", dept.getDepartName(), dept.getFeishuIdentifier());
                }
            }
        }

        // Step4: 停用本地有 feishu 绑定但飞书中已不存在的用户（设为停用状态，不直接删除账号）
        // 同样仅在无 API 错误时执行
        if (syncInfo.getFailInfo().isEmpty()) {
            LambdaQueryWrapper<SysThirdAccount> accountQuery = new LambdaQueryWrapper<SysThirdAccount>()
                    .eq(SysThirdAccount::getThirdType, THIRD_TYPE)
                    .eq(SysThirdAccount::getTenantId, tenantId)
                    .isNotNull(SysThirdAccount::getThirdUserUuid)
                    .ne(SysThirdAccount::getThirdUserUuid, "");
            List<SysThirdAccount> localFeishuAccounts = sysThirdAccountService.list(accountQuery);
            for (SysThirdAccount account : localFeishuAccounts) {
                if (!syncedOpenIds.contains(account.getThirdUserUuid())
                        && StringUtils.isNotEmpty(account.getSysUserId())) {
                    SysUser user = sysUserService.getById(account.getSysUserId());
                    if (user != null && user.getStatus() != 2) {
                        user.setStatus(2);
                        sysUserService.updateById(user);
                        syncInfo.addSuccessInfo("停用已离职用户：" + user.getRealname());
                        log.info("飞书同步停用用户：{}，openId={}", user.getRealname(), account.getThirdUserUuid());
                    }
                }
            }
        }
        //update-end---author:liusq ---date:2026-05-18  for：【QQYUN-12767】飞书同步增加删除逻辑：飞书侧已删除的部门/用户同步清理本地数据

        return syncInfo;
    }

    //update-begin---author:liusq ---date:2026-06-02  for：【QQYUN-12767】修复根部门（公司节点）及其直属成员未被同步的问题
    /**
     * 同步飞书根部门（即公司顶级节点，department_id="0"）到本地。
     * <p>
     * 飞书的根部门不会出现在 children 列表中，需通过部门详情接口单独获取其
     * open_department_id 和名称，在本地创建或更新对应的 SysDepart 记录，
     * 并以其本地 id 作为所有一级子部门的父节点，保证本地树形结构与飞书一致。
     * </p>
     */
    private SysDepart syncFeishuRootDept(String appToken, Integer tenantId, SyncInfoVo syncInfo) {
        // 飞书 department_id="0" 表示根部门，使用 department_id 类型查询
        String url = String.format(DEPT_DETAIL_URL, "0") + "?department_id_type=department_id";
        JSONObject resp = doGet(url, appToken);
        if (resp == null || resp.getIntValue("code") != 0) {
            String errMsg = resp != null ? resp.getString("msg") : "网络异常";
            syncInfo.addFailInfo("获取飞书根部门信息失败: " + errMsg);
            return null;
        }
        JSONObject data = resp.getJSONObject("data");
        if (data == null) {
            syncInfo.addFailInfo("获取飞书根部门信息返回数据为空");
            return null;
        }
        JSONObject deptObj = data.getJSONObject("department");
        if (deptObj == null) {
            syncInfo.addFailInfo("获取飞书根部门信息返回 department 字段为空");
            return null;
        }
        String openDeptId = deptObj.getString("open_department_id");
        //update-begin---author:liusq ---date:2026-06-02  for：【QQYUN-12767】飞书虚拟根部门"0"本身无名称，按优先级三级回退获取公司名
        // 优先级1：department.name
        String deptName = deptObj.getString("name");
        // 优先级2：department.i18n_name.zh_cn
        if (StringUtils.isEmpty(deptName)) {
            JSONObject i18nName = deptObj.getJSONObject("i18n_name");
            if (i18nName != null) {
                deptName = i18nName.getString("zh_cn");
            }
        }
        // 优先级3：租户信息接口（虚拟根"0"无名称时，公司名存于租户配置中）
        if (StringUtils.isEmpty(deptName)) {
            JSONObject tenantResp = doGet(TENANT_INFO_URL, appToken);
            if (tenantResp != null && tenantResp.getIntValue("code") == 0) {
                JSONObject tenantData = tenantResp.getJSONObject("data");
                if (tenantData != null) {
                    JSONObject tenant = tenantData.getJSONObject("tenant");
                    if (tenant != null) {
                        deptName = tenant.getString("name");
                    }
                }
            }
        }
        if (StringUtils.isEmpty(deptName)) {
            syncInfo.addFailInfo("获取飞书根部门名称失败：department.name、i18n_name.zh_cn 和租户接口均未返回有效名称");
            return null;
        }
        //update-end---author:liusq ---date:2026-06-02  for：【QQYUN-12767】飞书虚拟根部门"0"本身无名称，按优先级三级回退获取公司名

        SysDepart localDepart = findLocalDeptByFeishuId(openDeptId, tenantId);
        if (localDepart == null) {
            localDepart = findLocalDeptByNameAndParent(deptName, null, tenantId);
        }
        if (localDepart == null) {
            localDepart = createLocalDepart(deptName, null, openDeptId, tenantId);
            if (localDepart != null) {
                syncInfo.addSuccessInfo("新增根部门：" + deptName);
            } else {
                syncInfo.addFailInfo("创建根部门失败：" + deptName);
                return null;
            }
        } else {
            boolean changed = false;
            if (!openDeptId.equals(localDepart.getFeishuIdentifier())) {
                localDepart.setFeishuIdentifier(openDeptId);
                changed = true;
            }
            if (!deptName.equals(localDepart.getDepartName())) {
                localDepart.setDepartName(deptName);
                changed = true;
            }
            if (changed) {
                sysDepartService.updateDepartDataById(localDepart, "admin");
            }
            syncInfo.addSuccessInfo("更新根部门：" + deptName);
        }
        return localDepart;
    }
    //update-end---author:liusq ---date:2026-06-02  for：【QQYUN-12767】修复根部门（公司节点）及其直属成员未被同步的问题

    /**
     * 递归同步飞书部门到本地
     */
    private void syncFeishuDepts(String appToken, String parentOpenDeptId, String localParentId,
                                  Integer tenantId, Map<String, SysDepart> feishuDeptMap, SyncInfoVo syncInfo) {
        String pageToken = null;
        do {
            String url = String.format(DEPT_CHILDREN_URL, parentOpenDeptId) + "?page_size=50&department_id_type=open_department_id";
            if (pageToken != null) {
                url += "&page_token=" + pageToken;
            }
            JSONObject resp = doGet(url, appToken);
            if (resp == null || resp.getIntValue("code") != 0) {
                String errMsg = resp != null ? resp.getString("msg") : "网络异常";
                syncInfo.addFailInfo("获取飞书部门[" + parentOpenDeptId + "]子部门失败: " + errMsg);
                return;
            }
            JSONObject data = resp.getJSONObject("data");
            if (data == null) {
                break;
            }
            JSONArray items = data.getJSONArray("items");
            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    JSONObject deptObj = items.getJSONObject(i);
                    String openDeptId = deptObj.getString("open_department_id");
                    String deptName   = deptObj.getString("name");

                    SysDepart localDepart = findLocalDeptByFeishuId(openDeptId, tenantId);
                    if (localDepart == null) {
                        localDepart = findLocalDeptByNameAndParent(deptName, localParentId, tenantId);
                    }
                    if (localDepart == null) {
                        localDepart = createLocalDepart(deptName, localParentId, openDeptId, tenantId);
                        if (localDepart != null) {
                            syncInfo.addSuccessInfo("新增部门：" + deptName);
                        } else {
                            syncInfo.addFailInfo("创建部门失败：" + deptName);
                            continue;
                        }
                    } else {
                        //update-begin---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，更新时同步修正parentId和feishuIdentifier，修复部门层级丢失问题-----------
                        boolean changed = false;
                        if (!openDeptId.equals(localDepart.getFeishuIdentifier())) {
                            localDepart.setFeishuIdentifier(openDeptId);
                            changed = true;
                        }
                        if (!StringUtils.equals(localParentId, localDepart.getParentId())) {
                            localDepart.setParentId(localParentId);
                            changed = true;
                        }
                        //update-end---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，更新时同步修正parentId和feishuIdentifier，修复部门层级丢失问题-----------
                        //update-begin---author:liusq ---date:2026-05-18  for：【QQYUN-12767】飞书同步部门时补充名称更新，飞书改名后本地同步更新-----------
                        if (!deptName.equals(localDepart.getDepartName())) {
                            localDepart.setDepartName(deptName);
                            changed = true;
                        }
                        //update-end---author:liusq ---date:2026-05-18  for：【QQYUN-12767】飞书同步部门时补充名称更新，飞书改名后本地同步更新-----------
                        if (changed) {
                            if (StringUtils.isNotEmpty(localParentId)) {
                                sysDepartService.updateIzLeaf(localParentId, CommonConstant.NOT_LEAF);
                            }
                            sysDepartService.updateDepartDataById(localDepart, "admin");
                        }
                        syncInfo.addSuccessInfo("更新部门：" + deptName);
                    }
                    feishuDeptMap.put(openDeptId, localDepart);
                    syncFeishuDepts(appToken, openDeptId, localDepart.getId(), tenantId, feishuDeptMap, syncInfo);
                }
            }
            boolean hasMore = Boolean.TRUE.equals(data.getBoolean("has_more"));
            pageToken = hasMore ? data.getString("page_token") : null;
        } while (pageToken != null);
    }

    /**
     * 同步飞书某部门下的直属用户到本地
     */
    private void syncFeishuDeptUsers(String appToken, String openDeptId, SysDepart localDepart,
                                      Integer tenantId, Set<String> syncedOpenIds, SyncInfoVo syncInfo) {
        String pageToken = null;
        do {
            String url = DEPT_USERS_URL + "?department_id=" + openDeptId
                       + "&department_id_type=open_department_id&page_size=50&user_id_type=open_id";
            if (pageToken != null) {
                url += "&page_token=" + pageToken;
            }
            JSONObject resp = doGet(url, appToken);
            if (resp == null || resp.getIntValue("code") != 0) {
                String errMsg = resp != null ? resp.getString("msg") : "网络异常";
                syncInfo.addFailInfo("获取飞书部门[" + openDeptId + "]用户列表失败: " + errMsg);
                return;
            }
            JSONObject data = resp.getJSONObject("data");
            if (data == null) {
                break;
            }
            JSONArray items = data.getJSONArray("items");
            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    JSONObject userObj = items.getJSONObject(i);
                    String openId  = userObj.getString("open_id");
                    if (syncedOpenIds.contains(openId)) {
                        if (localDepart != null) {
                            linkUserToDepart(openId, tenantId, localDepart.getId());
                        }
                        continue;
                    }
                    syncedOpenIds.add(openId);

                    String name      = userObj.getString("name");
                    String email     = userObj.getString("email");
                    String mobile    = userObj.getString("mobile");
                    String unionId   = userObj.getString("union_id");
                    JSONObject avatar = userObj.getJSONObject("avatar");
                    String avatarUrl = avatar != null ? avatar.getString("avatar_72") : null;
                    //update-begin---author:liusq ---date:2026-06-02  for：【QQYUN-12767】手机号为空时打印warn，引导用户检查飞书应用权限
                    if (StringUtils.isEmpty(mobile)) {
                        log.warn("飞书用户[{}]手机号未返回，请确认飞书应用已开通 contact:user.phone:readonly 权限并重新发布应用版本", name);
                    }
                    //update-end---author:liusq ---date:2026-06-02  for：【QQYUN-12767】手机号为空时打印warn，引导用户检查飞书应用权限

                    //update-begin---author:liusq ---date:2026-10-18  for：【QQYUN-12767】飞书同步单个用户异常时记录失败信息并继续，避免一条脏数据中断整次同步-----------
                    try {
                        LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
                        query.eq(SysThirdAccount::getThirdType, THIRD_TYPE);
                        query.eq(SysThirdAccount::getTenantId, tenantId);
                        query.and(w -> w.eq(SysThirdAccount::getThirdUserUuid, openId)
                                .or().eq(SysThirdAccount::getThirdUserId, openId));
                        //update-begin---author:liusq ---date:2026-10-18  for：【QQYUN-12767】存在重复的飞书第三方账号时不再抛 TooManyResultsException，优先取已绑定系统用户的记录-----------
                        List<SysThirdAccount> thirdAccounts = sysThirdAccountService.list(query);
                        if (thirdAccounts.size() > 1) {
                            log.warn("飞书用户[{}]存在 {} 条重复的第三方账号记录，请清理 sys_third_account 重复数据，openId={}", name, thirdAccounts.size(), openId);
                        }
                        SysThirdAccount thirdAccount = thirdAccounts.stream()
                                .filter(a -> StringUtils.isNotEmpty(a.getSysUserId()))
                                .findFirst()
                                .orElse(thirdAccounts.isEmpty() ? null : thirdAccounts.get(0));
                        //update-end---author:liusq ---date:2026-10-18  for：【QQYUN-12767】存在重复的飞书第三方账号时不再抛 TooManyResultsException，优先取已绑定系统用户的记录-----------

                        //update-begin---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，已绑定用户执行信息更新（姓名/手机/邮箱/头像），修复同步时已存在用户被跳过不更新的问题-----------
                        if (thirdAccount != null && StringUtils.isNotEmpty(thirdAccount.getSysUserId())) {
                            // 已有绑定关系，更新用户基本信息
                            SysUser existUser = sysUserService.getById(thirdAccount.getSysUserId());
                            if (existUser != null) {
                                existUser.setRealname(name);
                                if (StringUtils.isNotEmpty(email)) {
                                    existUser.setEmail(email);
                                }
                                if (StringUtils.isNotEmpty(mobile)) {
                                    existUser.setPhone(mobile.replaceAll("^\\+86\\s*", "").trim());
                                }
                                if (StringUtils.isNotEmpty(avatarUrl)) {
                                    existUser.setAvatar(avatarUrl);
                                }
                                sysUserService.updateById(existUser);
                                //update-begin---author:liusq ---date:2026-10-19  for：【QQYUN-12767】飞书同步用户未关联当前租户，补齐历史同步缺失的租户关系-----------
                                createUserTenant(existUser.getId(), false, tenantId);
                                //update-end---author:liusq ---date:2026-10-19  for：【QQYUN-12767】飞书同步用户未关联当前租户，补齐历史同步缺失的租户关系-----------
                            }
                            syncInfo.addSuccessInfo("更新已存在用户：" + name);
                            if (localDepart != null) {
                                linkUserToDepart(openId, tenantId, localDepart.getId());
                            }
                            continue;
                        }
                        //update-end---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，已绑定用户执行信息更新（姓名/手机/邮箱/头像），修复同步时已存在用户被跳过不更新的问题-----------

                        //update-begin---author:jeecg ---date:2026-08-04  for：【QQYUN-12767】飞书同步按手机号查重：手机号已存在的本地用户直接绑定，不新增用户-----------
                        // 兜底逻辑：未通过 thirdUserUuid/thirdUserId 找到绑定记录时，先按手机号匹配本地用户
                        // 命中则直接将飞书 openId 绑定到该现有用户，避免重复创建账号（对齐钉钉 addDepartUser 行为）
                        String strippedMobile = StringUtils.isNotEmpty(mobile) ? mobile.replaceAll("^\\+86\\s*", "").trim() : null;
                        SysUser userByPhone = StringUtils.isNotEmpty(strippedMobile) ? userMapper.getUserByPhone(strippedMobile) : null;
                        if (userByPhone != null) {
                            try {
                                // 1. 用飞书最新信息更新本地已有用户（仅更新邮箱/头像；姓名由业务自行维护，飞书姓名可能与系统姓名不一致，不覆盖）
                                boolean userChanged = false;
                                if (StringUtils.isNotEmpty(email) && !email.equals(userByPhone.getEmail())) {
                                    userByPhone.setEmail(email);
                                    userChanged = true;
                                }
                                if (StringUtils.isNotEmpty(avatarUrl) && !avatarUrl.equals(userByPhone.getAvatar())) {
                                    userByPhone.setAvatar(avatarUrl);
                                    userChanged = true;
                                }
                                if (userChanged) {
                                    sysUserService.updateById(userByPhone);
                                }
                                // 2. 复用或创建第三方账号记录，并将 sysUserId 绑定到该现有用户
                                SysThirdAccount accountToBind;
                                if (thirdAccount == null) {
                                    ThirdLoginModel tlm = new ThirdLoginModel(THIRD_TYPE, openId, name, avatarUrl);
                                    accountToBind = sysThirdAccountService.saveThirdUser(tlm, tenantId);
                                } else {
                                    accountToBind = thirdAccount;
                                    accountToBind.setThirdUserUuid(openId);
                                    accountToBind.setThirdUserId(openId);
                                    accountToBind.setRealname(name);
                                    if (StringUtils.isNotEmpty(avatarUrl)) {
                                        accountToBind.setAvatar(avatarUrl);
                                    }
                                    sysThirdAccountService.updateById(accountToBind);
                                }
                                if (StringUtils.isEmpty(accountToBind.getSysUserId())
                                        || !accountToBind.getSysUserId().equals(userByPhone.getId())) {
                                    accountToBind.setSysUserId(userByPhone.getId());
                                    sysThirdAccountService.updateById(accountToBind);
                                }
                                //update-begin---author:jeecg ---date:2026-10-19  for：【QQYUN-12767】飞书同步用户未关联当前租户，按手机号匹配的已有用户加入租户（审核中，对齐钉钉）-----------
                                createUserTenant(userByPhone.getId(), true, tenantId);
                                //update-end---author:jeecg ---date:2026-10-19  for：【QQYUN-12767】飞书同步用户未关联当前租户，按手机号匹配的已有用户加入租户（审核中，对齐钉钉）-----------
                                // 3. 关联部门
                                if (localDepart != null) {
                                    linkUserToDepart(openId, tenantId, localDepart.getId());
                                }
                                syncInfo.addSuccessInfo("按手机号绑定已有用户：" + name + "（" + strippedMobile + "）");
                                log.info("飞书同步按手机号绑定已有用户：openId={}, phone={}, sysUserId={}",
                                        openId, strippedMobile, userByPhone.getId());
                            } catch (Exception e) {
                                log.error("飞书同步按手机号绑定用户异常，name={}", name, e);
                                syncInfo.addFailInfo("按手机号绑定用户[" + name + "]异常：" + e.getMessage());
                            }
                            continue;
                        }
                        //update-end---author:jeecg ---date:2026-08-04  for：【QQYUN-12767】飞书同步按手机号查重：手机号已存在的本地用户直接绑定，不新增用户-----------

                        SysUser newUser = createLocalUser(name, email, mobile, openId, unionId, avatarUrl, tenantId);
                        if (newUser != null) {
                            ThirdLoginModel tlm = new ThirdLoginModel(THIRD_TYPE, openId, name, avatarUrl);
                            SysThirdAccount saved = sysThirdAccountService.saveThirdUser(tlm, tenantId);
                            saved.setSysUserId(newUser.getId());
                            sysThirdAccountService.updateById(saved);
                            //update-begin---author:liusq ---date:2026-10-19  for：【QQYUN-12767】飞书同步用户未关联当前租户，新建用户加入当前租户-----------
                            createUserTenant(newUser.getId(), false, tenantId);
                            //update-end---author:liusq ---date:2026-10-19  for：【QQYUN-12767】飞书同步用户未关联当前租户，新建用户加入当前租户-----------
                            if (localDepart != null) {
                                SysUserDepart userDepart = new SysUserDepart(newUser.getId(), localDepart.getId());
                                sysUserDepartService.save(userDepart);
                            }
                            syncInfo.addSuccessInfo("新增用户：" + name
                                    + (StringUtils.isNotEmpty(email) ? "（" + email + "）" : ""));
                        } else {
                            syncInfo.addFailInfo("创建用户失败：" + name);
                        }
                    } catch (Exception e) {
                        log.error("飞书用户同步异常，name={}, openId={}", name, openId, e);
                        syncInfo.addFailInfo("同步用户[" + name + "]异常：" + e.getMessage());
                    }
                    //update-end---author:liusq ---date:2026-10-19  for：【QQYUN-12767】飞书同步单个用户异常时记录失败信息并继续，避免一条脏数据中断整次同步-----------
                }
            }
            boolean hasMore = Boolean.TRUE.equals(data.getBoolean("has_more"));
            pageToken = hasMore ? data.getString("page_token") : null;
        } while (pageToken != null);
    }

    // ==================== 本地数据操作辅助方法 ====================

    private SysDepart findLocalDeptByFeishuId(String openDeptId, Integer tenantId) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        //update-begin---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，改用feishuIdentifier字段查询，与企业微信/钉钉保持一致-----------
        query.eq(SysDepart::getFeishuIdentifier, openDeptId);
        //update-end---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，改用feishuIdentifier字段查询，与企业微信/钉钉保持一致-----------
        query.eq(SysDepart::getDelFlag, "0");
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            query.eq(SysDepart::getTenantId, tenantId);
        }
        return sysDepartService.getOne(query, false);
    }

    private SysDepart findLocalDeptByNameAndParent(String departName, String parentId, Integer tenantId) {
        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
        query.eq(SysDepart::getDepartName, departName);
        query.eq(SysDepart::getDelFlag, "0");
        if (StringUtils.isNotEmpty(parentId)) {
            query.eq(SysDepart::getParentId, parentId);
        } else {
            query.isNull(SysDepart::getParentId);
        }
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            query.eq(SysDepart::getTenantId, tenantId);
        }
        List<SysDepart> list = sysDepartService.list(query);
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    private SysDepart createLocalDepart(String departName, String parentId, String openDeptId, Integer tenantId) {
        try {
            SysDepart depart = new SysDepart();
            depart.setDepartName(departName);
            depart.setParentId(parentId);
            depart.setStatus("1");
            //update-begin---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，改用feishuIdentifier字段存储部门ID-----------
            depart.setFeishuIdentifier(openDeptId);
            //update-end---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，改用feishuIdentifier字段存储部门ID-----------
            depart.setTenantId(tenantId);
            // 根部门（无父级）设为公司类型，子部门设为组织机构类型，与钉钉/企业微信同步保持一致
            if (StringUtils.isEmpty(parentId)) {
                depart.setOrgCategory("1");
            } else {
                depart.setOrgCategory("2");
            }
            //update-begin---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，使用saveDepartData正确生成orgCode和层级树结构，修复同步后部门层级丢失的问题-----------
            // saveDepartData 会自动生成 orgCode、orgType、izLeaf，并更新父节点 izLeaf，保证树结构正确
            // 若同级存在格式异常的 orgCode 导致 saveDepartData 抛异常，则回退到手动设值保存，确保 parentId/izLeaf 至少正确
            try {
                sysDepartService.saveDepartData(depart, "admin");
            } catch (Exception saveEx) {
                log.warn("saveDepartData 生成 orgCode 失败（可能存在格式异常的同级部门），回退手动保存，departName={}", departName, saveEx);
                depart.setId(com.baomidou.mybatisplus.core.toolkit.IdWorker.getIdStr(depart));
                depart.setOrgCode("FS" + System.currentTimeMillis() % 100000);
                depart.setOrgType(StringUtils.isNotEmpty(parentId) ? "2" : "1");
                depart.setIzLeaf(org.jeecg.common.constant.CommonConstant.IS_LEAF);
                depart.setCreateTime(new Date());
                depart.setDelFlag("0");
                if (StringUtils.isNotEmpty(parentId)) {
                    sysDepartService.updateIzLeaf(parentId, org.jeecg.common.constant.CommonConstant.NOT_LEAF);
                }
                sysDepartService.save(depart);
            }
            //update-end---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，使用saveDepartData正确生成orgCode和层级树结构，修复同步后部门层级丢失的问题-----------
            return depart;
        } catch (Exception e) {
            log.error("创建本地部门失败，departName={}", departName, e);
            return null;
        }
    }

    private void linkUserToDepart(String openId, Integer tenantId, String localDeptId) {
        try {
            LambdaQueryWrapper<SysThirdAccount> q = new LambdaQueryWrapper<>();
            q.eq(SysThirdAccount::getThirdType, THIRD_TYPE);
            q.eq(SysThirdAccount::getTenantId, tenantId);
            q.eq(SysThirdAccount::getThirdUserUuid, openId);
            SysThirdAccount ta = sysThirdAccountService.getOne(q, false);
            if (ta == null || StringUtils.isEmpty(ta.getSysUserId())) {
                return;
            }
            LambdaQueryWrapper<SysUserDepart> existQuery = new LambdaQueryWrapper<>();
            existQuery.eq(SysUserDepart::getUserId, ta.getSysUserId());
            existQuery.eq(SysUserDepart::getDepId, localDeptId);
            if (sysUserDepartService.count(existQuery) == 0) {
                SysUserDepart ud = new SysUserDepart(ta.getSysUserId(), localDeptId);
                sysUserDepartService.save(ud);
            }
        } catch (Exception e) {
            log.warn("补充用户部门关联失败，openId={}, deptId={}", openId, localDeptId, e);
        }
    }

    //update-begin---author:jeecg ---date:2026-10-16  for：【QQYUN-12767】飞书同步用户未关联当前租户，补充写入 sys_user_tenant-----------
    /**
     * 将用户加入租户（sys_user_tenant），已存在关系则跳过；未开启多租户时不处理。
     * <p>对齐钉钉 / 企业微信同步中的 createUserTenant 逻辑。</p>
     *
     * @param userId      系统用户 ID
     * @param underReview true=审核中（按手机号匹配到的已有用户，需租户管理员审核，对齐钉钉）；false=正常
     * @param tenantId    租户 ID
     */
    private void createUserTenant(String userId, boolean underReview, Integer tenantId) {
        if (!MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL || StringUtils.isEmpty(userId)) {
            return;
        }
        Integer count = userTenantMapper.userTenantIzExist(userId, tenantId);
        if (count == null || count == 0) {
            SysUserTenant userTenant = new SysUserTenant();
            userTenant.setTenantId(tenantId);
            userTenant.setUserId(userId);
            userTenant.setStatus(underReview ? CommonConstant.USER_TENANT_UNDER_REVIEW : CommonConstant.USER_TENANT_NORMAL);
            userTenantMapper.insert(userTenant);
        }
    }
    //update-end---author:jeecg ---date:2026-10-16  for：【QQYUN-12767】飞书同步用户未关联当前租户，补充写入 sys_user_tenant-----------

    private SysUser createLocalUser(String name, String email, String mobile,
                                     String openId, String unionId, String avatar, Integer tenantId) {
        String username = "feishu_" + (openId.length() > 8 ? openId.substring(openId.length() - 8) : openId);
        if (sysUserService.getUserByName(username) != null) {
            username = username + "_" + oConvertUtils.randomGen(4);
        }
        String salt = oConvertUtils.randomGen(8);
        String password = PasswordUtil.encrypt(username, oConvertUtils.randomGen(10), salt);
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setRealname(name);
        user.setPassword(password);
        user.setSalt(salt);
        user.setEmail(email);
        user.setPhone(StringUtils.isNotEmpty(mobile) ? mobile.replaceAll("^\\+86\\s*", "").trim() : null);
        user.setAvatar(avatar);
        user.setStatus(1);
        user.setDelFlag(0);
        user.setActivitiSync(1);
        user.setSort(CommonConstant.DEFAULT_USER_SORT);
        try {
            sysUserService.addUserWithRole(user, "");
            return user;
        } catch (Exception e) {
            log.error("创建本地用户失败，name={}", name, e);
            return null;
        }
    }

    // ==================== 飞书 API 工具方法 ====================

    //update-begin---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，统一获取飞书配置，未开启多租户时使用默认租户-----------
    /**
     * 根据当前租户获取飞书配置。
     * <p>未开启多租户（OPEN_SYSTEM_TENANT_CONTROL=false）时，固定使用 tenantId=0 查询默认配置，
     * 对齐钉钉 getDingThirdAppConfig() / 企业微信 getWeChatThirdAppConfig() 的处理方式。</p>
     * <p>若数据库中不存在配置，则 fallback 到 justauth yml 中的 FEISHU 配置项（client-id/client-secret）。</p>
     */
    private SysThirdAppConfig getFeishuThirdAppConfig() {
        int tenantId = MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL
                ? oConvertUtils.getInt(TenantContext.getTenant(), 0)
                : 0;
        SysThirdAppConfig config = configMapper.getThirdConfigByThirdType(tenantId, THIRD_TYPE);
        //update-begin---author:jeecg ---date:2026-05-18  for：【QQYUN-12767】飞书集成，数据库无配置时fallback到yml配置项-----------
        if (config == null) {
            config = buildFeishuConfigFromYaml();
        }
        //update-end---author:jeecg ---date:2026-05-18  for：【QQYUN-12767】飞书集成，数据库无配置时fallback到yml配置项-----------
        return config;
    }

    /**
     * 从 justauth yml 配置中读取飞书 app_id/app_secret，构建临时 SysThirdAppConfig。
     * client-id 为空或为占位符 "??" 时返回 null。
     */
    private SysThirdAppConfig buildFeishuConfigFromYaml() {
        if (justAuthProperties == null || justAuthProperties.getType() == null) {
            return null;
        }
        AuthConfig authConfig = justAuthProperties.getType().get(CommonConstant.FEISHU);
        if (authConfig == null || authConfig.getClientId() == null || "??".equals(authConfig.getClientId().trim())) {
            return null;
        }
        SysThirdAppConfig config = new SysThirdAppConfig();
        config.setClientId(authConfig.getClientId());
        config.setClientSecret(authConfig.getClientSecret());
        config.setTenantId(CommonConstant.TENANT_ID_DEFAULT_VALUE);
        return config;
    }
    //update-end---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，统一获取飞书配置，未开启多租户时使用默认租户-----------

    //update-begin---author:jeecg ---date:2026-05-18  for：【QQYUN-12767】飞书集成，新增HTML转飞书post富文本工具方法-----------
    /**
     * 将 HTML 富文本转换为飞书 post 消息的多行标签结构（contentRows）。
     * <p>
     * 支持转换规则：
     * <ul>
     *   <li>{@code <br>}、{@code </p>}、{@code </div>}、{@code </li>} → 换行（新增一行）</li>
     *   <li>{@code <b>}、{@code <strong>} → 加粗文本</li>
     *   <li>{@code <i>}、{@code <em>} → 斜体文本</li>
     *   <li>{@code <u>} → 下划线文本</li>
     *   <li>{@code <a href="...">} → 超链接（tag: a）</li>
     *   <li>{@code <li>} → 自动前置项目符号 "• "</li>
     *   <li>其余未识别标签：剥掉标签保留文字内容</li>
     *   <li>HTML 实体：{@code &nbsp;} → 空格，{@code &amp;} → &，{@code &lt;} → <，{@code &gt;} → ></li>
     * </ul>
     *
     * @param html 原始 HTML 字符串
     * @return 飞书 post content 多行结构（每行为 JSONArray，行内为各标签 JSONObject）
     */
    private JSONArray htmlToFeishuPostRows(String html) {
        JSONArray rows = new JSONArray();
        if (html == null || html.trim().isEmpty()) {
            return rows;
        }
        // Step1：将块级换行标签转换为 \n，<li> 追加项目符号
        String normalized = html
                .replaceAll("(?i)<br\\s*/?>", "\n")
                .replaceAll("(?i)</p\\s*>", "\n")
                .replaceAll("(?i)</div\\s*>", "\n")
                .replaceAll("(?i)</li\\s*>", "\n")
                .replaceAll("(?i)<li[^>]*>", "• ");
        // Step2：按换行切行，逐行解析内联标签
        String[] lines = normalized.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            JSONArray row = parseInlineHtml(line);
            if (!row.isEmpty()) {
                rows.add(row);
            }
        }
        // 兜底：若解析结果为空，将所有标签剥掉后作为单行纯文本输出
        if (rows.isEmpty()) {
            String plain = html.replaceAll("<[^>]+>", "")
                    .replaceAll("&nbsp;", " ").replaceAll("&amp;", "&")
                    .replaceAll("&lt;", "<").replaceAll("&gt;", ">").trim();
            if (!plain.isEmpty()) {
                JSONArray fallbackRow = new JSONArray();
                addFeishuTextTag(fallbackRow, plain, false, false, false);
                rows.add(fallbackRow);
            }
        }
        return rows;
    }

    /**
     * 解析单行 HTML 中的内联标签，返回该行的飞书 tag 列表。
     * 处理：{@code <b>/<strong>}、{@code <i>/<em>}、{@code <u>}、{@code <a href>}，其余标签忽略。
     */
    private JSONArray parseInlineHtml(String line) {
        JSONArray row = new JSONArray();
        java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("(<[^>]+>|[^<]+)", java.util.regex.Pattern.DOTALL)
                .matcher(line);
        boolean bold = false, italic = false, underline = false;
        boolean insideLink = false;
        String hrefPending = null;
        StringBuilder linkText = new StringBuilder();
        while (m.find()) {
            String token = m.group();
            if (token.charAt(0) == '<') {
                // HTML 标签
                String lower = token.toLowerCase().trim();
                if (lower.equals("<b>") || lower.equals("<strong>") || lower.startsWith("<strong ")) {
                    bold = true;
                } else if (lower.equals("</b>") || lower.equals("</strong>")) {
                    bold = false;
                } else if (lower.equals("<i>") || lower.equals("<em>") || lower.startsWith("<em ")) {
                    italic = true;
                } else if (lower.equals("</i>") || lower.equals("</em>")) {
                    italic = false;
                } else if (lower.equals("<u>")) {
                    underline = true;
                } else if (lower.equals("</u>")) {
                    underline = false;
                } else if (lower.startsWith("<a ")) {
                    java.util.regex.Matcher hrefM = java.util.regex.Pattern
                            .compile("href=[\"']([^\"']*)[\"']", java.util.regex.Pattern.CASE_INSENSITIVE)
                            .matcher(token);
                    hrefPending = hrefM.find() ? hrefM.group(1) : null;
                    insideLink = true;
                    linkText.setLength(0);
                } else if (lower.equals("</a>")) {
                    if (insideLink) {
                        String lt = linkText.toString();
                        if (hrefPending != null && !lt.isEmpty()) {
                            JSONObject tag = new JSONObject();
                            tag.put("tag", "a");
                            tag.put("text", lt);
                            tag.put("href", hrefPending);
                            row.add(tag);
                        } else {
                            // 没有 href，退化为普通文本
                            addFeishuTextTag(row, lt, bold, italic, underline);
                        }
                    }
                    insideLink = false;
                    hrefPending = null;
                    linkText.setLength(0);
                }
                // 其余标签：忽略标签本身，文字内容会在下一次 text token 时处理
            } else {
                // 纯文本片段，进行简单 HTML 实体解码
                String text = token
                        .replaceAll("&nbsp;", " ").replaceAll("&amp;", "&")
                        .replaceAll("&lt;", "<").replaceAll("&gt;", ">");
                if (insideLink) {
                    linkText.append(text);
                } else if (!text.isEmpty()) {
                    addFeishuTextTag(row, text, bold, italic, underline);
                }
            }
        }
        return row;
    }

    /**
     * 向飞书 post 行（row）中追加一个文本 tag，按需设置 bold/italic/underline。
     */
    private void addFeishuTextTag(JSONArray row, String text, boolean bold, boolean italic, boolean underline) {
        if (text == null || text.isEmpty()) {
            return;
        }
        JSONObject tag = new JSONObject();
        tag.put("tag", "text");
        tag.put("text", text);
        if (bold) {
            tag.put("bold", true);
        }
        if (italic) {
            tag.put("italic", true);
        }
        if (underline) {
            tag.put("underline", true);
        }
        row.add(tag);
    }
    //update-end---author:jeecg ---date:2026-05-18  for：【QQYUN-12767】飞书集成，新增HTML转飞书post富文本工具方法-----------

    /**
     * 获取飞书应用级 tenant_access_token（用于通讯录同步、消息发送等应用身份接口）
     */
    public String getTenantAccessToken(SysThirdAppConfig config) {
        if (config == null) {
            return null;
        }
        JSONObject body = new JSONObject();
        body.put("app_id", config.getClientId());
        body.put("app_secret", config.getClientSecret());
        JSONObject result = doPost(APP_TOKEN_URL, null, body.toJSONString());
        if (result != null && result.getIntValue("code") == 0) {
            return result.getString("tenant_access_token");
        }
        log.error("飞书获取 tenant_access_token 失败: {}", result);
        return null;
    }

    /**
     * 向指定飞书用户（open_id）发送文本消息
     */
    private boolean doSendTextMessage(String appToken, String openId, String content) {
        String url = SEND_MSG_URL + "?receive_id_type=open_id";
        JSONObject msgContent = new JSONObject();
        msgContent.put("text", content);
        JSONObject body = new JSONObject();
        body.put("receive_id", openId);
        body.put("msg_type", "text");
        body.put("content", msgContent.toJSONString());
        JSONObject result = doPost(url, appToken, body.toJSONString());
        if (result != null && result.getIntValue("code") == 0) {
            log.info("飞书消息发送成功: openId={}", openId);
            return true;
        }
        log.error("飞书消息发送失败: openId={}, result={}", openId, result);
        return false;
    }

    //update-begin---author:scott ---date:2026-10-09  for：【敲敲云】简流 通知 对接飞书，Markdown 内容改发卡片消息，避免飞书里显示 Markdown 原文-----------
    /**
     * 单发 Markdown 卡片消息（msg_type=interactive，卡片内 markdown 组件渲染内容）
     *
     * @param title 卡片标题，为空时不显示标题栏
     */
    private boolean doSendMarkdownCardMessage(String appToken, String openId, String title, String content) {
        String url = SEND_MSG_URL + "?receive_id_type=open_id";
        JSONObject card = new JSONObject();
        JSONObject cardConfig = new JSONObject();
        cardConfig.put("wide_screen_mode", true);
        card.put("config", cardConfig);
        if (StringUtils.isNotEmpty(title)) {
            JSONObject titleObj = new JSONObject();
            titleObj.put("tag", "plain_text");
            titleObj.put("content", title);
            JSONObject header = new JSONObject();
            header.put("title", titleObj);
            header.put("template", "blue");
            card.put("header", header);
        }
        JSONObject markdown = new JSONObject();
        markdown.put("tag", "markdown");
        markdown.put("content", content);
        JSONArray elements = new JSONArray();
        elements.add(markdown);
        card.put("elements", elements);
        JSONObject body = new JSONObject();
        body.put("receive_id", openId);
        body.put("msg_type", "interactive");
        body.put("content", card.toJSONString());
        JSONObject result = doPost(url, appToken, body.toJSONString());
        if (result != null && result.getIntValue("code") == 0) {
            log.info("飞书卡片消息发送成功: openId={}", openId);
            return true;
        }
        log.error("飞书卡片消息发送失败: openId={}, result={}", openId, result);
        return false;
    }
    //update-end---author:scott ---date:2026-10-09  for：【敲敲云】简流 通知 对接飞书，Markdown 内容改发卡片消息，避免飞书里显示 Markdown 原文-----------

    // ==================== HTTP 工具方法（供同步/消息使用）====================

    /**
     * POST 请求（JSON body），bearerToken 为空时不携带 Authorization 头
     */
    private JSONObject doPost(String url, String bearerToken, String jsonBody) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
            if (StringUtils.isNotEmpty(bearerToken)) {
                httpPost.setHeader("Authorization", "Bearer " + bearerToken);
            }
            httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.debug("飞书 POST {} 响应: {}", url, responseBody);
                return JSON.parseObject(responseBody);
            }
        } catch (Exception e) {
            log.error("飞书 POST 请求异常: url={}", url, e);
            return null;
        }
    }

    //update-begin---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，新增公告消息发送方法，对齐钉钉sendActionCardMessage和企业微信sendTextCardMessage-----------
    /**
     * 向飞书用户发送公告消息（富文本 post 类型），对齐钉钉 sendActionCardMessage / 企业微信 sendTextCardMessage
     *
     * @param announcement  公告对象
     * @param mobileOpenUrl 移动端跳转链接（可为 null）
     * @param verifyConfig  true 时配置未就绪则静默跳过
     * @return 是否全部发送成功
     */
    public boolean sendAnnouncementMessage(SysAnnouncement announcement, String mobileOpenUrl, boolean verifyConfig) {
        //update-begin---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，改用统一配置获取方法-----------
        int tenantId = MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL
                ? oConvertUtils.getInt(TenantContext.getTenant(), 0) : 0;
        SysThirdAppConfig config = getFeishuThirdAppConfig();
        //update-end---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，改用统一配置获取方法-----------
        if (config == null) {
            if (verifyConfig) {
                log.debug("飞书应用未配置，跳过公告消息发送！");
            }
            return false;
        }
        String appToken = getTenantAccessToken(config);
        if (appToken == null) {
            log.error("飞书获取 tenant_access_token 失败，公告消息发送中止");
            return false;
        }
        // 确定摘要文本
        String summary;
        if (oConvertUtils.isNotEmpty(announcement.getMsgAbstract())) {
            String msgAbstract = announcement.getMsgAbstract().trim();
            // 如果摘要是 JSON 格式的业务扩展参数，取公告正文
            summary = (msgAbstract.startsWith("{") && msgAbstract.endsWith("}"))
                    ? announcement.getMsgContent() : msgAbstract;
        } else {
            summary = oConvertUtils.getString(announcement.getMsgContent(), "");
        }
        //update-begin---author:jeecg ---date:2026-05-18  for：【QQYUN-12767】飞书公告消息修复：两种内容构建方式，可按需选择-----------
        // -----------------------------------------------------------------------
        // 方式一（纯文本，当前注释）：去掉所有 HTML 标签后以单行纯文本发送。
        //   优点：实现简单，兼容性最好。
        //   缺点：丢失加粗、换行、超链接等富文本格式。
        //   启用方式：取消下方注释，同时注释掉"方式二"代码块。
        // -----------------------------------------------------------------------
        // String plainSummary = summary.replaceAll("<[^>]+>", "").trim();
        // JSONArray contentRows = new JSONArray();
        // JSONArray textRow = new JSONArray();
        // JSONObject textTag = new JSONObject();
        // textTag.put("tag", "text");
        // textTag.put("text", plainSummary);
        // textRow.add(textTag);
        // contentRows.add(textRow);

        // -----------------------------------------------------------------------
        // 方式二（HTML 转飞书 post 富文本，当前启用）：将 HTML 解析为飞书 post 多行标签结构。
        //   支持：<br>/<p> 换行、<b>/<strong> 加粗、<i>/<em> 斜体、<u> 下划线、<a href> 超链接。
        //   其余未识别标签剥掉标签保留文字内容。
        //   启用方式：保持下方代码不变；若切回方式一，注释掉本方式并取消方式一的注释。
        // -----------------------------------------------------------------------
        JSONArray contentRows = htmlToFeishuPostRows(summary);

        // 追加跳转链接行（两种方式共用）
        String linkUrl = oConvertUtils.isNotEmpty(mobileOpenUrl) ? mobileOpenUrl
                : oConvertUtils.getString(announcement.getOpenPage(), "");
        if (oConvertUtils.isNotEmpty(linkUrl)) {
            JSONArray linkRow = new JSONArray();
            JSONObject linkTag = new JSONObject();
            linkTag.put("tag", "a");
            linkTag.put("text", "查看详情");
            linkTag.put("href", linkUrl);
            linkRow.add(linkTag);
            contentRows.add(linkRow);
        }
        JSONObject zhContent = new JSONObject();
        zhContent.put("title", oConvertUtils.getString(announcement.getTitile(), "系统通知"));
        zhContent.put("content", contentRows);
        // 飞书 API：msg_type=post 时 content 字段结构为 {"zh_cn":{"title":"...","content":[[...]]}}
        JSONObject contentWrapper = new JSONObject();
        contentWrapper.put("zh_cn", zhContent);
        //update-end---author:jeecg ---date:2026-05-18  for：【QQYUN-12767】飞书公告消息修复：两种内容构建方式，可按需选择-----------

        // 确定接收人
        boolean isToAll = CommonConstant.MSG_TYPE_ALL.equals(announcement.getMsgType());
        List<String> openIds = new ArrayList<>();
        if (!isToAll) {
            String[] userIds = null;
            String userId = announcement.getUserIds();
            if (oConvertUtils.isNotEmpty(userId)) {
                userIds = userId.substring(0, userId.length() - 1).split(",");
            } else {
                LambdaQueryWrapper<SysAnnouncementSend> q = new LambdaQueryWrapper<>();
                q.eq(SysAnnouncementSend::getAnntId, announcement.getId());
                SysAnnouncementSend send = sysAnnouncementSendMapper.selectOne(q);
                if (send != null) {
                    userIds = new String[]{send.getUserId()};
                }
            }
            if (userIds != null) {
                LambdaQueryWrapper<SysUser> uq = new LambdaQueryWrapper<>();
                uq.in(SysUser::getId, (Object[]) userIds);
                List<SysUser> userList = userMapper.selectList(uq);
                LambdaQueryWrapper<SysThirdAccount> tq = new LambdaQueryWrapper<>();
                tq.eq(SysThirdAccount::getThirdType, THIRD_TYPE);
                tq.eq(SysThirdAccount::getTenantId, tenantId);
                tq.in(SysThirdAccount::getSysUserId,
                        userList.stream().map(SysUser::getId).collect(java.util.stream.Collectors.toList()));
                List<SysThirdAccount> accounts = sysThirdAccountService.list(tq);
                accounts.stream()
                        .map(SysThirdAccount::getThirdUserUuid)
                        .filter(oConvertUtils::isNotEmpty)
                        .forEach(openIds::add);
            }
        }
        if (isToAll) {
            log.warn("飞书暂不支持全员广播公告，跳过 toAll 发送");
            return false;
        }
        boolean allSuccess = true;
        String msgUrl = SEND_MSG_URL + "?receive_id_type=open_id";
        for (String openId : openIds) {
            JSONObject body = new JSONObject();
            body.put("receive_id", openId);
            body.put("msg_type", "post");
            body.put("content", contentWrapper.toJSONString());
            JSONObject result = doPost(msgUrl, appToken, body.toJSONString());
            if (result == null || result.getIntValue("code") != 0) {
                log.error("飞书公告消息发送失败: openId={}, result={}", openId, result);
                allSuccess = false;
            } else {
                log.info("飞书公告消息发送成功: openId={}", openId);
            }
        }
        return allSuccess;
    }
    //update-end---author:jeecg ---date:2026-05-15  for：【QQYUN-12767】飞书集成，新增公告消息发送方法，对齐钉钉sendActionCardMessage和企业微信sendTextCardMessage-----------

    /**
     * GET 请求，bearerToken 为空时不携带 Authorization 头
     */
    private JSONObject doGet(String url, String bearerToken) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/json; charset=utf-8");
            if (StringUtils.isNotEmpty(bearerToken)) {
                httpGet.setHeader("Authorization", "Bearer " + bearerToken);
            }
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.debug("飞书 GET {} 响应: {}", url, responseBody);
                return JSON.parseObject(responseBody);
            }
        } catch (Exception e) {
            log.error("飞书 GET 请求异常: url={}", url, e);
            return null;
        }
    }
}
//update-end---author:jeecg ---date:2026-05-13  for：【QQYUN-12767】飞书集成-----------
