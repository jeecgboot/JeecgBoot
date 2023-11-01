package org.jeecg.modules.im.io.agora.media;

public class RtcTokenBuilder2 {
    public enum Role {
        ROLE_PUBLISHER(1),
        ROLE_SUBSCRIBER(2),
        ;

        public int initValue;

        Role(int initValue) {
            this.initValue = initValue;
        }
    }

    /**
     * Build the RTC token with uid.
     *
     * @param appId:            The App ID issued to you by Agora. Apply for a new App ID from
     *                          Agora Dashboard if it is missing from your kit. See Get an App ID.
     * @param appCertificate:   Certificate of the application that you registered in
     *                          the Agora Dashboard. See Get an App Certificate.
     * @param channelName:      Unique channel name for the AgoraRTC session in the string format
     * @param uid:              User ID. A 32-bit unsigned integer with a value ranging from 1 to (2^32-1).
     *                          optionalUid must be unique.
     * @param role:             ROLE_PUBLISHER: A broadcaster/host in a live-broadcast profile.
     *                          ROLE_SUBSCRIBER: An audience(default) in a live-broadcast profile.
     * @param token_expire:     represented by the number of seconds elapsed since now. If, for example,
     *                          you want to access the Agora Service within 10 minutes after the token is generated,
     *                          set token_expire as 600(seconds).
     * @param privilege_expire: represented by the number of seconds elapsed since now. If, for example,
     *                          you want to enable your privilege for 10 minutes, set privilege_expire as 600(seconds).
     * @return The RTC token.
     */
    public String buildTokenWithUid(String appId, String appCertificate, String channelName, int uid, Role role, int token_expire, int privilege_expire) {
        return buildTokenWithUserAccount(appId, appCertificate, channelName, AccessToken2.getUidStr(uid), role, token_expire, privilege_expire);
    }

