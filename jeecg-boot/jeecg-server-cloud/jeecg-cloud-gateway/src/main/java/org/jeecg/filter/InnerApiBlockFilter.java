package org.jeecg.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Set;

/**
 * 禁止外部经网关访问微服务内部接口 /sys/api/**、/airag/api/**、/online/api/**、/drag/api/**、/airag/flow/api/**、/eoa/api/**、/desform/inner/api/**、/act/designer/miniDesFlow/inner/api/**
 * <p>
 * 这些是各服务提供给其它微服务的 Feign 内部接口（含数据源明文口令、用户资料、任意表数据读写等），
 * Feign 按服务名直连，不经过网关，因此在网关拦截不影响内部调用。
 *
 * @author scott
 * @since 2026-10-14 微服务模式下外部请求可经网关直调内部接口（GHSA-vrqv-3gwg-423j / GHSA-hh6j-6p7c-8hrf / GHSA-483m-f38h-369m / GHSA-5q28-hq94-q2fj）
 */
@Slf4j
@Component
public class InnerApiBlockFilter implements GlobalFilter, Ordered {

    /**
     * 微服务内部接口路径前缀（小写）：/sys/api、/airag/api、/online/api、/drag/api、/airag/flow/api、/eoa/api（GHSA-v952-qq2h-377j / GHSA-qj27-82m3-9q8q / GHSA-2xf5-w883-78m9）
     */
    private static final String[] INNER_API_PREFIXES = {"/sys/api/", "/airag/api/", "/online/api/", "/drag/api/", "/airag/flow/api/", "/eoa/api/", "/desform/inner/api/", "/act/designer/minidesflow/inner/api/"};

    /**
     * 放行的接口（规范化后的小写路径）：旧版 PC 端、移动端 EOA 协同催办仍直接调用的过渡接口（下个版本删除）；前端字典翻译在用的 Online 报表字典
     */
    private static final Set<String> ALLOW_PATHS = Set.of("/online/api/cgreportgetdatapackage");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = normalizePath(exchange.getRequest().getURI().getRawPath());
        //update-begin---author:scott ---date:20261014  for：【GHSA-v952-qq2h-377j/GHSA-qj27-82m3-9q8q】网关拦截的内部接口由 /sys/api 扩展为多个前缀，增加 /airag/api、/online/api、/drag/api、/airag/flow/api、/eoa/api-----------
        for (String prefix : INNER_API_PREFIXES) {
            int idx = path.indexOf(prefix);
            if (idx >= 0 || path.endsWith(prefix.substring(0, prefix.length() - 1))) {
                String innerPath = idx >= 0 ? path.substring(idx) : path;
                if (!ALLOW_PATHS.contains(innerPath)) {
                    log.warn("拦截外部访问内部接口: {}", exchange.getRequest().getURI().getRawPath());
                    return forbidden(exchange.getResponse());
                }
            }
        }
        //update-end---author:scott ---date:20261014  for：【GHSA-v952-qq2h-377j/GHSA-qj27-82m3-9q8q】网关拦截的内部接口由 /sys/api 扩展为多个前缀，增加 /airag/api、/online/api、/drag/api、/airag/flow/api、/eoa/api-----------
        return chain.filter(exchange);
    }

    /**
     * 规范化路径，防止编码、矩阵参数、重复斜杠、大小写等方式绕过
     */
    private static String normalizePath(String rawPath) {
        if (rawPath == null) {
            return "";
        }
        String path = rawPath;
        // 多次解码，防止 %252F 之类的二次编码
        for (int i = 0; i < 3; i++) {
            String decoded = UriUtils.decode(path, StandardCharsets.UTF_8);
            if (decoded.equals(path)) {
                break;
            }
            path = decoded;
        }
        // 去掉矩阵参数（Spring MVC 默认忽略 ;xxx），统一斜杠并合并重复斜杠
        path = path.replace('\\', '/').replaceAll(";[^/]*", "").replaceAll("/{2,}", "/");
        return path.toLowerCase(Locale.ROOT);
    }

    private static Mono<Void> forbidden(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] body = "{\"success\":false,\"code\":403,\"message\":\"内部接口禁止外部访问\"}".getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(body);
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        // 早于 GlobalAccessTokenFilter(0) 执行
        return -100;
    }
}
