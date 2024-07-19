package com.treesoft.system.utils;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class CryptoUtil {
    public static Key DEFAULT_KEY = null;

    public static final String DEFAULT_SECRET_KEY1 = "?:P)(OL><KI*&UJMNHY^%TGBVFR$#EDCXSW@!QAZ";

    public static final String DEFAULT_SECRET_KEY2 = "1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik,9ol.0p;/";

    public static final String DEFAULT_SECRET_KEY3 = "!QAZ@WSX#EDC$RFV%TGB^YHN&UJM*IK<(OL>)P:?";

    public static final String DEFAULT_SECRET_KEY4 = "1qaz@WSX3edc$RFV5tgb^YHN7ujm*IK<9ol.)P:?";

    public static final String DEFAULT_SECRET_KEY5 = "!QAZ2wsx#EDC4rfv%TGB6yhn&UJM8ik,(OL>0p;/";

    public static final String DEFAULT_SECRET_KEY6 = "1qaz2wsx3edc4rfv5tgb^YHN&UJM*IK<(OL>)P:?";

    public static final String DEFAULT_SECRET_KEY = "?:P)(OL><KI*&UJMNHY^%TGBVFR$#EDCXSW@!QAZ";

    public static final String DES = "DES";

    public static String encode(String str) {
        try {
            byte[] textByte = str.getBytes(StandardCharsets.UTF_8);
            return new Base64().encodeToString(textByte);
        } catch (Exception e) {
        }
        return "";
    }

    public static String decode(String encodedText) {
        String pass = "";
        try {
            Base64 base64 = new Base64();
            pass = new String(base64.decode(encodedText), StandardCharsets.UTF_8);
        } catch (Exception localException) {
        }
        return pass;
    }

    public static String encode64xx(String key, String str) {
        try {
            return new String(Base64.encodeBase64(str.getBytes("UTF_8")), "UTF_8");
        } catch (Exception e) {
        }
        return "";
    }

    public static String decode64xx(String str) {
        String pass = "";
        try {
            pass = new String(Base64.decodeBase64(str.getBytes("UTF_8")), "UTF_8");
        } catch (Exception localException) {
        }
        return pass;
    }
}
