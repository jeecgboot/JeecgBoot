package org.jeecg.common.util.encryption;


import lombok.Data;

/**
 * @Description: EncryptedString
 * @author: jeecg-boot
 */
@Data
public class  EncryptedString {

    /**
     * 长度为16个字符
     */
    public static  String key = "1234567890adbcde";

    /**
     * 长度为16个字符
     */
    public static  String iv  = "1234567890hjlkew";
}
