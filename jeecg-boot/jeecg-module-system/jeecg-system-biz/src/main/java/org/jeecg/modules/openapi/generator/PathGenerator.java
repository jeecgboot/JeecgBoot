package org.jeecg.modules.openapi.generator;

import lombok.experimental.UtilityClass;

import java.util.Random;

/**
 * @date 2024/12/10 10:00
 */
@UtilityClass
public class PathGenerator {

    // Base62字符集
    private static final String BASE62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 生成随机路径
     * @return
     */
    public static String genPath() {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i=0; i<8; i++) {
            result.append(BASE62.charAt(random.nextInt(62)));
        }
        return result.toString();
    }
}
