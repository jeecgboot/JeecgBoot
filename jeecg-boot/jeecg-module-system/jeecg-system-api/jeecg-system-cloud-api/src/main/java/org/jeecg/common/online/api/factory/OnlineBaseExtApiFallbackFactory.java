package org.jeecg.common.online.api.factory;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.jeecg.common.online.api.IOnlineBaseExtApi;
import org.jeecg.common.online.api.fallback.OnlineBaseExtApiFallback;
import org.springframework.stereotype.Component;

/**
 * @Description: OnlineBaseExtAPIFallbackFactory
 * @author: jeecg-boot
 */
@Component
public class OnlineBaseExtApiFallbackFactory implements FallbackFactory<IOnlineBaseExtApi> {

    @Override
    public IOnlineBaseExtApi create(Throwable throwable) {
        OnlineBaseExtApiFallback fallback = new OnlineBaseExtApiFallback();
        fallback.setCause(throwable);
        return fallback;
    }
}