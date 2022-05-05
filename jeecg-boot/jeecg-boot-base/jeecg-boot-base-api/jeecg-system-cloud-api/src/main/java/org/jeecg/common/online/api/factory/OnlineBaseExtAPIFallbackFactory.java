package org.jeecg.common.online.api.factory;

import feign.hystrix.FallbackFactory;
import org.jeecg.common.online.api.IOnlineBaseExtAPI;
import org.jeecg.common.online.api.fallback.OnlineBaseExtAPIFallback;
import org.springframework.stereotype.Component;

@Component
public class OnlineBaseExtAPIFallbackFactory implements FallbackFactory<IOnlineBaseExtAPI> {

    @Override
    public IOnlineBaseExtAPI create(Throwable throwable) {
        OnlineBaseExtAPIFallback fallback = new OnlineBaseExtAPIFallback();
        fallback.setCause(throwable);
        return fallback;
    }
}