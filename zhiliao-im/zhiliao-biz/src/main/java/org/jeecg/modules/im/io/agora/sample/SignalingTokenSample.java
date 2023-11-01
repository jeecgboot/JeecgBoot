package org.jeecg.modules.im.io.agora.sample;

import io.agora.signal.SignalingToken;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class SignalingTokenSample {

    public static void main(String []args) throws NoSuchAlgorithmException{

        String appId = "970ca35de60c44645bbae8a215061b33";
        String certificate = "5cfd2fd1755d40ecb72977518be15d3b";
        String account = "TestAccount";
        //Use the current time plus an available time to guarantee the only time it is obtained
        int expiredTsInSeconds = 1446455471 + (int) (new Date().getTime()/1000l);
        String result = SignalingToken.getToken(appId, certificate, account, expiredTsInSeconds);
        System.out.println(result);

    }
}
