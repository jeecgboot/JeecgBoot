package org.springframework.base.system.utils;

import java.security.MessageDigest;

public class MD5Utils {
    private static final String[] hexDigIts = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public MD5Utils() {
    }

    public static String MD5Encode(String origin) {
        String charsetname = "utf8";
        String resultString = null;

        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname != null && !"".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            }
        } catch (Exception var4) {
        }

        return resultString;
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();

        for (int i = 0; i < b.length; ++i) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if (b < 0) {
            n = b + 256;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigIts[d1] + hexDigIts[d2];
    }

    public static void main(String[] args) throws Exception {
        String personNumber = "e96281f222072f8bf6e730d41b42cb30";
        String company = "福州青格软件有限公司";
        String validToken = MD5Encode(personNumber + company + "treesoft");
        System.out.println("用户名=" + company);
        System.out.println("CODE=" + validToken);
    }
}
