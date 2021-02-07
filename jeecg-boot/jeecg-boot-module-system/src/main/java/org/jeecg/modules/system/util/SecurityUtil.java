package org.jeecg.modules.system.util;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * @Description: 密码加密解密
 * @author: lsq
 * @date: 2020年09月07日 14:26
 */
public class SecurityUtil {
    /**加密key*/
    private static String key = "JEECGBOOT1423670";

    //---AES加密---------begin---------
    /**加密
     * @param content
     * @return
     */
    public static String jiami(String content) {
            SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key.getBytes());
            String encryptResultStr = aes.encryptHex(content);
            return encryptResultStr;
    }

    /**解密
     * @param encryptResultStr
     * @return
     */
    public static String jiemi(String encryptResultStr){
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key.getBytes());
        //解密为字符串
        String decryptResult = aes.decryptStr(encryptResultStr, CharsetUtil.CHARSET_UTF_8);
        return  decryptResult;
    }
    //---AES加密---------end---------
    /**
     * 主函数
     */
    public static void main(String[] args) {
        String content="test1111";
        String encrypt = jiami(content);
        System.out.println(encrypt);
        //构建
        String decrypt = jiemi(encrypt);
        //解密为字符串
        System.out.println(decrypt);
    }
}
