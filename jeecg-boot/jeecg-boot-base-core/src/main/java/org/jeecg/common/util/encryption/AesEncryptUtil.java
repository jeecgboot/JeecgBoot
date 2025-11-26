package org.jeecg.common.util.encryption;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.lang.codec.Base64;
import org.jeecg.common.util.oConvertUtils;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES 工具 (兼容历史 NoPadding + 新 PKCS5Padding)
 */
@Slf4j
public class AesEncryptUtil {

    private static final String KEY = EncryptedString.key;
    private static final String IV  = EncryptedString.iv;

    /* -------- 新版：CBC + PKCS5Padding (与前端 CryptoJS Pkcs7 兼容) -------- */
    private static String decryptPkcs5(String cipherBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec ks = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, ks, ivSpec);
        byte[] plain = cipher.doFinal(Base64.decode(cipherBase64));
        return new String(plain, StandardCharsets.UTF_8);
    }

    /* -------- 旧版：CBC + NoPadding (手工补 0) -------- */
    private static String decryptLegacyNoPadding(String cipherBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec ks = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, ks, ivSpec);
        byte[] data = cipher.doFinal(Base64.decode(cipherBase64));
        return new String(data, StandardCharsets.UTF_8)
                .replace("\u0000",""); // 旧填充 0
    }

    /* -------- 兼容入口：登录使用 -------- */
    public static String resolvePassword(String input){
        if(oConvertUtils.isEmpty(input)){
            return input;
        }
        // 1. 先尝试新版
        try{
            String p = decryptPkcs5(input);
            return clean(p);
        }catch(Exception ignore){
            //log.debug("【AES解密】Password not AES PKCS5 cipher, try legacy.");
        }

        // 2. 回退旧版
        try{
            String legacy = decryptLegacyNoPadding(input);
            return clean(legacy);
        }catch(Exception e){
            log.debug("【AES解密】Password not AES cipher, raw used.");
        }

        // 3. 视为明文
        return input;
    }

    /* -------- 可选：统一清理尾部不可见控制字符 -------- */
    private static String clean(String s){
        if(s==null) return null;
        // 去除结尾控制符/空白（不影响中间合法空格）
        return s.replaceAll("[\\p{Cntrl}]+","").trim();
    }

    /* -------- 若仍需要旧接口，可保留 (不建议再用于新前端) -------- */
    @Deprecated
    public static String desEncrypt(String data) throws Exception {
        return decryptLegacyNoPadding(data);
    }

    /* 加密（若前端不再使用，可忽略；保留旧实现避免影响历史） */
    @Deprecated
    public static String encrypt(String data) throws Exception {
        try{
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength += (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return Base64.encodeToString(encrypted);
        }catch(Exception e){
            throw new IllegalStateException("legacy encrypt error", e);
        }
    }

//    public static void main(String[] args) throws Exception {
//        // 前端 CBC/Pkcs7 密文测试
//        String frontCipher = encrypt("sa"); // 仅验证管道是否可用（旧方式）
//        System.out.println(resolvePassword(frontCipher));
//    }
}