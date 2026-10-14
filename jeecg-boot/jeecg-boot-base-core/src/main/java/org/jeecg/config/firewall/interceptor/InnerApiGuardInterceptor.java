package org.jeecg.config.firewall.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 单体模式下禁止外部调用微服务内部接口
 * <p>
 * /sys/api、/airag/api、/online/api、/drag/api 等是提供给其它微服务 Feign 调用的内部接口，
 * 单体模式下各模块为进程内调用，不需要对外暴露。其中部分接口位于依赖 jar 中无法直接修改，
 * 因此按 Spring 实际匹配到的接口路径模板统一拦截（不受 URL 编码、大小写、重复斜杠等写法影响）。
 *
 * @author scott
 * @since 2026-10-14 单体模式下内部接口可被任意登录用户直接调用（读写任意数据、读取数据源口令等）
 */
@Slf4j
public class InnerApiGuardInterceptor implements HandlerInterceptor {

    /**
     * 微服务内部接口路径前缀
     */
    private static final String[] INNER_API_PREFIXES = {"/sys/api/", "/airag/api/", "/online/api/", "/drag/api/"};

    /**
     * 前端在用、需要放行的接口（Online 报表字典翻译）
     */
    private static final Set<String> ALLOW_PATTERNS = Set.of("/online/api/cgreportGetDataPackage");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        Object pattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        if (pattern == null) {
            return true;
        }
        String matched = pattern.toString();
        if (isInnerApi(matched) && !ALLOW_PATTERNS.contains(matched)) {
            log.warn("单体模式禁止外部调用内部接口: {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print(JSON.toJSONString(Result.error(HttpServletResponse.SC_FORBIDDEN, "内部接口禁止外部访问")));
            return false;
        }
        return true;
    }

    private static boolean isInnerApi(String pattern) {
        for (String prefix : INNER_API_PREFIXES) {
            if (pattern.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
