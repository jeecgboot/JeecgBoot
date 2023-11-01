package org.jeecg.modules.im.io.agora.sample;


import org.jeecg.modules.im.io.agora.chat.ChatTokenBuilder2;

public class ChatTokenBuilder2Sample {
    private static String appId = "970CA35de60c44645bbae8a215061b33";
    private static String appCertificate = "5CFd2fd1755d40ecb72977518be15d3b";
    private static String userId = "2882341273";
    private static int expire = 600;

    public static void main(String[] args) throws Exception {
    	ChatTokenBuilder2 tokenBuilder = new ChatTokenBuilder2();
        String userToken = tokenBuilder.buildUserToken(appId, appCertificate, userId, expire);
        System.out.printf("Chat user token: %s\n", userToken);
        String appToken = tokenBuilder.buildAppToken(appId, appCertificate, expire);
        System.out.printf("Chat app token: %s\n", appToken);
    }
}
