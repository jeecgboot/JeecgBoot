package org.jeecg.modules.test.feign.factory;




import feign.hystrix.FallbackFactory;
import org.jeecg.modules.test.feign.client.JeecgTestClient;
import org.jeecg.modules.test.feign.fallback.JeecgTestFallback;
import org.springframework.stereotype.Component;

/**
 * @author qinfeng
 */
@Component
public class JeecgTestClientFactory implements FallbackFactory<JeecgTestClient> {

    @Override
    public JeecgTestClient create(Throwable throwable) {
        JeecgTestFallback fallback = new JeecgTestFallback();
        fallback.setCause(throwable);
        return fallback;
    }
}
