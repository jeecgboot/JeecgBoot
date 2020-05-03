package org.jeecg.modules.shiro.authc.interceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.OnlineAuth;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.service.ISysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Online 自定义请求拦截器
 *
 * @author: taoyan
 * @date: 2020年03月20日
 */
@Slf4j
public class OnlineInterceptor implements HandlerInterceptor {

    @Autowired
    private ISysPermissionService sysPermissionService;

    /**
     * online表单菜单地址
     */
    private static final String ONLINE_FORM = "/online/cgform";

    /**
     * online功能测试地址 前缀
     */
    private static final String[] ONLINE_TEST_PRE = {"/online/cgformErpList", "/online/cgformList", "/online/cgformTreeList"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 请求的方法是否有注解
        boolean anno = handler.getClass().isAssignableFrom(HandlerMethod.class);
        if (anno) {
            OnlineAuth onlineAuth = ((HandlerMethod) handler).getMethodAnnotation(OnlineAuth.class);
            if (onlineAuth != null) {
                String requestPath = request.getRequestURI().substring(request.getContextPath().length());
                requestPath = filterUrl(requestPath);
                //1.通过前端请求地址得到code
                String authKey = onlineAuth.value();
                String code = requestPath.substring(requestPath.lastIndexOf(authKey) + authKey.length());
                log.info("拦截请求(" + request.getMethod() + ")：" + requestPath + ",");
                if ("form".equals(authKey) && "DELETE".equals(request.getMethod())) {
                    code = code.substring(0, code.lastIndexOf("/"));
                }
                List<String> possibleUrl = new ArrayList<>();
                //获取可能的表单地址
                for (String pre : ONLINE_TEST_PRE) {
                    possibleUrl.add(pre + code);
                }
                //查询菜单
                LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
                query.eq(SysPermission::getDelFlag, 0);
                query.in(SysPermission::getUrl, possibleUrl);
                List<SysPermission> permissionList = sysPermissionService.list(query);

                String username = JwtUtil.getUserNameByToken(request);
                if (permissionList == null || permissionList.size() == 0) {
                    //没有配置菜单 找online表单菜单地址
                    boolean hasPermission = sysPermissionService.hasPermission(username, ONLINE_FORM);
                    if (!hasPermission) {
                        backError(response, authKey);
                        return false;
                    }
                } else {
                    //找到菜单了
                    boolean has = false;
                    for (SysPermission p : permissionList) {
                        has = has || sysPermissionService.hasPermission(username, p);
                    }
                    if (!has) {
                        backError(response, authKey);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 地址过滤
     *
     * @param requestPath
     * @return
     */
    private String filterUrl(String requestPath) {
        String url = "";
        if (oConvertUtils.isNotEmpty(requestPath)) {
            url = requestPath.replace("\\", "/");
            url = requestPath.replace("//", "/");
            if (url.indexOf("//") >= 0) {
                url = filterUrl(url);
            }
        }
        return url;
    }

    /**
     * 返回一个错误信息
     *
     * @param response
     * @param authKey
     */
    private void backError(HttpServletResponse response, String authKey) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("auth", "fail");
        try {
            writer = response.getWriter();
            if ("exportXls".equals(authKey)) {
                writer.print("");
            } else {
                Result<?> result = Result.error("无权限访问(操作)");
                writer.print(JSON.toJSON(result));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
