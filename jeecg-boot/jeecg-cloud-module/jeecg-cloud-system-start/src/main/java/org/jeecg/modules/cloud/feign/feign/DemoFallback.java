package org.jeecg.modules.cloud.feign.feign;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author qinfeng
 */
@Component
public class DemoFallback implements FallbackFactory<JeecgTestClient> {

    @Override
    public JeecgTestClient create(Throwable throwable) {
        return null;
    }
}
