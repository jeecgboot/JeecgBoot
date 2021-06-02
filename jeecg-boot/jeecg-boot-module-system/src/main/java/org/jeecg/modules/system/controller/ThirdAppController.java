package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.jeecg.dingtalk.api.core.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.config.thirdapp.ThirdAppConfig;
import org.jeecg.modules.system.service.impl.ThirdAppDingtalkServiceImpl;
import org.jeecg.modules.system.service.impl.ThirdAppWechatEnterpriseServiceImpl;
import org.jeecg.modules.system.vo.thirdapp.SyncInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 第三方App对接
 */
@Slf4j
@RestController("thirdAppController")
@RequestMapping("/sys/thirdApp")
public class ThirdAppController {

    @Autowired
    ThirdAppConfig thirdAppConfig;

    @Autowired
    ThirdAppWechatEnterpriseServiceImpl wechatEnterpriseService;
    @Autowired
    ThirdAppDingtalkServiceImpl dingtalkService;

    /**
     * 获取启用的系统
     */
    @GetMapping("/getEnabledType")
    public Result getEnabledType() {
        Map<String, Boolean> enabledMap = new HashMap<>();
        enabledMap.put("wechatEnterprise", thirdAppConfig.isWechatEnterpriseEnabled());
        enabledMap.put("dingtalk", thirdAppConfig.isDingtalkEnabled());
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
        if (thirdAppConfig.isWechatEnterpriseEnabled()) {
            SyncInfoVo syncInfo = wechatEnterpriseService.syncLocalUserToThirdApp(ids);
            if (syncInfo.getFailInfo().size() == 0) {
                return Result.OK("同步成功", syncInfo);
            } else {
                return Result.error("同步失败", syncInfo);
            }
        }
        return Result.error("企业微信同步功能已禁用");
    }

    /**
     * 同步【企业微信】[用户]到本地
     *
     * @param ids 作废
     * @return
     */
    @GetMapping("/sync/wechatEnterprise/user/toLocal")
    public Result syncWechatEnterpriseUserToLocal(@RequestParam(value = "ids", required = false) String ids) {
        if (thirdAppConfig.isWechatEnterpriseEnabled()) {
            SyncInfoVo syncInfo = wechatEnterpriseService.syncThirdAppUserToLocal();
            if (syncInfo.getFailInfo().size() == 0) {
                return Result.OK("同步成功", syncInfo);
            } else {
                return Result.error("同步失败", syncInfo);
            }
        }
        return Result.error("企业微信同步功能已禁用");
    }

    /**
     * 同步本地[部门]到【企业微信】
     *
     * @param ids
     * @return
     */
    @GetMapping("/sync/wechatEnterprise/depart/toApp")
    public Result syncWechatEnterpriseDepartToApp(@RequestParam(value = "ids", required = false) String ids) {
        if (thirdAppConfig.isWechatEnterpriseEnabled()) {
            boolean flag = wechatEnterpriseService.syncLocalDepartmentToThirdApp(ids);
            return flag ? Result.OK("同步成功", null) : Result.error("同步失败");
        }
        return Result.error("企业微信同步功能已禁用");
    }

    /**
     * 同步【企业微信】[部门]到本地
     *
     * @param ids
     * @return
     */
    @GetMapping("/sync/wechatEnterprise/depart/toLocal")
    public Result syncWechatEnterpriseDepartToLocal(@RequestParam(value = "ids", required = false) String ids) {
        if (thirdAppConfig.isWechatEnterpriseEnabled()) {
            SyncInfoVo syncInfo = wechatEnterpriseService.syncThirdAppDepartmentToLocal(ids);
            if (syncInfo.getFailInfo().size() == 0) {
                return Result.OK("同步成功", syncInfo);
            } else {
                return Result.error("同步失败", syncInfo);
            }
        }
        return Result.error("企业微信同步功能已禁用");
    }

