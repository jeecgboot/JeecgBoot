package org.jeecg.config.jimureport;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义积木报表鉴权(如果不进行自定义，则所有请求不做权限控制)
 *  * 1.自定义获取登录token
 *  * 2.自定义获取登录用户
 */


@Slf4j
@Component
public class JimuReportTokenService implements JmReportTokenServiceI {
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

    @Override
    public Map<String, Object> getUserInfo(String token) {
        Map<String, Object> map = new HashMap<String, Object>();
        String username = JwtUtil.getUsername(token);
        //此处通过token只能拿到一个信息 用户账号  后面的就是根据账号获取其他信息 查询数据或是走redis 用户根据自身业务可自定义
        SysUserCacheInfo userInfo = null;
        try {
            userInfo = sysBaseAPI.getCacheUser(username);
        } catch (Exception e) {
            log.error("获取用户信息异常:"+ e.getMessage());
            return map;
        }
        //设置账号名
        map.put(SYS_USER_CODE, userInfo.getSysUserCode());
        //设置部门编码
        map.put(SYS_ORG_CODE, userInfo.getSysOrgCode());
        // 将所有信息存放至map 解析sql/api会根据map的键值解析
        return map;
    }
}
