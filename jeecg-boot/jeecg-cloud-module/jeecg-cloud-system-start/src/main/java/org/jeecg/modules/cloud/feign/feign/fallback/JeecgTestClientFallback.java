package org.jeecg.modules.cloud.feign.feign.fallback;

import feign.hystrix.FallbackFactory;
import org.jeecg.modules.cloud.feign.feign.JeecgTestClient;
import org.springframework.stereotype.Component;

/**
 * @author qinfeng
 */
@Component
public class JeecgTestClientFallback implements FallbackFactory<JeecgTestClient> {

    @Override
    public JeecgTestClient create(Throwable throwable) {
        return null;
    }
}
