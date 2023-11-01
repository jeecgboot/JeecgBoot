package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.base.util.ToolString;
import org.jeecg.modules.im.configuration.ClientOperLog;
import org.jeecg.modules.im.base.util.TCaptchaVerify;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.entity.query_helper.QUser;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户注册
 */
@RestController
@RequestMapping("/a/reg")
@Slf4j
public class RegisterCtrl extends BaseApiCtrl {

    @Resource
    private UserService userService;
    @Resource
    private ParamService paramService;
    @Resource
    private VerifyCodeService verifyCodeService;
    @Resource
    private ClientConfigService clientConfigService;
    @Resource
    private InviteCodeService inviteCodeService;
    @Resource
    private LoginLogService loginLogService;


    /**
     * 发送短信验证码
     */
    @NoNeedUserToken
    @RequestMapping("/sendSmsCode")
    @ClientOperLog(module = "用户注册", type = "发送短信验证码", desc = "手机注册")
    public Result<Object> sendSmsCode(@RequestParam String mobile,String inviteCode, String ticket, String randstr) {
        if(!ToolString.regExpValiMobile(mobile)){
            return fail("手机号格式不正确");
        }
        if ("1".equals(paramService.getByName(Param.Name.tencent_captcha_on))) {
            //腾讯防水墙校验
            int checkCode = TCaptchaVerify.verifyTicket(paramService.getByName(Param.Name.tencent_captcha_app_id), paramService.getByName(Param.Name.tencent_captcha_app_secret), ticket, randstr, getIp());
            if (checkCode == -1) {
                return fail("安全验证失败");
            }
        }
        //检测注册频率
        Result limitCheckResult = checkLimit();
        if(!limitCheckResult.isSuccess()){
            return limitCheckResult;
        }
        if(userService.findByMobile(mobile)!=null){
            return fail("该手机号已被注册");
        }
        Result<Object> inviteCodeCheckResult = inviteCodeService.checkCode(inviteCode);
        if(!inviteCodeCheckResult.isSuccess()){
            return inviteCodeCheckResult;
        }
        // 二次验证成功
        return verifyCodeService.sendByMobile(mobile, VerifyCode.Type.Register.name());
    }

    /**
     * 校验验证码
     */
    @NoNeedUserToken
    @RequestMapping("/checkSmsCode")
    @ClientOperLog(module = "用户注册", type = "校验验证码", desc = "手机注校验册验证码，跳转填写昵称，头像，性别等信息")
    public Result<Object> checkSmsCode(@RequestParam String mobile, @RequestParam String verifyCode) {
        if (!ToolString.regExpValiMobile(mobile)) {
            return fail("手机号格式不正确");
        }
        //检测注册频率
        Result limitCheckResult = checkLimit();
        if(!limitCheckResult.isSuccess()){
            return limitCheckResult;
        }
        if(userService.findByMobile(mobile)!=null){
            return fail("该手机号已被注册");
        }
        return verifyCodeService.verifyByMobile(mobile, verifyCode, VerifyCode.Type.Register.name());
    }
    /**
     * 校驗用戶名是否可用
     */
    @NoNeedUserToken
    @RequestMapping("/checkUsername")
    @ClientOperLog(module = "用户注册", type = "校验用户名", desc = "用户名注册校验用户名是否可用")
    public Result<Object> checkUsername(@RequestParam String username, String inviteCode,String ticket, String randstr) {
        if (!ToolString.regExpVali(ToolString.pattern_username, username)) {
            return fail("用户名不符合要求");
        }
        if ("1".equals(paramService.getByName(Param.Name.tencent_captcha_on))) {
            //腾讯防水墙校验
            int checkCode = TCaptchaVerify.verifyTicket(paramService.getByName(Param.Name.tencent_captcha_app_id), paramService.getByName(Param.Name.tencent_captcha_app_secret), ticket, randstr, getIp());
            if (checkCode == -1) {
                return fail("安全验证失败");
            }
        }
        //检测注册频率
        Result limitCheckResult = checkLimit();
        if(!limitCheckResult.isSuccess()){
            return limitCheckResult;
        }
        if(userService.findByUsername(username)!=null){
            return fail("该用户名已被注册");
        }
        Result<Object> inviteCodeCheckResult = inviteCodeService.checkCode(inviteCode);
        if(!inviteCodeCheckResult.isSuccess()){
            return inviteCodeCheckResult;
        }
        return success();
    }

