package org.jeecg.common.util;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.concurrent.*;

/**
 * @date 2025-09-04
 * @author scott
 *
 * @Description: 支持Spring Security的API，获取当前登录人方法的线程池
 */
public class ShiroThreadPoolExecutor extends ThreadPoolExecutor {

    public ShiroThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public void execute(Runnable command) {
        SecurityContext context = SecurityContextHolder.getContext();
        super.execute(() -> {
            SecurityContext previousContext = SecurityContextHolder.getContext();
            try {
                SecurityContextHolder.setContext(context);
                command.run();
            } finally {
                SecurityContextHolder.setContext(previousContext);
            }
        });
    }
}