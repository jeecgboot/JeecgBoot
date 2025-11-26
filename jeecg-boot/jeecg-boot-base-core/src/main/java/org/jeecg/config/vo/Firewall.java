package org.jeecg.config.vo;

/**
 * 平台安全配置
 *
 * @author: scott
 * @date: 2023年09月05日 9:25
 */
public class Firewall {
    /**
     * 数据源安全 (开启后，Online报表和图表的数据源为必填)
     */
    private Boolean dataSourceSafe = false;
    /**
     * 是否禁止使用 * 查询所有字段
     */
    private Boolean disableSelectAll = false;
    /**
     * 低代码模式（dev:开发模式，prod:发布模式——关闭所有在线开发配置能力）
     */
    private String lowCodeMode;
    /**
     * 是否允许同一账号多地同时登录 （为 true 时允许一起登录, 为 false 时新登录挤掉旧登录）
     */
    private Boolean isConcurrent = true;
    /**
     * 是否开启默认密码登录提醒（true 登录后提示必须修改默认密码）
     */
    private Boolean enableDefaultPwdCheck = false;

    /**
     * 是否开启登录验证码校验（true 开启；false 关闭并跳过验证码逻辑）
     */
    private Boolean enableLoginCaptcha = true;

//    /**
//     * 表字典安全模式（white:白名单——配置了白名单的表才能通过表字典方式访问，black:黑名单——配置了黑名单的表不允许表字典方式访问）
//     */
//    private String tableDictMode;


    public Boolean getEnableLoginCaptcha() {
        return enableLoginCaptcha;
    }

    public void setEnableLoginCaptcha(Boolean enableLoginCaptcha) {
        this.enableLoginCaptcha = enableLoginCaptcha;
    }

    public Boolean getEnableDefaultPwdCheck() {
        return enableDefaultPwdCheck;
    }

    public void setEnableDefaultPwdCheck(Boolean enableDefaultPwdCheck) {
        this.enableDefaultPwdCheck = enableDefaultPwdCheck;
    }

    public Boolean getDataSourceSafe() {
        return dataSourceSafe;
    }

    public void setDataSourceSafe(Boolean dataSourceSafe) {
        this.dataSourceSafe = dataSourceSafe;
    }

    public String getLowCodeMode() {
        return lowCodeMode;
    }

    public void setLowCodeMode(String lowCodeMode) {
        this.lowCodeMode = lowCodeMode;
    }

    public Boolean getDisableSelectAll() {
        return disableSelectAll;
    }

    public void setDisableSelectAll(Boolean disableSelectAll) {
        this.disableSelectAll = disableSelectAll;
    }

    public Boolean getIsConcurrent() {
        return isConcurrent;
    }

    public void setIsConcurrent(Boolean isConcurrent) {
        this.isConcurrent = isConcurrent;
    }
}
