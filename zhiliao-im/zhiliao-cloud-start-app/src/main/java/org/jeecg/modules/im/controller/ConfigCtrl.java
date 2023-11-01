package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.entity.ClientConfig;
import org.jeecg.modules.im.entity.Customer;
import org.jeecg.modules.im.entity.Param;
import org.jeecg.modules.im.entity.SysConfig;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 配置
 */
@RestController
@RequestMapping("/a/config")
public class ConfigCtrl extends BaseApiCtrl {

    @Resource
    private ClientVerService clientVerService;
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private ClientConfigService clientConfigService;
    @Resource
    private ParamService paramService;
    @Resource
    private CustomerService customerService;
    @Resource
    private UserService userService;
    @Resource
    private LinkService linkService;

    @NoNeedUserToken
    @RequestMapping("/checkServer")
    public Result<Object> checkServer(@RequestParam String serverId) {
        Customer customer = customerService.findByServerId(serverId);
        if(customer==null){
            return fail();
        }
        return success(customer.getApiUrl());
    }

    @NoNeedUserToken
    @RequestMapping("/server")
    public Result<Object> server() {
        SysConfig sysConfig = sysConfigService.get();
        ClientConfig clientConfig = clientConfigService.get();
        Kv config = Kv.create();
        //app版本
        config.set("versions", clientVerService.findLatestOfAll())
        //腾讯防水墙
        .set("tencentCaptcha",
                Kv.by("on",paramService.getByName(Param.Name.tencent_captcha_on))
                    .set("appId",paramService.getByName(Param.Name.tencent_captcha_app_id))
                    .set("appSecret",paramService.getByName(Param.Name.tencent_captcha_app_secret)))
        //文件上传地址
        .set("uploadUrl",sysConfig.getUploadUrl())
        .set("maintenance",sysConfig.getMaintenance())
        .set("xmppDomain",sysConfig.getXmppDomain())
        .set("xmppHost",sysConfig.getXmppHost())
        .set("xmppPort",sysConfig.getXmppPort())
        .set("enableVideoCall",sysConfig.getEnableVideoCall())
        //agora
        .set("agoraAppId",sysConfig.getAgoraAppId())
        //服务器时间戳
        .set("serverTimestamp",getTs())
        .set("client",clientConfig)
        //发现页链接
        .set("links",linkService.list())
        ;
        return success(config);
    }
}
