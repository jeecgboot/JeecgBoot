package org.jeecg.common.bpm.api.factory;

import org.jeecg.common.bpm.api.IBpmBaseExtAPI;
import org.jeecg.common.bpm.api.fallback.BpmBaseExtAPIFallback;
import org.springframework.stereotype.Component;

import feign.hystrix.FallbackFactory;

@Component
public class BpmBaseExtAPIFallbackFactory implements FallbackFactory<IBpmBaseExtAPI> {

    @Override
    public IBpmBaseExtAPI create(Throwable throwable) {
        BpmBaseExtAPIFallback fallback = new BpmBaseExtAPIFallback();
        fallback.setCause(throwable);
        return fallback;
    }
}