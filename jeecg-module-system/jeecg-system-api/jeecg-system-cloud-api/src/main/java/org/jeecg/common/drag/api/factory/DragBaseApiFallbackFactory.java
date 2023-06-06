package org.jeecg.common.drag.api.factory;

import org.jeecg.common.drag.api.IDragBaseApi;
import org.jeecg.common.drag.api.fallbak.DragBaseApiFallback;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * DragBaseApiFallbackFactory
 * @author: lsq
 * @date 2023/01/09
 */
@Component
public class DragBaseApiFallbackFactory implements FallbackFactory<IDragBaseApi> {


    @Override
    public IDragBaseApi create(Throwable throwable) {
        DragBaseApiFallback fallback = new DragBaseApiFallback();
        fallback.setCause(throwable);
        return fallback;
    }
}