package org.jeecg.common.util.security;

import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;

/**
 * jdbc连接校验
 * @Author taoYan
 * @Date 2022/8/10 18:15
 **/
public class JdbcSecurityUtil {

    /**
     * 连接驱动漏洞 最新版本修复后，可删除相应的key
     * postgre：authenticationPluginClassName, sslhostnameverifier, socketFactory, sslfactory, sslpasswordcallback
     * https://github.com/pgjdbc/pgjdbc/security/advisories/GHSA-v7wg-cpwc-24m4
     * mysql: allowLoadLocalInfile, allowUrlInLocalInfile, allowLoadLocalInfileInPath, autoDeserialize
     * h2: INIT, TRACE_LEVEL_SYSTEM_OUT
     */
    public static final String[] notAllowedProps = new String[]{
        "authenticationPluginClassName", "sslhostnameverifier", "socketFactory", "sslfactory", "sslpasswordcallback",
        "allowLoadLocalInfile", "allowUrlInLocalInfile", "allowLoadLocalInfileInPath", "autoDeserialize",
        "INIT", "TRACE_LEVEL_SYSTEM_OUT"
    };

    /**
     * 校验sql是否有特定的key
     * @param jdbcUrl
     * @return
     */
    public static void validate(String jdbcUrl){
        if(oConvertUtils.isEmpty(jdbcUrl)){
            return;
        }

        // 同时检查 ? 和 ; 分隔符，防止通过分号方式（H2/MySQL）绕过校验
        String argString = null;
        int questionMarkIndex = jdbcUrl.indexOf("?");
        int semicolonIndex = jdbcUrl.indexOf(";");

        if(questionMarkIndex < 0 && semicolonIndex < 0){
            return;
        }

        // 收集所有参数：既检查 ? 后的参数，也检查 ; 后的参数
        StringBuilder allArgs = new StringBuilder();
        if(questionMarkIndex >= 0){
            allArgs.append(jdbcUrl.substring(questionMarkIndex + 1));
        }
        if(semicolonIndex >= 0){
            String semicolonPart = jdbcUrl.substring(semicolonIndex + 1);
            if(allArgs.length() > 0){
                allArgs.append("&");
            }
            // 将分号分隔的参数转换为 & 分隔，以便统一处理
            allArgs.append(semicolonPart.replace(";", "&"));
        }

        String[] keyAndValues = allArgs.toString().split("&");
        for(String temp: keyAndValues){
            if(oConvertUtils.isEmpty(temp)){
                continue;
            }
            String key = temp.split("=")[0].trim();
            for(String prop: notAllowedProps){
                if(prop.equalsIgnoreCase(key)){
                    throw new JeecgBootException("连接地址有安全风险，【"+key+"】");
                }
            }
        }
    }

}
