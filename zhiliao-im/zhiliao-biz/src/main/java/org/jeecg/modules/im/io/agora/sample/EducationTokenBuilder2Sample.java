package org.jeecg.modules.im.io.agora.sample;


import org.jeecg.modules.im.io.agora.education.EducationTokenBuilder2;

public class EducationTokenBuilder2Sample {
    private static String appId = "970CA35de60c44645bbae8a215061b33";
    private static String appCertificate = "5CFd2fd1755d40ecb72977518be15d3b";
    private static String roomUuid = "123";
    private static String userUuid = "2882341273";
    private static Short role = 1;
    private static int expire = 600;

    public static void main(String[] args) {
        EducationTokenBuilder2 tokenBuilder = new EducationTokenBuilder2();
        String roomUserToken = tokenBuilder.buildRoomUserToken(appId, appCertificate, roomUuid, userUuid, role, expire);
        System.out.printf("Education room user token: %s\n", roomUserToken);

        String userToken = tokenBuilder.buildUserToken(appId, appCertificate, userUuid, expire);
        System.out.printf("Education user token: %s\n", userToken);

        String appToken = tokenBuilder.buildAppToken(appId, appCertificate, expire);
        System.out.printf("Education app token: %s\n", appToken);
    }
}
