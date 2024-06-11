package org.jeecg.config.shiro.ignore;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用内存存储通过@IgnoreAuth注解的url，配合JwtFilter进行免登录校验
 * PS：无法使用ThreadLocal进行存储，因为ThreadLocal装载时，JwtFilter已经初始化完毕，导致该类获取ThreadLocal为空
 * @author eightmonth
 * @date 2024/4/18 15:02
 */
public class InMemoryIgnoreAuth {
    private static final List<String> IGNORE_AUTH_LIST = new ArrayList<>();

    public InMemoryIgnoreAuth() {}

    public static void set(List<String> list) {
        IGNORE_AUTH_LIST.addAll(list);
    }

    public static List<String> get() {
        return IGNORE_AUTH_LIST;
    }

    public static void clear() {
        IGNORE_AUTH_LIST.clear();
    }

    public static boolean contains(String url) {
        for (String ignoreAuth : IGNORE_AUTH_LIST) {
            if (url.endsWith(ignoreAuth)) {
                return true;
            }
        }

        return false;
    }
}
