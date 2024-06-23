package org.apache.shiro;

import org.apache.shiro.subject.Subject;

/**
 * 兼容处理Online功能使用处理，请勿修改
 * @author eightmonth@qq.com
 * @date 2024/4/29 14:05
 */
public class SecurityUtils {


    public static Subject getSubject() {
        return new Subject() {
            @Override
            public Object getPrincipal() {
                return Subject.super.getPrincipal();
            }
        };
    }
}
