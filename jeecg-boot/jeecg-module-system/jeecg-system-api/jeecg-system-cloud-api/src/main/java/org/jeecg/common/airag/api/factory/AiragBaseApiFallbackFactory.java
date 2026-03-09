package org.jeecg.common.airag.api.factory;

import org.jeecg.common.airag.api.IAiragBaseApi;
import org.jeecg.common.airag.api.fallback.AiragBaseApiFallback;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AiragBaseApiFallbackFactory implements FallbackFactory<IAiragBaseApi> {

    @Override
    public IAiragBaseApi create(Throwable cause) {
        AiragBaseApiFallback fallback = new AiragBaseApiFallback();
        fallback.setCause(cause);
        return fallback;
    }

}
