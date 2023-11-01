package org.jeecg.modules.im.base.util;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.Random;

public class PasswordEncoder {

    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private Object salt;
    private String algorithm = "MD5";

    /**
     * 加密构造方法
     * @param salt 传入盐值
     */
    public PasswordEncoder(Object salt) {
        this.salt = salt;
    }

    /**
     * 加密方法
     * @param rawPass 传入要加密的密码
     * @return
     */
    public String encode(String rawPass) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            // 加密后的字符串
            result = byteArrayToHexString(md.digest(mergePasswordAndSalt(
                    rawPass).getBytes("utf-8")));
        } catch (Exception ex) {
        }
        return result;
    }

    /**
     * 验证密码是否正确
     * @param encPass
     * @param rawPass
     * @return
     */
    public boolean isPasswordValid(String encPass, String rawPass) {
        return StringUtils.equals(encPass,encode(rawPass));
    }


    /**
     * 创建盐值
     * @param length 盐值位数
     * @return
     */
    public static String createSalt(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    private String mergePasswordAndSalt(String password) {
        if (password == null) {
            password = "";
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param b
     *            字节数组
     * @return 16进制字串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

}