package org.jeecg.common.util.encryption;

import org.apache.shiro.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description: AES 加密
 * @author: jeecg-boot
 * @date: 2022/3/30 11:48
 */
public class AesEncryptUtil {

    /**
     * 使用AES-128-CBC加密模式 key和iv可以相同
     */
    private static String KEY = EncryptedString.key;
    private static String IV = EncryptedString.iv;

    /**
     * 加密方法
     * @param data  要加密的数据
     * @param key 加密key
     * @param iv 加密iv
     * @return 加密的结果
     * @throws Exception
     */
    public static String encrypt(String data, String key, String iv) throws Exception {
        try {

            //"算法/模式/补码方式"NoPadding PkcsPadding
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return Base64.encodeToString(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密方法
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv 解密iv
     * @return 解密的结果
     * @throws Exception
     */
    public static String desEncrypt(String data, String key, String iv) throws Exception {
        //update-begin-author:taoyan date:2022-5-23 for:VUEN-1084 【vue3】online表单测试发现的新问题 6、解密报错 ---解码失败应该把异常抛出去，在外面处理
        byte[] encrypted1 = Base64.decode(data);

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        //加密解码后的字符串会出现\u0000
        return originalString.replaceAll("\\u0000", "");
        //update-end-author:taoyan date:2022-5-23 for:VUEN-1084 【vue3】online表单测试发现的新问题 6、解密报错 ---解码失败应该把异常抛出去，在外面处理
    }

    /**
     * 使用默认的key和iv加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        return encrypt(data, KEY, IV);
    }

    /**
     * 使用默认的key和iv解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String desEncrypt(String data) throws Exception {
        return desEncrypt(data, KEY, IV);
    }



//    /**
//     * 测试
//     */
//    public static void main(String args[]) throws Exception {
//        String test1 = "sa";
//        String test =new String(test1.getBytes(),"UTF-8");
//        String data = null;
//        String key =  KEY;
//        String iv = IV;
//        // /g2wzfqvMOeazgtsUVbq1kmJawROa6mcRAzwG1/GeJ4=
//        data = encrypt(test, key, iv);
//        System.out.println("数据："+test);
//        System.out.println("加密："+data);
//        String jiemi =desEncrypt(data, key, iv).trim();
//        System.out.println("解密："+jiemi);
//    }

}
