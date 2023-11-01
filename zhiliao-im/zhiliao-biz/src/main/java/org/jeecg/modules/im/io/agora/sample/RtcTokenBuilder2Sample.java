package org.jeecg.modules.im.io.agora.sample;

import org.jeecg.modules.im.io.agora.media.RtcTokenBuilder2;
import org.jeecg.modules.im.io.agora.media.RtcTokenBuilder2.Role;

public class RtcTokenBuilder2Sample {
    static String appId = "4ebab8b6d5e24db5819c0f8806fcab70";
    static String appCertificate = "40881d20fa2c42e68eee5a35491a234d";
    static String channelName = "7d72365eb983485397e3e3f9d460bdda";
    static String account = "2082341273";
    static int uid = 2082341273;
    static int tokenExpirationInSeconds = 3600;
    static int privilegeExpirationInSeconds = 3600;

    public static void main(String[] args) {
        RtcTokenBuilder2 token = new RtcTokenBuilder2();
        String result = token.buildTokenWithUid(appId, appCertificate, channelName, uid, Role.ROLE_SUBSCRIBER, tokenExpirationInSeconds, privilegeExpirationInSeconds);
        System.out.println("Token with uid: " + result);

        result = token.buildTokenWithUserAccount(appId, appCertificate, channelName, account, Role.ROLE_SUBSCRIBER, tokenExpirationInSeconds, privilegeExpirationInSeconds);
        System.out.println("Token with account: " + result);

        result = token.buildTokenWithUid(appId, appCertificate, channelName, uid, privilegeExpirationInSeconds, privilegeExpirationInSeconds, privilegeExpirationInSeconds, privilegeExpirationInSeconds, privilegeExpirationInSeconds);
        System.out.println("Token with uid and privilege: " + result);

        result = token.buildTokenWithUserAccount(appId, appCertificate, channelName, account, privilegeExpirationInSeconds, privilegeExpirationInSeconds, privilegeExpirationInSeconds, privilegeExpirationInSeconds, privilegeExpirationInSeconds);
        System.out.println("Token with account and privilege: " + result);
    }
}
