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
 * /sys/api、/airag/api、/online/api、/drag/api、/airag/flow/api、/eoa/api 等是提供给其它微服务 Feign 调用的内部接口，
 * 单体模式下各模块为进程内调用，不需要对外暴露。按 Spring 实际匹配到的接口路径模板统一拦截
 * （不受 URL 编码、大小写、重复斜杠等写法影响），作为控制器加载条件之外的兜底。
 *
 * @author scott
 * @since 2026-10-14 单体模式下内部接口可被任意登录用户直接调用（GHSA-vrqv-3gwg-423j / GHSA-v952-qq2h-377j / GHSA-qj27-82m3-9q8q / GHSA-2xf5-w883-78m9）
 */
@Slf4j
public class InnerApiGuardInterceptor implements HandlerInterceptor {

    /**
     * 微服务内部接口路径前缀（/airag/flow/api：按 id 执行任意租户的 AI 流程；/eoa/api：文件、公文模板、考勤补卡）
     */
    private static final String[] INNER_API_PREFIXES = {"/sys/api/", "/airag/api/", "/online/api/", "/drag/api/", "/airag/flow/api/", "/eoa/api/"};

    /**
     * 放行的接口：前端字典翻译在用的 Online 报表字典；旧版 PC 端、移动端 EOA 协同催办在用的过渡接口（下个版本删除）
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