    /**
     * 注册
     */
    @NoNeedUserToken
    @RequestMapping("/register")
    @ClientOperLog(module = "用户注册", type = "执行注册", desc = "手机号、邮箱、用户名注册")
    public Result<Object> register(String mobile,
                                   String email,
                                   String username,
                                   @RequestParam String password,
                                   @RequestParam String nickname,
                                   String verifyCode,
                                   @RequestParam(required = false) String inviteCode,
                                   @RequestParam(required = false) String avatar,
                                   @RequestParam(required = false) String smallAvatar,
                                   @RequestParam(required = false) String gender,
                                   @RequestParam(required = false) String birthday
    ) {
        if (isNotEmpty(mobile)&&!ToolString.regExpValiMobile(mobile)) {
            return fail("手机号格式不正确");
        }
        if (isNotEmpty(email)&&!ToolString.regExpVali(ToolString.pattern_email, email)) {
            return fail("邮箱地址非法");
        }
        if (isNotEmpty(username)&&!ToolString.regExpVali(ToolString.pattern_username, username)) {
            return fail("用户名不符合要求");
        }
        if (!ToolString.valiPasswordQQ(password)) {
            return fail("密码由6~20位数字、大小写字母或符号组成，至少含有2种以上字符");
        }
        int nicknameLen = ToolString.getNicknameLength(nickname);
        if (nicknameLen==0||nicknameLen>36) {
            return fail("昵称为1~36个字符,由字母、数字、中文和特殊符号组成");
        }
        //检测注册频率
        Result limitCheckResult = checkLimit();
        if(!limitCheckResult.isSuccess()){
            return limitCheckResult;
        }
        ClientConfig config = clientConfigService.get();
        //昵称唯一
        if(config.getNicknameUnique()&&userService.getCountOfNickname(nickname)>0){
            return fail("昵称已存在");
        }
        //校验邀请码
        Result checkResult = inviteCodeService.checkCode(inviteCode);
        if(!checkResult.isSuccess()){
            return checkResult;
        }
        QUser user = new QUser();
        user.setPassword(password);
        user.setMobile(mobile);
        user.setEmail(email);
        user.setUsername(username);
        user.setNickname(nickname);
        user.setAvatar(avatar);
        user.setSmallAvatar(smallAvatar);
        user.setGender(gender);
        user.setBirthday(ToolDateTime.getDate(birthday,ToolDateTime.pattern_ymd));
        return userService.register(user, verifyCode,inviteCode);
    }
    //检测注册频率
    private Result<Object> checkLimit(){
        try {
            ClientConfig config = clientConfigService.get();
            //ip限制校验
            Integer ipLimitDuration = config.getLimitIpDuration();
            Integer ipLimitCount = config.getLimitIpCount();
            if (ipLimitDuration > 0) {
                if (config.getLimitIpCount() == 0) {
                    return fail("已限制注册，请稍后再试");
                }
                Date now = new Date();
                Date begin = ToolDateTime.getDateByDatePlusSeconds(now, -1 * ipLimitDuration);
                if (loginLogService.getRegisterCountOfIp(begin, now, getIp()) >= ipLimitCount) {
                    return fail("当前ip[" + getIp() + "]单位时间内注册用户数已达上限");
                }
            }
            Integer deviceLimitDuration = config.getLimitDeviceDuration();
            Integer deviceLimitCount = config.getLimitDeviceCount();
            if (deviceLimitDuration > 0) {
                if (config.getLimitIpCount() == 0) {
                    return fail("已限制注册，请稍后再试");
                }
                Date now = new Date();
                Date begin = ToolDateTime.getDateByDatePlusSeconds(now, -1 * deviceLimitDuration);
                if (loginLogService.getRegisterCountOfDevice(begin, now, getDeviceNo()) >= deviceLimitCount) {
                    return fail("当前设备单位时间内注册用户数已达上限");
                }
            }
            return success();
        }catch (Exception e){
            log.error("检测注册频率失败：",e);
            return fail("检测失败，请稍后再试");
        }
    }
}
