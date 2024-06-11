package org.jeecg.modules.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeecg.dingtalk.api.core.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.constant.enums.MessageTypeEnum;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.system.entity.SysThirdAccount;
import org.jeecg.modules.system.entity.SysThirdAppConfig;
import org.jeecg.modules.system.service.ISysThirdAccountService;
import org.jeecg.modules.system.service.ISysThirdAppConfigService;
import org.jeecg.modules.system.service.impl.ThirdAppDingtalkServiceImpl;
import org.jeecg.modules.system.service.impl.ThirdAppWechatEnterpriseServiceImpl;
import org.jeecg.modules.system.vo.thirdapp.JwSysUserDepartVo;
import org.jeecg.modules.system.vo.thirdapp.JwUserDepartVo;
import org.jeecg.modules.system.vo.thirdapp.SyncInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 第三方App对接
 * @author: jeecg-boot
 */
@Slf4j
@RestController("thirdAppController")
@RequestMapping("/sys/thirdApp")
public class ThirdAppController {

    @Autowired
    ThirdAppWechatEnterpriseServiceImpl wechatEnterpriseService;
    @Autowired
    ThirdAppDingtalkServiceImpl dingtalkService;

    @Autowired
    private ISysThirdAppConfigService appConfigService;

    @Autowired
    private ISysThirdAccountService sysThirdAccountService;

    /**
     * 获取启用的系统
     */
    @GetMapping("/getEnabledType")
    public Result getEnabledType() {
        Map<String, Boolean> enabledMap = new HashMap(5);
        int tenantId;
        //是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            tenantId = oConvertUtils.getInt(TenantContext.getTenant(), -1);
        } else {
            tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
        }
        //查询当前租户下的第三方配置
        List<SysThirdAppConfig> list = appConfigService.getThirdConfigListByThirdType(tenantId);
        //钉钉是否已配置
        boolean dingConfig = false;
        //企业微信是否已配置
        boolean qywxConfig = false;
        if(null != list && list.size()>0){
            for (SysThirdAppConfig config:list) {
                if(MessageTypeEnum.DD.getType().equals(config.getThirdType())){
                    dingConfig = true;
                    continue;
                }
                if(MessageTypeEnum.QYWX.getType().equals(config.getThirdType())){
                    qywxConfig = true;
                    continue;
                }
            }
        }
        enabledMap.put("wechatEnterprise", qywxConfig);
        enabledMap.put("dingtalk", dingConfig);
        //update-end---author:wangshuai ---date:20230224  for：[QQYUN-3440]通过租户模式隔离------------
        return Result.OK(enabledMap);
    }

    /**
     * 同步本地[用户]到【企业微信】
     *
     * @param ids
     * @return
     */
    @GetMapping("/sync/wechatEnterprise/user/toApp")
    public Result syncWechatEnterpriseUserToApp(@RequestParam(value = "ids", required = false) String ids) {
        //update-begin---author:wangshuai ---date:20230224  for：[QQYUN-3440]通过租户模式隔离 ------------
        //获取企业微信配置
        Integer tenantId = oConvertUtils.getInt(TenantContext.getTenant(),0);
        SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(tenantId, MessageTypeEnum.QYWX.getType());
        if (null != config) {
        //update-begin---author:wangshuai ---date:20230224  for：[QQYUN-3440]通过租户模式隔离 ------------
            SyncInfoVo syncInfo = wechatEnterpriseService.syncLocalUserToThirdApp(ids);
            if (syncInfo.getFailInfo().size() == 0) {
                return Result.OK("同步成功", syncInfo);
            } else {
                return Result.error("同步失败", syncInfo);
            }
        }
        return Result.error("企业微信尚未配置,请配置企业微信");
    }

    /**
     * 同步【企业微信】[用户]到本地
     *
     * @param ids 作废
     * @return
     */
    @GetMapping("/sync/wechatEnterprise/user/toLocal")
    public Result syncWechatEnterpriseUserToLocal(@RequestParam(value = "ids", required = false) String ids) {
        return Result.error("由于企业微信接口调整，同步到本地功能已失效");

//        if (thirdAppConfig.isWechatEnterpriseEnabled()) {
//            SyncInfoVo syncInfo = wechatEnterpriseService.syncThirdAppUserToLocal();
//            if (syncInfo.getFailInfo().size() == 0) {
//                return Result.OK("同步成功", syncInfo);
//            } else {
//                return Result.error("同步失败", syncInfo);
//            }
//        }
//        return Result.error("企业微信同步功能已禁用");
    }

    /**
     * 同步本地[部门]到【企业微信】
     *
     * @param ids
     * @return
     */
    @GetMapping("/sync/wechatEnterprise/depart/toApp")
    public Result syncWechatEnterpriseDepartToApp(@RequestParam(value = "ids", required = false) String ids) {
        //获取企业微信配置
        Integer tenantId = oConvertUtils.getInt(TenantContext.getTenant(),0);
        SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(tenantId, MessageTypeEnum.QYWX.getType());
        if (null != config) {
            SyncInfoVo syncInfo = wechatEnterpriseService.syncLocalDepartmentToThirdApp(ids);
            if (syncInfo.getFailInfo().size() == 0) {
                return Result.OK("同步成功", null);
            } else {
                return Result.error("同步失败", syncInfo);
            }
        }
        return Result.error("企业微信尚未配置,请配置企业微信");
    }

    /**
     * 同步【企业微信】[部门]到本地
     *
     * @param ids
     * @return
     */
    @GetMapping("/sync/wechatEnterprise/depart/toLocal")
    public Result syncWechatEnterpriseDepartToLocal(@RequestParam(value = "ids", required = false) String ids) {
        return Result.error("由于企业微信接口调整，企业微信同步本地部门失效");
//        //获取企业微信配置
//        Integer tenantId = oConvertUtils.getInt(TenantContext.getTenant(),0);
//        SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(tenantId, MessageTypeEnum.QYWX.getType());
//        if (null != config) {
//            SyncInfoVo syncInfo = wechatEnterpriseService.syncThirdAppDepartmentToLocal(ids);
//            if (syncInfo.getFailInfo().size() == 0) {
//                return Result.OK("同步成功", syncInfo);
//            } else {
//                return Result.error("同步失败", syncInfo);
//            }
//        }
//        return Result.error("企业微信尚未配置,请配置企业微信");
    }

    /**
     * 同步本地[部门]到【钉钉】
     *
     * @param ids
     * @return
     */
    @GetMapping("/sync/dingtalk/depart/toApp")
    public Result syncDingtalkDepartToApp(@RequestParam(value = "ids", required = false) String ids) {
        //获取钉钉配置
        Integer tenantId = oConvertUtils.getInt(TenantContext.getTenant(),0);
        SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(tenantId, MessageTypeEnum.DD.getType());
        if (null != config) {
            SyncInfoVo syncInfo = dingtalkService.syncLocalDepartmentToThirdApp(ids);
            if (syncInfo.getFailInfo().size() == 0) {
                return Result.OK("同步成功", null);
            } else {
                return Result.error("同步失败", syncInfo);
            }
        }
        return Result.error("钉钉尚未配置,请配置钉钉");
    }

