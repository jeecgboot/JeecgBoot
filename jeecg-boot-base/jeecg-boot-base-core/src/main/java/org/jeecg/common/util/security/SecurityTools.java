package org.jeecg.common.util.security;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.symmetric.AES;
import org.jeecg.common.util.security.entity.*;
import com.alibaba.fastjson.JSONObject;
import javax.crypto.SecretKey;
import java.security.KeyPair;

/**
 * @Description: SecurityTools
 * @author: jeecg-boot
 */
public class SecurityTools {
    public static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    public static SecurityResp valid(SecurityReq req) {
        SecurityResp resp=new SecurityResp();
        String pubKey=req.getPubKey();
        String aesKey=req.getAesKey();
        String data=req.getData();
        String signData=req.getSignData();
        RSA rsa=new RSA(null, Base64Decoder.decode(pubKey));
        Sign sign= new Sign(SignAlgorithm.SHA1withRSA,null,pubKey);



        byte[] decryptAes = rsa.decrypt(aesKey, KeyType.PublicKey);
        //log.info("rsa解密后的秘钥"+ Base64Encoder.encode(decryptAes));
        AES aes = SecureUtil.aes(decryptAes);

        String dencrptValue =aes.decryptStr(data);
        //log.info("解密后报文"+dencrptValue);
        resp.setData(JSONObject.parseObject(dencrptValue));

        boolean verify = sign.verify(dencrptValue.getBytes(), Base64Decoder.decode(signData));
        resp.setSuccess(verify);
        return resp;
    }

    public static SecuritySignResp sign(SecuritySignReq req) {
        SecretKey secretKey = SecureUtil.generateKey(ALGORITHM);
        byte[] key= secretKey.getEncoded();
        String prikey=req.getPrikey();
        String data=req.getData();

        AES aes = SecureUtil.aes(key);
        aes.getSecretKey().getEncoded();
        String encrptData =aes.encryptBase64(data);
        RSA rsa=new RSA(prikey,null);
        byte[] encryptAesKey = rsa.encrypt(secretKey.getEncoded(), KeyType.PrivateKey);
        //log.info(("rsa加密过的秘钥=="+Base64Encoder.encode(encryptAesKey));

        Sign sign= new Sign(SignAlgorithm.SHA1withRSA,prikey,null);
        byte[] signed = sign.sign(data.getBytes());

        //log.info(("签名数据===》》"+Base64Encoder.encode(signed));

        SecuritySignResp resp=new SecuritySignResp();
        resp.setAesKey(Base64Encoder.encode(encryptAesKey));
        resp.setData(encrptData);
        resp.setSignData(Base64Encoder.encode(signed));
        return resp;
    }
    public static MyKeyPair generateKeyPair(){
        KeyPair keyPair= SecureUtil.generateKeyPair(SignAlgorithm.SHA1withRSA.getValue(),2048);
        String priKey= Base64Encoder.encode(keyPair.getPrivate().getEncoded());
        String pubkey= Base64Encoder.encode(keyPair.getPublic().getEncoded());
        MyKeyPair resp=new MyKeyPair();
        resp.setPriKey(priKey);
        resp.setPubKey(pubkey);
        return resp;
    }
}
