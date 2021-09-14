package org.jeecg.common.config.mqtoken;


/**
 * 用户token上下文
 * @author zyf
 */
public class UserTokenContext {

    private static ThreadLocal<String> userToken = new ThreadLocal<String>();

    public UserTokenContext() {
    }

    public static String getToken(){
        return userToken.get();
    }

    public static void setToken(String token){
        userToken.set(token);
    }
}
