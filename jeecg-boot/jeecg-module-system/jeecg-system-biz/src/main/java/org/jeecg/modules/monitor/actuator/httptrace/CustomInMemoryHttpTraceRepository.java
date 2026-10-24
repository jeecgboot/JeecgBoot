package org.jeecg.modules.monitor.actuator.httptrace;

import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 自定义内存请求追踪存储
 * @Author: chenrui
 * @Date: 2024/5/13 17:02
 */
public class CustomInMemoryHttpTraceRepository extends InMemoryHttpExchangeRepository {

    @Override
    public List<HttpExchange> findAll() {
        return super.findAll();
    }

    /**
     * for [issues/8309]系统监控>请求追踪，列表每刷新一下，总数据就减一#8309
     * @param trace
     * @author chenrui
     * @date 2025/6/4 19:38
     */
    @Override
    public void add(HttpExchange trace) {
        // 只有当请求不是OPTIONS方法，并且URI不包含httptrace时才记录数据
        if (!"OPTIONS".equals(trace.getRequest().getMethod()) &&
                !trace.getRequest().getUri().toString().contains("httptrace")) {
            //update-begin---author:scott ---date:20261023  for：请求追踪记录前脱敏，避免token、cookie等凭据经追踪接口泄露------------
            super.add(desensitize(trace));
            //update-end---author:scott ---date:20261023  for：请求追踪记录前脱敏------------
        }
    }

    /**
     * 脱敏占位符
     */
    private static final String MASK = "******";

    /**
     * 需要脱敏的请求头/响应头（小写比较）
     */
    private static final Set<String> SENSITIVE_HEADERS = Set.of(
            "x-access-token", "authorization", "proxy-authorization", "cookie", "set-cookie", "x-sign");

    /**
     * URL 上携带凭据的参数，如 ?token=xxx
     */
    private static final Pattern SENSITIVE_QUERY = Pattern.compile("(?i)([?&](?:token|access_token|x-access-token)=)[^&#]*");

    /**
     * 复制一份请求追踪记录，把请求/响应中的凭据替换为占位符后再存储
     *
     * @author scott
     * @since 2026-10-23 请求追踪记录脱敏，避免泄露 X-Access-Token
     */
    private static HttpExchange desensitize(HttpExchange trace) {
        HttpExchange.Request request = trace.getRequest();
        HttpExchange.Request safeRequest = new HttpExchange.Request(maskUri(request.getUri()), request.getRemoteAddress(),
                request.getMethod(), maskHeaders(request.getHeaders()));
        HttpExchange.Response response = trace.getResponse();
        HttpExchange.Response safeResponse = response == null ? null
                : new HttpExchange.Response(response.getStatus(), maskHeaders(response.getHeaders()));
        return new HttpExchange(trace.getTimestamp(), safeRequest, safeResponse, trace.getPrincipal(), trace.getSession(), trace.getTimeTaken());
    }

    /**
     * 请求头/响应头脱敏：命中 SENSITIVE_HEADERS 的头（不区分大小写）值替换为占位符
     *
     * @author scott
     * @since 2026-10-23 请求追踪记录脱敏，避免泄露 X-Access-Token
     */
    private static Map<String, List<String>> maskHeaders(Map<String, List<String>> headers) {
        if (headers == null || headers.isEmpty()) {
            return headers;
        }
        Map<String, List<String>> safeHeaders = new LinkedHashMap<>(headers.size());
        headers.forEach((name, values) -> safeHeaders.put(name,
                name != null && SENSITIVE_HEADERS.contains(name.toLowerCase(Locale.ROOT)) ? List.of(MASK) : values));
        return safeHeaders;
    }

    /**
     * URL 脱敏：token/access_token/x-access-token 参数值替换为占位符
     *
     * @author scott
     * @since 2026-10-23 请求追踪记录脱敏，避免泄露 X-Access-Token
     */
    private static URI maskUri(URI uri) {
        if (uri == null || uri.getRawQuery() == null) {
            return uri;
        }
        try {
            return URI.create(SENSITIVE_QUERY.matcher(uri.toString()).replaceAll("$1" + MASK));
        } catch (IllegalArgumentException e) {
            //替换后无法解析时，宁可丢掉查询参数也不记录原值
            return URI.create(uri.toString().split("\\?", 2)[0]);
        }
    }

    public List<HttpExchange> findAll(String query) {
        List<HttpExchange> allTrace = super.findAll();
        if (null != allTrace && !allTrace.isEmpty()) {
            Stream<HttpExchange> stream = allTrace.stream();
            String[] params = query.split(",");
            stream = filter(params, stream);
            stream = sort(params, stream);
            allTrace = stream.collect(Collectors.toList());
        }
        return allTrace;
    }

    private Stream<HttpExchange> sort(String[] params, Stream<HttpExchange> stream) {
        if (params.length < 2) {
            return stream;
        }
        String sortBy = params[1];
        String order;
        if (params.length > 2) {
            order = params[2];
        } else {
            order = "desc";
        }
        return stream.sorted((o1, o2) -> {
            int i = 0;
            if("timeTaken".equalsIgnoreCase(sortBy)) {
                i = o1.getTimeTaken().compareTo(o2.getTimeTaken());
            }else if("timestamp".equalsIgnoreCase(sortBy)){
                i = o1.getTimestamp().compareTo(o2.getTimestamp());
            }
            if("desc".equalsIgnoreCase(order)){
                i *=-1;
            }
            return i;
        });
    }

    private static Stream<HttpExchange> filter(String[] params, Stream<HttpExchange> stream) {
        if (params.length == 0) {
            return stream;
        }
        String statusQuery = params[0];
        if (null != statusQuery && !statusQuery.isEmpty()) {
            statusQuery = statusQuery.toLowerCase().trim();
            switch (statusQuery) {
                case "error":
                    stream = stream.filter(httpTrace -> {
                        int status = httpTrace.getResponse().getStatus();
                        return status >= 404 && status < 501;
                    });
                    break;
                case "warn":
                    stream = stream.filter(httpTrace -> {
                        int status = httpTrace.getResponse().getStatus();
                        return status >= 201 && status < 404;
                    });
                    break;
                case "success":
                    stream = stream.filter(httpTrace -> {
                        int status = httpTrace.getResponse().getStatus();
                        return status == 200;
                    });
                    break;
                case "all":
                default:
                    break;
            }
            return stream;
        }
        return stream;
    }

}
