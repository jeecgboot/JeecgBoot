package org.jeecg.modules.monitor.actuator.httptrace;

import lombok.Getter;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

import static org.springframework.boot.actuate.endpoint.annotation.Selector.Match.ALL_REMAINING;

/**
 * @Description: ENDPOINT: 请求追踪(新),支持通过responseCode筛选
 * @Author: chenrui
 * @Date: 2024/5/13 17:02
 */
@Component
@Endpoint(id = "httptrace-new")
public class CustomHttpTraceEndpoint{
    private final CustomInMemoryHttpTraceRepository repository;

    public CustomHttpTraceEndpoint(CustomInMemoryHttpTraceRepository repository) {
        Assert.notNull(repository, "Repository must not be null");
        this.repository = repository;
    }

    @ReadOperation
    public HttpTraceDescriptor traces(@Selector(match = ALL_REMAINING) String query) {
        return new CustomHttpTraceEndpoint.HttpTraceDescriptor(this.repository.findAll(query));
    }

    @Getter
    public static final class HttpTraceDescriptor {
        private final List<HttpTrace> traces;

        private HttpTraceDescriptor(List<HttpTrace> traces) {
            this.traces = traces;
        }

    }
}
