package org.jeecg.modules.im.base.util;

import org.apache.commons.codec.digest.DigestUtils;

public class ToolPassword {

    public static final String config_password_key = "zhiliaoim";
    /**
     * 验证密码的正确性
     *
     * @param salt        数据库中保存的用户salt值
     * @param encPassword 数据库中保存的加密的密码
     * @param password    用户输入的密码
     * @return
     */
    public static boolean checkPassword(String salt, String encPassword, String password) {
        PasswordEncoder encoder = new PasswordEncoder(salt);
        return encoder.isPasswordValid(encPassword, password);
    }

    /**
     * 加盐密码
     *
     * @param salt
     * @param password
     * @return
     */
    public static String getEncPassword(String salt, String password) {
        PasswordEncoder encoder = new PasswordEncoder(salt);
        return encoder.encode(password);
    }

    public static String getPassword(String password) {
        return DigestUtils.md5Hex(password + config_password_key);
    }
}