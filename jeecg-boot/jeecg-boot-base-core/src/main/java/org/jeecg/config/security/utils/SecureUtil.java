package org.jeecg.config.security.utils;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 认证信息工具类
 * @author EightMonth
 * @date 2024/1/10 17:03
 */
@Slf4j
public class SecureUtil {

    /**
     * 通过当前认证信息获取用户信息
     * @return
     */
    public static LoginUser currentUser() {
        String userInfoJson = SecurityContextHolder.getContext().getAuthentication().getName();
        //log.info("SecureUtil.currentUser: {}", userInfoJson);
        return JSONObject.parseObject(userInfoJson, LoginUser.class);
    }

}
