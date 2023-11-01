package org.jeecg.modules.im.io.agora.sample;


import org.jeecg.modules.im.io.agora.media.FpaTokenBuilder;

public class FpaTokenBuilderSample {
    static String appId = "970CA35de60c44645bbae8a215061b33";
    static String appCertificate = "5CFd2fd1755d40ecb72977518be15d3b";

    public static void main(String[] args) {
        FpaTokenBuilder token = new FpaTokenBuilder();
        String result = token.buildToken(appId, appCertificate);
        System.out.println("Token with FPA service: " + result);
    }
}