//    /**
//     * 同步【钉钉】[部门]到本地
//     *
//     * @param ids
//     * @return
//     */
//   @GetMapping("/sync/dingtalk/depart/toLocal")
//    public Result syncDingtalkDepartToLocal(@RequestParam(value = "ids", required = false) String ids) {
//        //获取钉钉配置
//        Integer tenantId = oConvertUtils.getInt(TenantContext.getTenant(),0);
//        SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(tenantId, MessageTypeEnum.DD.getType());
//        if (null!= config) {
//            SyncInfoVo syncInfo = dingtalkService.syncThirdAppDepartmentToLocal(ids);
//            if (syncInfo.getFailInfo().size() == 0) {
//                return Result.OK("同步成功", syncInfo);
//            } else {
//                return Result.error("同步失败", syncInfo);
//            }
//        }
//        return Result.error("钉钉尚未配置,请配置钉钉");
//    }

    /**
     * 同步本地[用户]到【钉钉】
     *
     * @param ids
     * @return
     */
    @GetMapping("/sync/dingtalk/user/toApp")
    public Result syncDingtalkUserToApp(@RequestParam(value = "ids", required = false) String ids) {
        //获取钉钉配置
        int tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
        //根据租户id和第三方类别获取租户数据
        SysThirdAppConfig appConfig = appConfigService.getThirdConfigByThirdType(tenantId,MessageTypeEnum.DD.getType());
        if(null != appConfig){
            SyncInfoVo syncInfo = dingtalkService.syncLocalUserToThirdApp(ids);
            if (syncInfo.getFailInfo().size() == 0) {
                return Result.OK("同步成功", syncInfo);
            } else {
                return Result.error("同步失败", syncInfo);
            }
        }
        return Result.error("钉钉尚未配置,请配置钉钉");
    }

