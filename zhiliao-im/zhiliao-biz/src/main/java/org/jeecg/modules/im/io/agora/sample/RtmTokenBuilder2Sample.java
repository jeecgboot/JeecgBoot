package org.jeecg.modules.im.io.agora.sample;


import org.jeecg.modules.im.io.agora.rtm.RtmTokenBuilder2;

public class RtmTokenBuilder2Sample {
    private static String appId = "970CA35de60c44645bbae8a215061b33";
    private static String appCertificate = "5CFd2fd1755d40ecb72977518be15d3b";
    private static String userId = "test_user_id";
    private static int expirationInSeconds = 3600;

    public static void main(String[] args) {
        RtmTokenBuilder2 token = new RtmTokenBuilder2();
        String result = token.buildToken(appId, appCertificate, userId, expirationInSeconds);
        System.out.println("Rtm Token: " + result);
    }
}