    /**
     * 同步本地[部门]到【钉钉】
     *
     * @param ids
     * @return
     */
    @GetMapping("/sync/dingtalk/depart/toApp")
    public Result syncDingtalkDepartToApp(@RequestParam(value = "ids", required = false) String ids) {
        if (thirdAppConfig.isDingtalkEnabled()) {
            boolean flag = dingtalkService.syncLocalDepartmentToThirdApp(ids);
            return flag ? Result.OK("同步成功", null) : Result.error("同步失败");
        }
        return Result.error("钉钉同步功能已禁用");
    }

    /**
     * 同步【钉钉】[部门]到本地
     *
     * @param ids
     * @return
     */
    @GetMapping("/sync/dingtalk/depart/toLocal")
    public Result syncDingtalkDepartToLocal(@RequestParam(value = "ids", required = false) String ids) {
        if (thirdAppConfig.isDingtalkEnabled()) {
            SyncInfoVo syncInfo = dingtalkService.syncThirdAppDepartmentToLocal(ids);
            if (syncInfo.getFailInfo().size() == 0) {
                return Result.OK("同步成功", syncInfo);
            } else {
                return Result.error("同步失败", syncInfo);
            }
        }
        return Result.error("钉钉同步功能已禁用");
    }

    /**
     * 同步本地[用户]到【钉钉】
     *
     * @param ids
     * @return
     */
    @GetMapping("/sync/dingtalk/user/toApp")
    public Result syncDingtalkUserToApp(@RequestParam(value = "ids", required = false) String ids) {
        if (thirdAppConfig.isDingtalkEnabled()) {
            SyncInfoVo syncInfo = dingtalkService.syncLocalUserToThirdApp(ids);
            if (syncInfo.getFailInfo().size() == 0) {
                return Result.OK("同步成功", syncInfo);
            } else {
                return Result.error("同步失败", syncInfo);
            }
        }
        return Result.error("钉钉同步功能已禁用");
    }

    /**
     * 同步【钉钉】[用户]到本地
     *
     * @param ids 作废
     * @return
     */
    @GetMapping("/sync/dingtalk/user/toLocal")
    public Result syncDingtalkUserToLocal(@RequestParam(value = "ids", required = false) String ids) {
        if (thirdAppConfig.isDingtalkEnabled()) {
            SyncInfoVo syncInfo = dingtalkService.syncThirdAppUserToLocal();
            if (syncInfo.getFailInfo().size() == 0) {
                return Result.OK("同步成功", syncInfo);
            } else {
                return Result.error("同步失败", syncInfo);
            }
        }
        return Result.error("钉钉同步功能已禁用");
    }

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

        String fromUser = JwtUtil.getUserNameByToken(request);
        String title = "第三方APP消息测试";
        MessageDTO message = new MessageDTO(fromUser, receiver, title, content);
        message.setToAll(sendAll);

        if (ThirdAppConfig.WECHAT_ENTERPRISE.equals(app)) {
            if (thirdAppConfig.isWechatEnterpriseEnabled()) {
                JSONObject response = wechatEnterpriseService.sendMessageResponse(message, false);
                return Result.OK(response);
            }
            return Result.error("企业微信已被禁用");
        } else if (ThirdAppConfig.DINGTALK.equals(app)) {
            if (thirdAppConfig.isDingtalkEnabled()) {
                Response<String> response = dingtalkService.sendMessageResponse(message, false);
                return Result.OK(response);
            }
            return Result.error("钉钉已被禁用");
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
        String msg_task_id = params.getString("msg_task_id");

        if (ThirdAppConfig.WECHAT_ENTERPRISE.equals(app)) {
            if (thirdAppConfig.isWechatEnterpriseEnabled()) {
                return Result.error("企业微信不支持撤回消息");
            }
            return Result.error("企业微信已被禁用");
        } else if (ThirdAppConfig.DINGTALK.equals(app)) {
            if (thirdAppConfig.isDingtalkEnabled()) {
                Response<JSONObject> response = dingtalkService.recallMessageResponse(msg_task_id);
                if (response.isSuccess()) {
                    return Result.OK("撤回成功", response);
                } else {
                    return Result.error("撤回失败：" + response.getErrcode() + "——" + response.getErrmsg(), response);
                }
            }
            return Result.error("钉钉已被禁用");
        }
        return Result.error("不识别的第三方APP");
    }

}
