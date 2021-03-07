package org.jeecg.config.jimureport;

import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
* 自定义积木报表鉴权实现类(如果不进行自定义，则所有请求不做权限控制)
 * 1.自定义获取登录token
 * 2.自定义获取登录用户
*/
@Component
class JimuReportTokenService implements JmReportTokenServiceI {
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    @Lazy
    private RedisUtil redisUtil;

    @Override
    public String getToken(HttpServletRequest request) {
        return TokenUtils.getTokenByRequest(request);
    }

    @Override
    public String getUsername(String token) {
        return JwtUtil.getUsername(token);
    }

    @Override
    public Boolean verifyToken(String token) {
        return TokenUtils.verifyToken(token, sysBaseAPI, redisUtil);
    }
}
