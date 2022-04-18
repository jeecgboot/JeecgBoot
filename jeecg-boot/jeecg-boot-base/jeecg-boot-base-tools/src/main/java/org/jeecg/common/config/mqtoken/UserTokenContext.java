package org.jeecg.common.config.mqtoken;


/**
 * 用户Token线程上下文
 *
 * 供队列、定时任务 feign调用使用（解决无会话Token问题）
 * @author zyf
 */
public class UserTokenContext {

    /**
     * 当前线程的TOKEN副本
     */
    private static ThreadLocal<String> userToken = new ThreadLocal<String>();

    public UserTokenContext() {
    }

    public static String getToken(){
        return userToken.get();
    }

    public static void setToken(String token){
        userToken.set(token);
    }

    public static void remove() {
        userToken.remove();
    }
}
