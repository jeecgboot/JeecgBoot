package org.jeecg.config.vo;

import java.util.List;

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

    //update-begin---author:wangshuai ---date:2026-06-29  for：【QQYUN-16619】三级等保密码强度开关-----------
    /**
     * 是否开启三级等保强密码校验（true 开启强密码模式，false 使用简单密码规则）
     */
    private Boolean enableStrongPwd = false;
    //update-end---author:wangshuai ---date:2026-06-29  for：【QQYUN-16619】三级等保密码强度开关-----------

    /**
     * 是否允许访问本地网络地址（内网 10/8、172.16/12、192.168/16、100.64/10、fc00::/7）：按URL下载文件时（网络图片上传、AI附件、知识库网页等）生效。
     * 默认 true 允许，兼容企业内网 MinIO/文件服务；设为 false 后拦截，白名单除外。本机其他端口、链路本地（含云元数据）、0.0.0.0 始终拦截，不受此开关影响。
     *
     * @author scott
     * @since 2026-10-26 【GHSA-5g5g-43q2-g87v】【GHSA-hw5p-65q6-qcxr】SSRF 内网段可配置拦截
     */
    private Boolean allowLocalAddress = true;

    /**
     * 本地地址白名单：allowLocalAddress=false 时仍允许访问的内网地址，支持 IP、CIDR 网段（如 10.0.1.0/24）、域名（精确匹配）；
     * 写"地址:端口"（如 localhost:9999）只放行该端口，本机地址也可按端口放行，不受 allowLocalAddress 开关影响
     *
     * @author scott
     * @since 2026-10-26 【GHSA-5g5g-43q2-g87v】【GHSA-hw5p-65q6-qcxr】SSRF 内网段可配置拦截
     */
    private List<String> localAddressWhiteList;

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

    //update-begin---author:wangshuai ---date:2026-06-29  for：【QQYUN-16619】三级等保密码强度开关-----------
    public Boolean getEnableStrongPwd() {
        return enableStrongPwd;
    }

    public void setEnableStrongPwd(Boolean enableStrongPwd) {
        this.enableStrongPwd = enableStrongPwd;
    }
    //update-end---author:wangshuai ---date:2026-06-29  for：【QQYUN-16619】三级等保密码强度开关-----------

    public Boolean getAllowLocalAddress() {
        return allowLocalAddress;
    }

    public void setAllowLocalAddress(Boolean allowLocalAddress) {
        this.allowLocalAddress = allowLocalAddress;
    }

    public List<String> getLocalAddressWhiteList() {
        return localAddressWhiteList;
    }

    public void setLocalAddressWhiteList(List<String> localAddressWhiteList) {
        this.localAddressWhiteList = localAddressWhiteList;
    }
}
