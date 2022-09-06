package org.jeecg.modules.monitor.exception;

/**
 * Redis 连接异常
 * @author: jeecg-boot
 */
public class RedisConnectException extends Exception {

    private static final long serialVersionUID = 1639374111871115063L;

    public RedisConnectException(String message) {
        super(message);
    }
}
