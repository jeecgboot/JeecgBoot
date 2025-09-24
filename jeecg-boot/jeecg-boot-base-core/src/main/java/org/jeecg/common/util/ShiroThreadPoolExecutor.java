package org.jeecg.common.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import java.util.concurrent.*;

/**
 * @date 2025-09-04
 * @author scott
 * 
 * @Description: 支持shiro的API，获取当前登录人方法的线程池
 */
public class ShiroThreadPoolExecutor extends ThreadPoolExecutor {

    public ShiroThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public void execute(Runnable command) {
        Subject subject = SecurityUtils.getSubject();
        SecurityManager securityManager = SecurityUtils.getSecurityManager();
        super.execute(() -> {
            try {
                ThreadContext.bind(securityManager);
                ThreadContext.bind(subject);
                command.run();
            } finally {
                ThreadContext.unbindSubject();
                ThreadContext.unbindSecurityManager();
            }
        });
    }
}