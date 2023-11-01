package org.jeecg.modules.im.base.util;

import java.util.Random;

public class NickNameTool {
    public static String genarate(){
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        random(builder, random);
        return builder.toString();
    }

    public static String genarateV2(String wechatNickname){
        int length = wechatNickname.length();
        StringBuilder builder = new StringBuilder((length>6)?wechatNickname.substring(0,6):wechatNickname.substring(0,length)+"_");
        Random random = new Random();
        random(builder, random);
        return builder.toString();
    }

    private static void random(StringBuilder builder, Random random) {
        for (int i = 0; i < 8; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                builder.append((char) (choice + random.nextInt(26)));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                builder.append(String.valueOf(random.nextInt(10)));
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(genarateV2("zl_18250800255"));
    }
}