package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.google.GoogleAuthenticator;
import org.jeecg.modules.im.base.util.ToolPassword;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.UserSetting;
import org.jeecg.modules.im.entity.query_helper.QUser;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.UserSettingService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户
 */
@Slf4j
@RestController
@RequestMapping("/a/user")
public class UserCtrl extends BaseApiCtrl {

    @Resource
    private UserService userService;
    @Resource
    private UserSettingService userSettingService;

    /**
     * 注销登录
     */
    @PostMapping("/logout")
    public Result<Object> logout(){
        return userService.logout();
    }
    /**
     * 用户资料
     */
    @PostMapping("/info")
    public Result<Object> info(){
        return success(userService.getBasicInfoById(getCurrentUserId()));
    }

    /**
     * 更新用户资料
     */
    @PostMapping("/updateInfo")
    public Result<Object> updateInfo(QUser param){
        return userService.updateInfo(param);
    }
    /**
     * 更新用户设置
     */
    @PostMapping("/updateSetting")
    public Result<Object> updateSetting(UserSetting setting){
        return userSettingService.updateSetting(setting);
    }

    //修改密码 通过旧密码
    @PostMapping("/updatePwd")
    public Result<Object> updatePwd(@RequestParam String oldPwd,@RequestParam String newPwd){
        return userService.updatePwd(getCurrentUserId(),oldPwd,newPwd);
    }


    /**
     * 根据id查找
     */
    @PostMapping("/getById")
    public Result<Object> getById(@RequestParam Integer id){
        return success(userService.getBasicInfoWithoutSettingById(id));
    }
    /**
     * 根据qrCode查找
     */
    @PostMapping("/getByQrCode")
    public Result<Object> getByQrCode(@RequestParam String qrCode){
        User user = userService.findByQrCode(qrCode);
        if(user==null){
            return fail("用户不存在");
        }
        return success(userService.getBasicInfoById(user.getId()));
    }
    /**
     * 检测支付密码
     */
    @PostMapping("/checkPayPwd")
    public Result<Object> checkPayPwd(@RequestParam String pwd){
        User user = getCurrentUser();
        if(!ToolPassword.checkPassword(user.getPaySalt(),user.getPayPassword(),pwd)){
            return fail();
        }
        return success();
    }
    /**
     * 设置支付密码
     * pwd:新密码
     * oldPwd:旧密码
     */
    @PostMapping("/setPayPassword")
    public Result<Object> setPayPassword(@RequestParam String pwd,@RequestParam(required = false) String oldPwd){
        User user = getCurrentUser();
        return userService.setPayPassword(user,oldPwd,pwd);
    }
    /**
     * 用户名可用
     */
    @PostMapping("/usernameAvailable")
    public Result<Object> usernameAvailable(@RequestParam String username){
        if(equals(username,getCurrentUser().getUsername())){
            return success();
        }
        User temp = userService.findByUsername(username);
        if(temp!=null&&!temp.getId().equals(getCurrentUserId())){
            return fail();
        }
        return success();
    }
    /**
     * 生成谷歌密钥
     */
    @PostMapping("/googleCodeGen")
    public Result<Object> googleCodeGen(){
        String account = getCurrentUser().getAccount();
        return success(GoogleAuthenticator.genSecret(account));
    }

    /**
     * 设置谷歌密钥
     */
    @PostMapping("/setGoogleCode")
    public Result<Object> setGoogleCode(@RequestParam(required = false) String code,@RequestParam(required = false) Boolean enable){
        User user = getCurrentUser();
        return userService.setGoogleCode(user,code,enable);
    }
}
