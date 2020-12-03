package org.jeecg.common.system.api.factory;

import feign.hystrix.FallbackFactory;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.api.fallback.SysBaseAPIFallback;
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