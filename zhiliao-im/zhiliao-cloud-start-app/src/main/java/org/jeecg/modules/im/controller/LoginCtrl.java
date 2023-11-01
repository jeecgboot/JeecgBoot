package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.util.JwtUtilApp;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.base.util.ToolString;
import org.jeecg.modules.im.configuration.ClientOperLog;
import org.jeecg.modules.im.base.util.TCaptchaVerify;
import org.jeecg.modules.im.entity.Param;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.VerifyCode;
import org.jeecg.modules.im.service.ParamService;
import org.jeecg.modules.im.service.VerifyCodeService;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.UUID;

/**
 * 用户登录
 */
@RestController
@RequestMapping("/a/login")
@Slf4j
public class LoginCtrl extends BaseApiCtrl {

    @Resource
    private UserService userService;
    @Resource
    private ParamService paramService;
    @Resource
    private VerifyCodeService verifyCodeService;
    @Resource
    @Lazy
    private RedisUtil redisUtil;


    /**
     * 手机号/用户名/邮箱/账号登录
     */
    @NoNeedUserToken
    @PostMapping({"", "/"})
    @ClientOperLog(module = "用户登录", type = "手机号/账号登录", desc = "")
    public Result<Object> index(@RequestParam String account, @RequestParam String password,String ticket,String randstr) {
        if ("1".equals(paramService.getByName(Param.Name.tencent_captcha_on))) {
            //腾讯防水墙校验
            int checkCode = TCaptchaVerify.verifyTicket(paramService.getByName(Param.Name.tencent_captcha_app_id), paramService.getByName(Param.Name.tencent_captcha_app_secret), ticket, randstr, getIp());
            if (checkCode == -1) {
                return fail("安全验证失败");
            }
        }
        // 二次验证成功
        return userService.login(account, password);
    }

    /**
     * 登录二次校验谷歌验证码
     */
    @NoNeedUserToken
    @PostMapping("/checkGoogleCode")
    @ClientOperLog(module = "登录二次校验谷歌验证码", type = "登录二次校验", desc = "")
    public Result<Object> checkGoogleCode(@RequestParam String account,@RequestParam String googleCode) {
        return userService.checkGoogleCode(account, googleCode);
    }



    /**
     * 短信验证码登录
     */
    @NoNeedUserToken
    @PostMapping("/smsCodeLogin/send")
    @ClientOperLog(module = "用户登录", type = "短信验证码登录", desc = "")
    public Result<Object> smsCodeLogin(@RequestParam String mobile, String ticket, String randstr) {
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
        // 二次验证成功
        return verifyCodeService.sendByMobile(mobile, VerifyCode.Type.Login.name());
    }

    /**
     * 校验验证码
     */
    @NoNeedUserToken
    @PostMapping("/smsCodeLogin/check")
    @ClientOperLog(module = "用户登录", type = "校验验证码", desc = "")
    public Result<Object> checkSmsCode(@RequestParam String mobile, @RequestParam String verifyCode) {
        if (!ToolString.regExpValiMobile(mobile)) {
            return fail("手机号格式不正确");
        }
//        if (userService.findByMobile(mobile) == null) {
//            return fail("该手机号未注册");
//        }
        return userService.loginBySmsCode(mobile, verifyCode, VerifyCode.Type.Login.name());
    }

    /**
     * token登录
     * isScan:扫码登录
     */
    @NoNeedUserToken
    @PostMapping("/token")
    @ClientOperLog(module = "用户登录", type = "自动登录", desc = "token登录")
    public Result<Object> tokenLogin(@RequestParam(required = false) Boolean isScan) {
        // 二次验证成功
        return userService.tokenLogin(isScan);
    }

    /**
     * 被扫端获取登录二维码
     * 一分钟有效
     * creator：创建者
     * @return
     */
    @NoNeedUserToken
    @GetMapping("/getLoginQrcode")
    public Result<Object> getLoginQrcode() {
        String deviceId = getDeviceNo();
        String code = UUID.randomUUID().toString();
        String link = "https://zhiliao.info/qrcode_login/"+code;
        Kv kv = Kv.by("code",code).set("creator",deviceId).set("status",-1);
        redisUtil.set(code,kv,60);
        kv.set("link",link);
        return success(kv);
    }

    /**
     * 被扫端获取扫码授权结果
     * @param qrcode
     * @return
     */
    @NoNeedUserToken
    @GetMapping("/getGrantStatus")
    public Result<Object> getGrantStatus(String qrcode) {
        String deviceId = getDeviceNo();
        Kv kv = (Kv) redisUtil.get(qrcode);
        //不存在或是他人创建的
        if(kv==null){
            return fail();
        }
        Object creator = kv.get("creator");
        if(creator!=null&&!equals(creator.toString(),deviceId)){
            return fail();
        }
        Integer status = kv.getInt("status");
        if(status!=null){
            if(status==1){
                Integer userId = kv.getInt("userId");
                User user = userService.getById(userId);
                if(user==null){
                    redisUtil.del(deviceId);
                    return fail();
                }
                String token = JwtUtilApp.sign(user.getId(), user.getPassword(),user.getAccount());
                kv.set("token",token);
            }
        }
        return success(kv);
    }
    /**
     * 扫码者获取扫码信息
     * 绑定扫码者
     * @param qrcode
     * @return
     */
    @PostMapping("/getQrcodeInfo")
    public Result<Object> getQrcodeInfo(String qrcode) {
        String deviceId = getDeviceNo();
        Kv kv = (Kv) redisUtil.get(qrcode);
        if(kv==null){
            return fail();
        }
        Object scanner = kv.get("scanner");
        if(scanner!=null&&!equals(scanner.toString(),deviceId)){
            return fail();
        }
        if(kv.getInt("status")>0){
            return fail();
        }

        long seconds = redisUtil.getExpire(qrcode);
        kv.set("scanner",deviceId);
        User user = getCurrentUser();
        kv.set("userId",user.getId());
        kv.set("nickname",ToolString.nicknameTuoMin(user.getNickname()));
        kv.set("status",0);
        redisUtil.set(qrcode,kv,seconds);
        return success(kv);
    }
    /**
     * 扫码者扫码授权
     * 拒绝则将二维码失效
     * @param qrcode
     * @param isGrant
     * @return
     */
    @PostMapping("/grantScanLogin")
    public Result<Object> grantScanLogin(String qrcode,boolean isGrant) {
        String deviceId = getDeviceNo();
        Kv kv = (Kv) redisUtil.get(qrcode);
        if(kv==null){
            return fail();
        }
        Object scanner = kv.get("scanner");
        if(scanner==null||!equals(scanner.toString(),deviceId)){
            return fail();
        }
        long seconds = redisUtil.getExpire(qrcode);
        kv.set("status",isGrant?1:2);
        redisUtil.set(qrcode,kv,seconds);
        return success(qrcode);
    }
}