    /**
     * Build the RTC token with account.
     *
     * @param appId:            The App ID issued to you by Agora. Apply for a new App ID from
     *                          Agora Dashboard if it is missing from your kit. See Get an App ID.
     * @param appCertificate:   Certificate of the application that you registered in
     *                          the Agora Dashboard. See Get an App Certificate.
     * @param channelName:      Unique channel name for the AgoraRTC session in the string format
     * @param account:          The user's account, max length is 255 Bytes.
     * @param role:             ROLE_PUBLISHER: A broadcaster/host in a live-broadcast profile.
     *                          ROLE_SUBSCRIBER: An audience(default) in a live-broadcast profile.
     * @param token_expire:     represented by the number of seconds elapsed since now. If, for example,
     *                          you want to access the Agora Service within 10 minutes after the token is generated,
     *                          set token_expire as 600(seconds).
     * @param privilege_expire: represented by the number of seconds elapsed since now. If, for example,
     *                          you want to enable your privilege for 10 minutes, set privilege_expire as 600(seconds).
     * @return The RTC token.
     */
    public String buildTokenWithUserAccount(String appId, String appCertificate, String channelName, String account, Role role, int token_expire, int privilege_expire) {
        AccessToken2 accessToken = new AccessToken2(appId, appCertificate, token_expire);
        AccessToken2.Service serviceRtc = new AccessToken2.ServiceRtc(channelName, account);

        serviceRtc.addPrivilegeRtc(AccessToken2.PrivilegeRtc.PRIVILEGE_JOIN_CHANNEL, privilege_expire);
        if (role == Role.ROLE_PUBLISHER) {
            serviceRtc.addPrivilegeRtc(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_AUDIO_STREAM, privilege_expire);
            serviceRtc.addPrivilegeRtc(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_VIDEO_STREAM, privilege_expire);
            serviceRtc.addPrivilegeRtc(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_DATA_STREAM, privilege_expire);
        }
        accessToken.addService(serviceRtc);

        try {
            return accessToken.build();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Generates an RTC token with the specified privilege.
     * <p>
     * This method supports generating a token with the following privileges:
     * - Joining an RTC channel.
     * - Publishing audio in an RTC channel.
     * - Publishing video in an RTC channel.
     * - Publishing data streams in an RTC channel.
     * <p>
     * The privileges for publishing audio, video, and data streams in an RTC channel apply only if you have
     * enabled co-host authentication.
     * <p>
     * A user can have multiple privileges. Each privilege is valid for a maximum of 24 hours.
     * The SDK triggers the onTokenPrivilegeWillExpire and onRequestToken callbacks when the token is about to expire
     * or has expired. The callbacks do not report the specific privilege affected, and you need to maintain
     * the respective timestamp for each privilege in your app logic. After receiving the callback, you need
     * to generate a new token, and then call renewToken to pass the new token to the SDK, or call joinChannel to re-join
     * the channel.
     *
     * @param appId                        The App ID of your Agora project.
     * @param appCertificate               The App Certificate of your Agora project.
     * @param channelName                  The unique channel name for the Agora RTC session in string format. The string length must be less than 64 bytes. The channel name may contain the following characters:
     *                                     - All lowercase English letters: a to z.
     *                                     - All uppercase English letters: A to Z.
     *                                     - All numeric characters: 0 to 9.
     *                                     - The space character.
     *                                     - "!", "#", "$", "%", "&", "(", ")", "+", "-", ":", ";", "<", "=", ".", ">", "?", "@", "[", "]", "^", "_", " {", "}", "|", "~", ",".
     * @param uid                          The user ID. A 32-bit unsigned integer with a value range from 1 to (2^32 - 1). It must be unique. Set uid as 0, if you do not want to authenticate the user ID, that is, any uid from the app client can join the channel.
     * @param tokenExpire                  represented by the number of seconds elapsed since now. If, for example, you want to access the
     *                                     Agora Service within 10 minutes after the token is generated, set tokenExpire as 600(seconds).
     * @param joinChannelPrivilegeExpire   The Unix timestamp when the privilege for joining the channel expires, represented
     *                                     by the sum of the current timestamp plus the valid time period of the token. For example, if you set joinChannelPrivilegeExpire as the
     *                                     current timestamp plus 600 seconds, the token expires in 10 minutes.
     * @param pubAudioPrivilegeExpire      The Unix timestamp when the privilege for publishing audio expires, represented
     *                                     by the sum of the current timestamp plus the valid time period of the token. For example, if you set pubAudioPrivilegeExpire as the
     *                                     current timestamp plus 600 seconds, the token expires in 10 minutes. If you do not want to enable this privilege,
     *                                     set pubAudioPrivilegeExpire as the current Unix timestamp.
     * @param pubVideoPrivilegeExpire      The Unix timestamp when the privilege for publishing video expires, represented
     *                                     by the sum of the current timestamp plus the valid time period of the token. For example, if you set pubVideoPrivilegeExpire as the
     *                                     current timestamp plus 600 seconds, the token expires in 10 minutes. If you do not want to enable this privilege,
     *                                     set pubVideoPrivilegeExpire as the current Unix timestamp.
     * @param pubDataStreamPrivilegeExpire The Unix timestamp when the privilege for publishing data streams expires, represented
     *                                     by the sum of the current timestamp plus the valid time period of the token. For example, if you set pubDataStreamPrivilegeExpire as the
     *                                     current timestamp plus 600 seconds, the token expires in 10 minutes. If you do not want to enable this privilege,
     *                                     set pubDataStreamPrivilegeExpire as the current Unix timestamp.
     * @note Agora recommends setting a reasonable timestamp for each privilege according to your scenario.
     * Suppose the expiration timestamp for joining the channel is set earlier than that for publishing audio.
     * When the token for joining the channel expires, the user is immediately kicked off the RTC channel
     * and cannot publish any audio stream, even though the timestamp for publishing audio has not expired.
     */
    public String buildTokenWithUid(String appId, String appCertificate, String channelName, int uid,
                                    int tokenExpire, int joinChannelPrivilegeExpire, int pubAudioPrivilegeExpire,
                                    int pubVideoPrivilegeExpire, int pubDataStreamPrivilegeExpire) {
        return buildTokenWithUserAccount(appId, appCertificate, channelName, AccessToken2.getUidStr(uid),
                tokenExpire, joinChannelPrivilegeExpire, pubAudioPrivilegeExpire, pubVideoPrivilegeExpire, pubDataStreamPrivilegeExpire);
    }

    /**
     * Generates an RTC token with the specified privilege.
     * <p>
     * This method supports generating a token with the following privileges:
     * - Joining an RTC channel.
     * - Publishing audio in an RTC channel.
     * - Publishing video in an RTC channel.
     * - Publishing data streams in an RTC channel.
     * <p>
     * The privileges for publishing audio, video, and data streams in an RTC channel apply only if you have
     * enabled co-host authentication.
     * <p>
     * A user can have multiple privileges. Each privilege is valid for a maximum of 24 hours.
     * The SDK triggers the onTokenPrivilegeWillExpire and onRequestToken callbacks when the token is about to expire
     * or has expired. The callbacks do not report the specific privilege affected, and you need to maintain
     * the respective timestamp for each privilege in your app logic. After receiving the callback, you need
     * to generate a new token, and then call renewToken to pass the new token to the SDK, or call joinChannel to re-join
     * the channel.
     *
     * @param appId                        The App ID of your Agora project.
     * @param appCertificate               The App Certificate of your Agora project.
     * @param channelName                  The unique channel name for the Agora RTC session in string format. The string length must be less than 64 bytes. The channel name may contain the following characters:
     *                                     - All lowercase English letters: a to z.
     *                                     - All uppercase English letters: A to Z.
     *                                     - All numeric characters: 0 to 9.
     *                                     - The space character.
     *                                     - "!", "#", "$", "%", "&", "(", ")", "+", "-", ":", ";", "<", "=", ".", ">", "?", "@", "[", "]", "^", "_", " {", "}", "|", "~", ",".
     * @param account                      The user account.
     * @param tokenExpire                  represented by the number of seconds elapsed since now. If, for example, you want to access the
     *                                     Agora Service within 10 minutes after the token is generated, set tokenExpire as 600(seconds).
     * @param joinChannelPrivilegeExpire   The Unix timestamp when the privilege for joining the channel expires, represented
     *                                     by the sum of the current timestamp plus the valid time period of the token. For example, if you set joinChannelPrivilegeExpire as the
     *                                     current timestamp plus 600 seconds, the token expires in 10 minutes.
     * @param pubAudioPrivilegeExpire      The Unix timestamp when the privilege for publishing audio expires, represented
     *                                     by the sum of the current timestamp plus the valid time period of the token. For example, if you set pubAudioPrivilegeExpire as the
     *                                     current timestamp plus 600 seconds, the token expires in 10 minutes. If you do not want to enable this privilege,
     *                                     set pubAudioPrivilegeExpire as the current Unix timestamp.
     * @param pubVideoPrivilegeExpire      The Unix timestamp when the privilege for publishing video expires, represented
     *                                     by the sum of the current timestamp plus the valid time period of the token. For example, if you set pubVideoPrivilegeExpire as the
     *                                     current timestamp plus 600 seconds, the token expires in 10 minutes. If you do not want to enable this privilege,
     *                                     set pubVideoPrivilegeExpire as the current Unix timestamp.
     * @param pubDataStreamPrivilegeExpire The Unix timestamp when the privilege for publishing data streams expires, represented
     *                                     by the sum of the current timestamp plus the valid time period of the token. For example, if you set pubDataStreamPrivilegeExpire as the
     *                                     current timestamp plus 600 seconds, the token expires in 10 minutes. If you do not want to enable this privilege,
     *                                     set pubDataStreamPrivilegeExpire as the current Unix timestamp.
     * @note Agora recommends setting a reasonable timestamp for each privilege according to your scenario.
     * Suppose the expiration timestamp for joining the channel is set earlier than that for publishing audio.
     * When the token for joining the channel expires, the user is immediately kicked off the RTC channel
     * and cannot publish any audio stream, even though the timestamp for publishing audio has not expired.
     */
    public String buildTokenWithUserAccount(String appId, String appCertificate, String channelName, String account,
                                            int tokenExpire, int joinChannelPrivilegeExpire, int pubAudioPrivilegeExpire,
                                            int pubVideoPrivilegeExpire, int pubDataStreamPrivilegeExpire) {
        AccessToken2 accessToken = new AccessToken2(appId, appCertificate, tokenExpire);
        AccessToken2.Service serviceRtc = new AccessToken2.ServiceRtc(channelName, account);

        serviceRtc.addPrivilegeRtc(AccessToken2.PrivilegeRtc.PRIVILEGE_JOIN_CHANNEL, joinChannelPrivilegeExpire);
        serviceRtc.addPrivilegeRtc(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_AUDIO_STREAM, pubAudioPrivilegeExpire);
        serviceRtc.addPrivilegeRtc(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_VIDEO_STREAM, pubVideoPrivilegeExpire);
        serviceRtc.addPrivilegeRtc(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_DATA_STREAM, pubDataStreamPrivilegeExpire);
        accessToken.addService(serviceRtc);

        try {
            return accessToken.build();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
