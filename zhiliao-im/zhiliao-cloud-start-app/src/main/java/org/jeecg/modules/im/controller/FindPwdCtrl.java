package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.base.util.ToolString;
import org.jeecg.modules.im.configuration.ClientOperLog;
import org.jeecg.modules.im.base.util.TCaptchaVerify;
import org.jeecg.modules.im.entity.Param;
import org.jeecg.modules.im.entity.VerifyCode;
import org.jeecg.modules.im.service.ParamService;
import org.jeecg.modules.im.service.VerifyCodeService;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * 找回密码
 */
@RestController
@RequestMapping("/a/findPwd")
@Slf4j
public class FindPwdCtrl extends BaseApiCtrl {

    @Resource
    private UserService userService;
    @Resource
    private ParamService paramService;
    @Resource
    private VerifyCodeService verifyCodeService;


    /**
     * 发送短信验证码
     */
    @NoNeedUserToken
    @PostMapping("/sendSmsCode")
    @ClientOperLog(module = "找回密码", type = "发送短信验证码", desc = "")
    public Result<Object> sendSmsCode(@RequestParam String mobile, String ticket, String randstr) {
        if (!ToolString.regExpValiMobile(mobile)) {
            return fail("手机号格式不正确");
        }
        if ("1".equals(paramService.getByName(Param.Name.tencent_captcha_on))) {
            //腾讯防水墙校验
            int checkCode = TCaptchaVerify.verifyTicket(paramService.getByName(Param.Name.tencent_captcha_app_id), paramService.getByName(Param.Name.tencent_captcha_app_secret), ticket, randstr, getIp());
            if (checkCode == -1) {
                return fail("安全验证失败");
            }
        }
        if(userService.findByMobile(mobile)==null){
            return fail("该手机号未注册");
        }
        // 二次验证成功
        return verifyCodeService.sendByMobile(mobile, VerifyCode.Type.FindPwd.name());
    }

    /**
     * 校验验证码
     */
    @NoNeedUserToken
    @PostMapping("/checkSmsCode")
    @ClientOperLog(module = "找回密码", type = "校验验证码", desc = "")
    public Result<Object> checkSmsCode(@RequestParam String mobile, @RequestParam String verifyCode) {
        if (!ToolString.regExpValiMobile( mobile)) {
            return fail("手机号格式不正确");
        }
        if(userService.findByMobile(mobile)==null){
            return fail("该手机号未注册");
        }
        return verifyCodeService.verifyByMobile(mobile, verifyCode,VerifyCode.Type.FindPwd.name());
    }

    /**
     * 通过手机号重置密码
     */
    @NoNeedUserToken
    @PostMapping("/resetByMobile")
    @ClientOperLog(module = "重置密码", type = "手机号", desc = "")
    public Result<Object> mobile(@RequestParam String mobile, @RequestParam String password, @RequestParam String verifyCode) throws UnsupportedEncodingException {
        if (!ToolString.regExpValiMobile(mobile)) {
            return fail("手机号格式不正确");
        }
        if (!ToolString.valiPasswordQQ(password)) {
            return fail("密码由6~20位数字、大小写字母或符号组成，至少含有2种以上字符");
        }
        return userService.resetPwdByMobile(mobile,password, verifyCode);
    }
    /**
     * 通过密保问题重置密码
     */
    @NoNeedUserToken
    @PostMapping("/resetBySecretQuestion")
    @ClientOperLog(module = "重置密码", type = "密保问题", desc = "")
    public Result<Object> secretQuestion(@RequestParam String account, @RequestParam String password, @RequestParam String questions) {
        if (!ToolString.valiPasswordQQ(password)) {
            return fail("密码由6~20位数字、大小写字母或符号组成，至少含有2种以上字符");
        }
        return userService.resetPwdBySecretQuestion(account,password, questions);
    }
}
