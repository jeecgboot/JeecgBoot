package org.jeecg.modules.im.io.agora.media;

public class FpaTokenBuilder {
    /**
     * Build the FPA token.
     *
     * @param appId:          The App ID issued to you by Agora. Apply for a new App ID from
     *                        Agora Dashboard if it is missing from your kit. See Get an App ID.
     * @param appCertificate: Certificate of the application that you registered in
     *                        the Agora Dashboard. See Get an App Certificate.
     * @return The FPA token.
     */
    public String buildToken(String appId, String appCertificate) {
        AccessToken2 accessToken = new AccessToken2(appId, appCertificate, 24 * 3600);
        AccessToken2.Service serviceFpa = new AccessToken2.ServiceFpa();

        serviceFpa.addPrivilegeFpa(AccessToken2.PrivilegeFpa.PRIVILEGE_LOGIN, 0);
        accessToken.addService(serviceFpa);

        try {
            return accessToken.build();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
