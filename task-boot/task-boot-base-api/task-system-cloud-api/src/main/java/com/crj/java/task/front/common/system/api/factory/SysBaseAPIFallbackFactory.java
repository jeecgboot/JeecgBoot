package com.crj.java.task.front.common.system.api.factory;

import feign.hystrix.FallbackFactory;
import com.crj.java.task.front.common.system.api.ISysBaseAPI;
import com.crj.java.task.front.common.system.api.fallback.SysBaseAPIFallback;
import org.springframework.stereotype.Component;

@Component
public class SysBaseAPIFallbackFactory implements FallbackFactory<ISysBaseAPI> {

    @Override
    public ISysBaseAPI create(Throwable throwable) {
        SysBaseAPIFallback fallback = new SysBaseAPIFallback();
        fallback.setCause(throwable);
        return fallback;
    }
}
