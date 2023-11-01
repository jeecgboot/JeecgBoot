package org.jeecg.modules.im.io.agora.education;

import io.agora.media.Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.jeecg.modules.im.io.agora.media.AccessToken2;

public class EducationTokenBuilder2 {

    /**
     * build user room token
     *
     * @param appId          The App ID issued to you by Agora. Apply for a new App ID from
     *                       Agora Dashboard if it is missing from your kit. See Get an App ID.
     * @param appCertificate Certificate of the application that you registered in
     *                       the Agora Dashboard. See Get an App Certificate.
     * @param roomUuid       The room's id, must be unique.
     * @param userUuid       The user's id, must be unique.
     * @param role           The user's role, such as 0(invisible), 1(teacher), 2(student), 3(assistant), 4(observer) etc.
     * @param expire         represented by the number of seconds elapsed since now. If, for example, you want to access the
     *                       Agora Service within 10 minutes after the token is generated, set expireTimestamp as 600(seconds).
     * @return The education user room token.
     */
    public String buildRoomUserToken(String appId, String appCertificate, String roomUuid, String userUuid, Short role, int expire) {
        AccessToken2 accessToken = new AccessToken2(appId, appCertificate, expire);
        String chatUserId = DigestUtils.md5Hex(userUuid);

        AccessToken2.Service serviceEducation = new AccessToken2.ServiceEducation(roomUuid, userUuid, role);
        serviceEducation.addPrivilegeEducation(AccessToken2.PrivilegeEducation.PRIVILEGE_ROOM_USER, expire);
        accessToken.addService(serviceEducation);

        AccessToken2.Service serviceRtm = new AccessToken2.ServiceRtm(userUuid);
        serviceRtm.addPrivilegeRtm(AccessToken2.PrivilegeRtm.PRIVILEGE_LOGIN, expire);
        accessToken.addService(serviceRtm);

        AccessToken2.Service serviceChat = new AccessToken2.ServiceChat(chatUserId);
        serviceRtm.addPrivilegeChat(AccessToken2.PrivilegeChat.PRIVILEGE_CHAT_USER, expire);
        accessToken.addService(serviceChat);

        try {
            return accessToken.build();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * build user individual token
     *
     * @param appId          The App ID issued to you by Agora. Apply for a new App ID from
     *                       Agora Dashboard if it is missing from your kit. See Get an App ID.
     * @param appCertificate Certificate of the application that you registered in
     *                       the Agora Dashboard. See Get an App Certificate.
     * @param userUuid       The user's id, must be unique.
     * @param expire         represented by the number of seconds elapsed since now. If, for example, you want to access the
     *                       Agora Service within 10 minutes after the token is generated, set expireTimestamp as 600(seconds).
     * @return The education user token.
     */
    public String buildUserToken(String appId, String appCertificate, String userUuid, int expire) {
        AccessToken2 accessToken = new AccessToken2(appId, appCertificate, expire);
        AccessToken2.Service service = new AccessToken2.ServiceEducation(userUuid);

        service.addPrivilegeEducation(AccessToken2.PrivilegeEducation.PRIVILEGE_USER, expire);
        accessToken.addService(service);

        try {
            return accessToken.build();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * build app global token
     *
     * @param appId          The App ID issued to you by Agora. Apply for a new App ID from
     *                       Agora Dashboard if it is missing from your kit. See Get an App ID.
     * @param appCertificate Certificate of the application that you registered in
     *                       the Agora Dashboard. See Get an App Certificate.
     * @param expire         represented by the number of seconds elapsed since now. If, for example, you want to access the
     *                       Agora Service within 10 minutes after the token is generated, set expireTimestamp as 600(seconds).
     * @return The education global token.
     */
    public String buildAppToken(String appId, String appCertificate, int expire) {
        AccessToken2 accessToken = new AccessToken2(appId, appCertificate, expire);
        AccessToken2.Service serviceEducation = new AccessToken2.ServiceEducation();

        serviceEducation.addPrivilegeEducation(AccessToken2.PrivilegeEducation.PRIVILEGE_APP, expire);
        accessToken.addService(serviceEducation);

        try {
            return accessToken.build();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
