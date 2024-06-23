package org.jeecg.modules.monitor.actuator.httptrace;

import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description: 自定义内存请求追踪存储
 * @Author: chenrui
 * @Date: 2024/5/13 17:02
 */
public class CustomInMemoryHttpTraceRepository extends InMemoryHttpTraceRepository {

    @Override
    public List<HttpTrace> findAll() {
        return super.findAll();
    }

    public List<HttpTrace> findAll(String query) {
        List<HttpTrace> allTrace = super.findAll();
        if (null != allTrace && !allTrace.isEmpty()) {
            Stream<HttpTrace> stream = allTrace.stream();
            String[] params = query.split(",");
            stream = filter(params, stream);
            stream = sort(params, stream);
            allTrace = stream.collect(Collectors.toList());
        }
        return allTrace;
    }

    private Stream<HttpTrace> sort(String[] params, Stream<HttpTrace> stream) {
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

    private static Stream<HttpTrace> filter(String[] params, Stream<HttpTrace> stream) {
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
