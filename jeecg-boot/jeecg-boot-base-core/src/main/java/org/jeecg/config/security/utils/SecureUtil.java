package org.jeecg.config.security.utils;

import com.alibaba.fastjson2.JSONObject;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 认证信息工具类
 * @author EightMonth
 * @date 2024/1/10 17:03
 */
public class SecureUtil {

    /**
     * 通过当前认证信息获取用户信息
     * @return
     */
    public static LoginUser currentUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return JSONObject.parseObject(name, LoginUser.class);
    }
}
