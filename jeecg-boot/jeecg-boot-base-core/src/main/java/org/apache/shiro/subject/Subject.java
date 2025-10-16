package org.apache.shiro.subject;


import org.jeecg.common.util.LoginUserUtils;

/**
 * 兼容处理Online功能使用处理，请勿修改
 * @author eightmonth@qq.com
 * @date 2024/4/29 14:18
 */
public interface Subject {
    default Object getPrincipal() {
        return LoginUserUtils.getSessionUser();
    }
}
