package org.jeecg.config.security;

/**
 * 登录模式
 * @author EightMonth
 * @date 2024/1/10 17:43
 */
public class LoginType {

    /**
     * 密码模式
     */
    public static final String PASSWORD = "password";


    /**
     * 手机号+验证码模式
     */
    public static final String PHONE = "phone";


    /**
     * app登录
     */
    public static final String APP = "app";

    /**
     * 扫码登录
     */
    public static final String SCAN = "scan";

    /**
     * 所有联合登录，比如github\钉钉\企业微信\微信
     */
    public static final String SOCIAL = "social";
}
