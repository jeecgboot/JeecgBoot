package org.jeecg.modules.im.base.util;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

/**
 *  * @Description: aes对称加密解密工具类, 注意密钥不能随机生机, 不同客户端调用可能需要考虑不同Provider,
 *  * 考虑安卓与IOS不同平台复杂度,简化不使用Provider
 */
public class AESUtils {

    /***默认向量常量**/
    public static final String IV = "1234567890123456";
    private final static Logger logger = LoggerFactory.getLogger(AESUtils.class);

    /**
     * 使用PKCS7Padding填充必须添加一个支持PKCS7Padding的Provider
     * 类加载的时候就判断是否已经有支持256位的Provider,如果没有则添加进去
     */
    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * 加密 128位
     *
     * @param content 需要加密的原内容
     * @param pkey    密匙
     * @return
     */
    public static byte[] aesEncrypt(String content, String pkey) {
        try {
            SecretKeySpec skey = new SecretKeySpec(pkey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");// "算法/加密/填充"
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skey, iv);//初始化加密器
            return cipher.doFinal(content.getBytes("UTF-8")); // 加密
        } catch (Exception e) {
            logger.info("aesEncrypt() method error:", e);
        }
        return null;
    }

    /**
     * 获得密钥
     *
     * @param secretKey
     * @return
     * @throws Exception
     */
    private static SecretKey generateKey(String secretKey) throws Exception {
        //防止linux下 随机生成key
        Provider p = Security.getProvider("SUN");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", p);
        secureRandom.setSeed(secretKey.getBytes());
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(secureRandom);
        // 生成密钥
        return kg.generateKey();
    }

    /**
     * @param content 加密前原内容
     * @param pkey    长度为16个字符,128位
     * @return base64EncodeStr   aes加密完成后内容
     * @throws
     * @Title: aesEncryptStr
     * @Description: aes对称加密
     */
    public static String aesEncryptStr(String content, String pkey) {
        return Base64.encodeBase64String(aesEncrypt(content, pkey));
    }

    /**
     * @param content base64处理过的字符串
     * @param pkey    密匙
     * @return String    返回类型
     * @throws Exception
     * @throws
     * @Title: aesDecodeStr
     * @Description: 解密 失败将返回NULL
     */
    public static String aesDecodeStr(String content, String pkey) throws Exception {
        try {
            byte[] base64DecodeStr = Base64.decodeBase64(content);
            byte[] aesDecode = aesDecode(base64DecodeStr, pkey);
            if (aesDecode == null) {
                return null;
            }
            String result;
            result = new String(aesDecode, "UTF-8");
            return result;
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            throw new Exception("解密异常");
        }
    }

    /**
     * 解密 128位
     *
     * @param content 解密前的byte数组
     * @param pkey    密匙
     * @return result  解密后的byte数组
     * @throws Exception
     */
    public static byte[] aesDecode(byte[] content, String pkey) throws Exception {

        SecretKeySpec skey = new SecretKeySpec(pkey.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, skey, iv);// 初始化解密器
        return cipher.doFinal(content); // 解密

    }


    public static void main(String[] args) throws Exception {
        //明文
        String content = "1614354000818:a6371229-f2f3-4a67-bc46-1785774e7daf:你妈贵姓";
        //密匙
        String pkey = "12345678901234567890123456789012";
        System.out.println("待加密报文:" + content);
        System.out.println("密匙:" + pkey);
        String aesEncryptStr = aesEncryptStr(content, pkey);
        System.out.println("加密报文:" + aesEncryptStr);
        String aesDecodeStr = aesDecodeStr(aesEncryptStr, pkey);
        System.out.println("解密报文:" + aesDecodeStr);
        System.out.println("加解密前后内容是否相等:" + aesDecodeStr.equals(content));
    }

}