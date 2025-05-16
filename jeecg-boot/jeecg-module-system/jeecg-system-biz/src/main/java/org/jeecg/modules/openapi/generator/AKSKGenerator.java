package org.jeecg.modules.openapi.generator;

import java.security.SecureRandom;

/**
 * AK/SK生成器
 */
public class AKSKGenerator {
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int AK_LENGTH = 16; // Adjust as per requirements
    private static final int SK_LENGTH = 32;

    public static String[] genAKSKPair() {
        return new String[]{genAK(), genSK()};
    }

    public static String genAK() {
        return "ak-" + generateRandomString(AK_LENGTH);
    }

    public static String genSK() {
        return generateRandomString(SK_LENGTH);
    }


    private static String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }

        return sb.toString();
    }
}
