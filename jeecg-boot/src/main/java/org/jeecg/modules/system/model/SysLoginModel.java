package org.jeecg.modules.system.model;
/**
 * 登录表单
 *
 * @author scott
 * @since  2019-01-18
 */
public class SysLoginModel {
    private String username;
    private String password;
    //验证码
    private String captcha;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}