package org.jeecg.modules.monitor.actuator.httptrace;

import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;

import java.util.List;
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
            super.add(trace);
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
