package org.jeecg.modules.system.model;


import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 登录表单
 *
 * @Author scott
 * @since  2019-01-18
 */
@Schema(description="登录对象")
public class SysLoginModel {
	@Schema(description = "账号")
    private String username;
	@Schema(description = "密码")
    private String password;
	@Schema(description = "验证码")
    private String captcha;
	@Schema(description = "验证码key")
    private String checkKey;

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

	public String getCheckKey() {
		return checkKey;
	}

	public void setCheckKey(String checkKey) {
		this.checkKey = checkKey;
	}
    
}