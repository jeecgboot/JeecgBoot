package org.jeecg.modules.im.api.fallback;

import org.jeecg.modules.im.api.FeignApi;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author zyf
 */
@Component
public class ApiFallback implements FallbackFactory<FeignApi> {

    @Override
    public FeignApi create(Throwable throwable) {
        return null;
    }
}
