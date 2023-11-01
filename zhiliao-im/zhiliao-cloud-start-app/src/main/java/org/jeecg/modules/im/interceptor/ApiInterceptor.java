package org.jeecg.modules.im.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtilApp;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.base.constant.ConstantWeb;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.util.IPUtil;
import org.jeecg.modules.im.base.util.ToolWeb;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.BlockIpService;
import org.jeecg.modules.im.service.DeviceService;
import org.jeecg.modules.im.service.ParamService;
import org.jeecg.modules.im.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * api访问拦截器
 */
@Slf4j
public class ApiInterceptor implements HandlerInterceptor {
    @Resource
    private ParamService paramService;
    @Resource
    private UserService userService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private BlockIpService blockIpService;
    @Value("${spring.profiles.active}")
    private String evn;
    @Lazy
    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestIp = request.getHeader(ConstantWeb.IP);
        if(StringUtils.isBlank(requestIp)){
            requestIp = IPUtil.getIpAddr(request);
        }
        Result<Object> result = blockIpService.checkIp(requestIp);
        if (!result.isSuccess()) {
            throw new BusinessException(requestIp+"已被禁止访问");
        }
        String actionKey = ToolWeb.getActionKey(request);
        //过滤swagger
        if(actionKey.equals("/")||actionKey.equals("/doc.html")||actionKey.startsWith("/webjars")||actionKey.startsWith("/swagger-resources")||actionKey.startsWith("/v2-apidocs")){
            return true;
        }
        //判断方法是否需要校验token
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 从方法处理器中获取出要调用的方法
            Method method = handlerMethod.getMethod();
            // 获取出方法上的Access注解
            NoNeedUserToken noNeedUserToken = method.getAnnotation(NoNeedUserToken.class);
            if (noNeedUserToken != null) {
                // 如果注解为null说明不需要拦截, 直接放过
                return true;
            }
        }
        String devicePlatform = request.getHeader(ConstantWeb.DEVICE_PLATFORM);
        String deviceNo = request.getHeader(ConstantWeb.DEVICE_NO);
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        if (oConvertUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        checkUserTokenIsEffect(token,devicePlatform,deviceNo);
        return true;
    }


    /**
     * 校验token的有效性
     *
     * @param token
     */
    public User checkUserTokenIsEffect(String token,String platform,String no) throws AuthenticationException {
        // 解密获得userId，用于和数据库进行对比
        Integer userId = JwtUtilApp.getUserIdByToken(token);
        if (userId == null) {
            throw new AuthenticationException("token无效");
        }

        // 查询用户信息
        log.debug("———校验token是否有效————checkUserTokenIsEffect——————— "+ token);
        User user = userService.findById(userId);
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }
        // 判断用户状态
        if (user.getTsLocked()>0) {
            throw new AuthenticationException("账号已被锁定");
        }
        return user;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }



}