//    /**
//     * 同步【钉钉】[用户]到本地
//     *
//     * @param ids 作废
//     * @return
//     */
//    @GetMapping("/sync/dingtalk/user/toLocal")
//    public Result syncDingtalkUserToLocal(@RequestParam(value = "ids", required = false) String ids) {
//        //获取钉钉配置
//        Integer tenantId = oConvertUtils.getInt(TenantContext.getTenant(),0);
//        SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(tenantId, MessageTypeEnum.DD.getType());
//        if (null != config) {
//            SyncInfoVo syncInfo = dingtalkService.syncThirdAppUserToLocal();
//            if (syncInfo.getFailInfo().size() == 0) {
//                return Result.OK("同步成功", syncInfo);
//            } else {
//                return Result.error("同步失败", syncInfo);
//            }
//        }
//        return Result.error("钉钉尚未配置,请配置钉钉");
//    }

    /**
     * 发送消息测试
     *
     * @return
     */
    @PostMapping("/sendMessageTest")
    public Result sendMessageTest(@RequestBody JSONObject params, HttpServletRequest request) {
        /* 获取前台传递的参数 */
        // 第三方app的类型
        String app = params.getString("app");
        // 是否发送给全部人
        boolean sendAll = params.getBooleanValue("sendAll");
        // 消息接收者，传sys_user表的username字段，多个用逗号分割
        String receiver = params.getString("receiver");
        // 消息内容
        String content = params.getString("content");
        // 租户id
        int tenantId = oConvertUtils.getInt(TenantContext.getTenant(),0);

        String fromUser = JwtUtil.getUserNameByToken(request);
        String title = "第三方APP消息测试";
        MessageDTO message = new MessageDTO(fromUser, receiver, title, content);
        message.setToAll(sendAll);
        //update-begin---author:wangshuai ---date:20230224  for：[QQYUN-3440]钉钉、企业微信通过租户模式隔离 ------------
        String weChatType = MessageTypeEnum.QYWX.getType();
        String dingType = MessageTypeEnum.DD.getType();
        if (weChatType.toUpperCase().equals(app)) {
            SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(tenantId, weChatType);
            if (null != config) {
        //update-end---author:wangshuai ---date:20230224  for：[QQYUN-3440]钉钉、企业微信通过租户模式隔离 ------------
                JSONObject response = wechatEnterpriseService.sendMessageResponse(message, false);
                return Result.OK(response);
            }
            return Result.error("企业微信尚未配置,请配置企业微信");
            //update-begin---author:wangshuai ---date:20230224  for：[QQYUN-3440]钉钉、企业微信通过租户模式隔离 ------------
        } else if (dingType.toUpperCase().equals(app)) {
            SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(tenantId, dingType);
            if (null != config) {
            //update-end---author:wangshuai ---date:20230224  for：[QQYUN-3440]钉钉、企业微信通过租户模式隔离 ------------
                Response<String> response = dingtalkService.sendMessageResponse(message, false);
                return Result.OK(response);
            }
            return Result.error("钉钉尚未配置,请配置钉钉");
        }
        return Result.error("不识别的第三方APP");
    }

    /**
     * 撤回消息测试
     *
     * @return
     */
    @PostMapping("/recallMessageTest")
    public Result recallMessageTest(@RequestBody JSONObject params) {
        /* 获取前台传递的参数 */
        // 第三方app的类型
        String app = params.getString("app");
        // 消息id
        String msgTaskId = params.getString("msg_task_id");
        //租户id
        int tenantId = oConvertUtils.getInt(TenantContext.getTenant(),0);
        if (CommonConstant.WECHAT_ENTERPRISE.equals(app)) {
            SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(tenantId, MessageTypeEnum.QYWX.getType());
            if (null != config) {
                return Result.error("企业微信不支持撤回消息");
            }
            return Result.error("企业微信尚未配置,请配置企业微信");
        } else if (CommonConstant.DINGTALK.equals(app)) {
            SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(tenantId, MessageTypeEnum.DD.getType());
            if (null != config) {
                Response<JSONObject> response = dingtalkService.recallMessageResponse(msgTaskId);
                if (response.isSuccess()) {
                    return Result.OK("撤回成功", response);
                } else {
                    return Result.error("撤回失败：" + response.getErrcode() + "——" + response.getErrmsg(), response);
                }
            }
            return Result.error("钉钉尚未配置,请配置钉钉");
        }
        return Result.error("不识别的第三方APP");
    }

    //========================begin 应用低代码钉钉/企业微信同步用户部门专用 =============================
    /**
     * 添加第三方app配置
     *
     * @param appConfig
     * @return
     */
    @RequestMapping(value = "/addThirdAppConfig", method = RequestMethod.POST)
    public Result<String> addThirdAppConfig(@RequestBody SysThirdAppConfig appConfig) {
        Result<String> result = new Result<>();
        //根据当前登录租户id和第三方类别判断是否已经创建
        Integer tenantId = oConvertUtils.isNotEmpty(appConfig.getTenantId()) ? appConfig.getTenantId() : oConvertUtils.getInt(TenantContext.getTenant(), 0);
        SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(tenantId, appConfig.getThirdType());
        if (null != config) {
            result.error500("操作失败,同一个租户下只允许绑定一个钉钉或者企业微信");
            return result;
        }
        String clientId = appConfig.getClientId();
        //通过应用key获取第三方配置
        List<SysThirdAppConfig> thirdAppConfigByClientId = appConfigService.getThirdAppConfigByClientId(clientId);
        if(CollectionUtil.isNotEmpty(thirdAppConfigByClientId)){
            result.error500("AppKey已存在，请勿重复添加");
            return result;
        }
        try {
            appConfig.setTenantId(oConvertUtils.getInt(TenantContext.getTenant(),0));
            appConfigService.save(appConfig);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑第三方app配置
     *
     * @param appConfig
     * @return
     */
    @RequestMapping(value = "/editThirdAppConfig", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> editThirdAppConfig(@RequestBody SysThirdAppConfig appConfig) {
        Result<String> result = new Result<>();
        SysThirdAppConfig config = appConfigService.getById(appConfig.getId());
        if (null == config) {
            result.error500("数据不存在");
            return result;
        }
        String clientId = appConfig.getClientId();
        //如果编辑的应用key,和数据库中的不一致，需要判断应用key是否已存在
        if(!clientId.equals(config.getClientId())){
            //通过应用key获取第三方配置
            List<SysThirdAppConfig> thirdAppConfigByClientId = appConfigService.getThirdAppConfigByClientId(clientId);
            if(CollectionUtil.isNotEmpty(thirdAppConfigByClientId)){
                result.error500("AppKey已存在，请勿重复添加");
                return result;
            }
        }
        try {
            appConfigService.updateById(appConfig);
            result.success("修改成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 根据租户id和第三方类型获取第三方app配置信息
     *
     * @param tenantId
     * @param thirdType
     * @return
     */
    @GetMapping("/getThirdConfigByTenantId")
    public Result<SysThirdAppConfig> getThirdAppByTenantId(@RequestParam(name = "tenantId", required = false) Integer tenantId,
                                                           @RequestParam(name = "thirdType") String thirdType) {
        if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL) {
            if (tenantId == null) {
                return Result.error("开启多租户模式，租户ID参数不允许为空！");
            }
        } else {
            //租户未传递，则采用平台的
            if (tenantId == null) {
                tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
            }
        }
        Result<SysThirdAppConfig> result = new Result<>();
        LambdaQueryWrapper<SysThirdAppConfig> query = new LambdaQueryWrapper<>();
        query.eq(SysThirdAppConfig::getThirdType,thirdType);
        query.eq(SysThirdAppConfig::getTenantId,tenantId);
        SysThirdAppConfig sysThirdAppConfig = appConfigService.getOne(query);
        result.setSuccess(true);
        result.setResult(sysThirdAppConfig);
        return result;
    }

    /**
     * 同步【钉钉】[部门和用户]到本地
     *
     * @param ids
     * @return
     */
    @GetMapping("/sync/dingtalk/departAndUser/toLocal")
    public Result syncDingTalkDepartAndUserToLocal(@RequestParam(value = "ids", required = false) String ids) {
        Integer tenantId = oConvertUtils.getInt(TenantContext.getTenant(), 0);
        SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(tenantId, MessageTypeEnum.DD.getType());
        if (null != config) {
            SyncInfoVo syncInfo = dingtalkService.syncThirdAppDepartmentUserToLocal();
            if (syncInfo.getFailInfo().size() == 0) {
                return Result.OK("同步成功", syncInfo);
            } else {
                return Result.error("同步失败", syncInfo);
            }
        }
        return Result.error("钉钉尚未配置,请配置钉钉");
    }
    //========================end 应用低代码钉钉/企业微信同步用户部门专用 ========================


    //========================begin 应用低代码账号设置第三方账号绑定 ================================
    /**
     * 获取第三方账号
     * @param thirdType
     * @return
     */
    @GetMapping("/getThirdAccountByUserId")
    public Result<List<SysThirdAccount>> getThirdAccountByUserId(@RequestParam(name="thirdType") String thirdType){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
        //根据id查询
        query.eq(SysThirdAccount::getSysUserId,sysUser.getId());
        //扫码登录只有租户为0
        query.eq(SysThirdAccount::getTenantId,CommonConstant.TENANT_ID_DEFAULT_VALUE);
        //根据第三方类别查询
        if(oConvertUtils.isNotEmpty(thirdType)){
            query.in(SysThirdAccount::getThirdType, Arrays.asList(thirdType.split(SymbolConstant.COMMA)));
        }
        List<SysThirdAccount> list = sysThirdAccountService.list(query);
        return Result.ok(list);
    }

    /**
     * 绑定第三方账号
     * @return
     */
    @PostMapping("/bindThirdAppAccount")
    public Result<SysThirdAccount> bindThirdAppAccount(@RequestBody SysThirdAccount sysThirdAccount){
        SysThirdAccount thirdAccount = sysThirdAccountService.bindThirdAppAccountByUserId(sysThirdAccount);
        return Result.ok(thirdAccount);
    }

    /**
     * 删除第三方用户信息
     * @param sysThirdAccount
     * @return
     */
    @DeleteMapping("/deleteThirdAccount")
    public Result<String> deleteThirdAccountById(@RequestBody SysThirdAccount sysThirdAccount){
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if(!sysUser.getId().equals(sysThirdAccount.getSysUserId())){
            return Result.error("无权修改他人信息");
        }
        SysThirdAccount thirdAccount = sysThirdAccountService.getById(sysThirdAccount.getId());
        if(null == thirdAccount){
            return Result.error("未找到改第三方账户信息");
        }
        sysThirdAccountService.removeById(thirdAccount.getId());
        return Result.ok("解绑成功");
    }
    //========================end 应用低代码账号设置第三方账号绑定 ================================

    /**
     * 获取企业微信绑定的用户信息
     * @param request
     * @return
     */
    @GetMapping("/getThirdUserByWechat")
    public Result<JwSysUserDepartVo> getThirdUserByWechat(HttpServletRequest request){
        //获取企业微信配置
        Integer tenantId = oConvertUtils.getInt(TokenUtils.getTenantIdByRequest(request),0);
        SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(tenantId, MessageTypeEnum.QYWX.getType());
        if (null != config) {
            JwSysUserDepartVo list = wechatEnterpriseService.getThirdUserByWechat(tenantId);
            return Result.ok(list);
        }
        return Result.error("企业微信尚未配置,请配置企业微信"); 
    }

    /**
     * 同步企业微信部门和用户到本地
     * @param jwUserDepartJson
     * @param request
     * @return
     */
    @GetMapping("/sync/wechatEnterprise/departAndUser/toLocal")
    public Result<SyncInfoVo> syncWechatEnterpriseDepartAndUserToLocal(@RequestParam(name = "jwUserDepartJson") String jwUserDepartJson,HttpServletRequest request){
        int tenantId = oConvertUtils.getInt(TokenUtils.getTenantIdByRequest(request), 0);
        SyncInfoVo syncInfoVo = wechatEnterpriseService.syncWechatEnterpriseDepartAndUserToLocal(jwUserDepartJson,tenantId);
        return Result.ok(syncInfoVo);
    }

    /**
     * 查询被绑定的企业微信用户
     * @param request
     * @return
     */
    @GetMapping("/getThirdUserBindByWechat")
    public Result<List<JwUserDepartVo>> getThirdUserBindByWechat(HttpServletRequest request){
        int tenantId = oConvertUtils.getInt(TokenUtils.getTenantIdByRequest(request), 0);
        List<JwUserDepartVo> jwSysUserDepartVos = wechatEnterpriseService.getThirdUserBindByWechat(tenantId);
        return Result.ok(jwSysUserDepartVos);
    }
}
