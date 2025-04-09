package org.jeecg.modules.monitor.actuator.undertow;

import io.micrometer.core.instrument.MeterRegistry;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.session.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * 自定义undertow监控指标工具类
 * for [QQYUN-11902]tomcat 替换undertow 这里的功能还没修改
 * @author chenrui
 * @date 2025/4/8 19:06
 */
@Component("jeecgCustomUndertowMetricsHandler")
public class CustomUndertowMetricsHandler {

    // 用于统计已创建的 session 数量
    private final LongAdder sessionsCreated = new LongAdder();

    // 用于统计已销毁的 session 数量
    private final LongAdder sessionsExpired = new LongAdder();

    // 当前活跃的 session 数量
    private final AtomicInteger activeSessions = new AtomicInteger();

    // 历史最大活跃 session 数
    private final AtomicInteger maxActiveSessions = new AtomicInteger();

    // Undertow 内存 session 管理器（用于创建与管理 session）
    private final InMemorySessionManager sessionManager = new InMemorySessionManager("undertow-session-manager");

    // 使用 Cookie 存储 session ID
    private final SessionConfig sessionConfig = new SessionCookieConfig();

    /**
     * 构造函数
     * @param meterRegistry
     * @author chenrui
     * @date 2025/4/8 19:07
     */
    public CustomUndertowMetricsHandler(MeterRegistry meterRegistry) {
        // 注册 Micrometer 指标
        meterRegistry.gauge("undertow.sessions.created", sessionsCreated, LongAdder::longValue);
        meterRegistry.gauge("undertow.sessions.expired", sessionsExpired, LongAdder::longValue);
        meterRegistry.gauge("undertow.sessions.active.current", activeSessions, AtomicInteger::get);
        meterRegistry.gauge("undertow.sessions.active.max", maxActiveSessions, AtomicInteger::get);

        // 添加 session 生命周期监听器，统计 session 创建与销毁
        sessionManager.registerSessionListener(new SessionListener() {
            @Override
            public void sessionCreated(Session session, HttpServerExchange exchange) {
                sessionsCreated.increment();
                int now = activeSessions.incrementAndGet();
                maxActiveSessions.getAndUpdate(max -> Math.max(max, now));
            }

            @Override
            public void sessionDestroyed(Session session, HttpServerExchange exchange, SessionDestroyedReason reason) {
                sessionsExpired.increment();
                activeSessions.decrementAndGet();
            }
        });
    }

    /**
     * 包装 Undertow 的 HttpHandler，实现 session 自动创建逻辑
     * @param next
     * @return
     * @author chenrui
     * @date 2025/4/8 19:07
     */
    public HttpHandler wrap(HttpHandler next) {
        return exchange -> {
            // 获取当前 session，如果不存在则创建
            Session session = sessionManager.getSession(exchange, sessionConfig);
            if (session == null) {
                sessionManager.createSession(exchange, sessionConfig);
            }

            // 执行下一个 Handler
            next.handleRequest(exchange);
        };
    }
}