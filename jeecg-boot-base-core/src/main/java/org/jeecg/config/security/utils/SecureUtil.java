package org.jeecg.config.security.utils;

import com.alibaba.fastjson2.JSONObject;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author EightMonth
 * @date 2024/1/10 17:03
 */
public class SecureUtil {

    public static LoginUser currentUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return JSONObject.parseObject(name, LoginUser.class);
    }
}
