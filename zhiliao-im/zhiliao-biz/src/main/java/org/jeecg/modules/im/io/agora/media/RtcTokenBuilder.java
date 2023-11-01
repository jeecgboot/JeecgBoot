package org.jeecg.modules.im.io.agora.media;

public class RtcTokenBuilder {
	public enum Role {
        /**
         * DEPRECATED. Role_Attendee has the same privileges as Role_Publisher.
         */
        Role_Attendee(0),
        /**
         *    RECOMMENDED. Use this role for a voice/video call or a live broadcast, if your scenario does not require authentication for [Hosting-in](https://docs.agora.io/en/Agora%20Platform/terms?platform=All%20Platforms#hosting-in).
         */
        Role_Publisher(1), 
        /**
         * Only use this role if your scenario require authentication for [Hosting-in](https://docs.agora.io/en/Agora%20Platform/terms?platform=All%20Platforms#hosting-in).
         *
         * @note In order for this role to take effect, please contact our support team to enable authentication for Hosting-in for you. Otherwise, Role_Subscriber still has the same privileges as Role_Publisher.
         */
        Role_Subscriber(2), 
        /**
         * DEPRECATED. Role_Attendee has the same privileges as Role_Publisher.
         */
        Role_Admin(101);

        public int initValue;

        Role(int initValue) {
            this.initValue = initValue;
        }
    }

    /**
     * Builds an RTC token using an int uid.
     *
     * @param appId The App ID issued to you by Agora. 
     * @param appCertificate Certificate of the application that you registered in 
     *        the Agora Dashboard. 
     * @param channelName The unique channel name for the AgoraRTC session in the string format. The string length must be less than 64 bytes. Supported character scopes are:
     * <ul>
     *    <li> The 26 lowercase English letters: a to z.</li>
     *    <li> The 26 uppercase English letters: A to Z.</li>
     *    <li> The 10 digits: 0 to 9.</li>
     *    <li> The space.</li>
     *    <li> "!", "#", "$", "%", "&", "(", ")", "+", "-", ":", ";", "<", "=", ".", ">", "?", "@", "[", "]", "^", "_", " {", "}", "|", "~", ",".
     * </ul>
     * @param uid  User ID. A 32-bit unsigned integer with a value ranging from 
     *        1 to (2^32-1).
     * @param role The user role.
     * <ul>
     *     <li> Role_Publisher = 1: RECOMMENDED. Use this role for a voice/video call or a live broadcast.</li>
     *     <li> Role_Subscriber = 2: ONLY use this role if your live-broadcast scenario requires authentication for [Hosting-in](https://docs.agora.io/en/Agora%20Platform/terms?platform=All%20Platforms#hosting-in). In order for this role to take effect, please contact our support team to enable authentication for Hosting-in for you. Otherwise, Role_Subscriber still has the same privileges as Role_Publisher.</li>
     * </ul>
     * @param privilegeTs Represented by the number of seconds elapsed since 1/1/1970.
     *        If, for example, you want to access the Agora Service within 10 minutes
     *        after the token is generated, set expireTimestamp as the current time stamp
     *        + 600 (seconds).                             
     */
    public String buildTokenWithUid(String appId, String appCertificate, 
    		String channelName, int uid, Role role, int privilegeTs) {
    	String account = uid == 0 ? "" : String.valueOf(uid);
    	return buildTokenWithUserAccount(appId, appCertificate, channelName, 
    			account, role, privilegeTs);
    }
    
    /**
     * Builds an RTC token using a string userAccount.
     * 
     * @param appId The App ID issued to you by Agora. 
     * @param appCertificate Certificate of the application that you registered in 
     *        the Agora Dashboard. 
     * @param channelName The unique channel name for the AgoraRTC session in the string format. The string length must be less than 64 bytes. Supported character scopes are:
     * <ul>
     *    <li> The 26 lowercase English letters: a to z.</li>
     *    <li> The 26 uppercase English letters: A to Z.</li>
     *    <li> The 10 digits: 0 to 9.</li>
     *    <li> The space.</li>
     *    <li> "!", "#", "$", "%", "&", "(", ")", "+", "-", ":", ";", "<", "=", ".", ">", "?", "@", "[", "]", "^", "_", " {", "}", "|", "~", ",".
     * </ul>
     * @param account  The user account.
     * @param role The user role.
     * <ul>
     *     <li> Role_Publisher = 1: RECOMMENDED. Use this role for a voice/video call or a live broadcast.</li>
     *     <li> Role_Subscriber = 2: ONLY use this role if your live-broadcast scenario requires authentication for [Hosting-in](https://docs.agora.io/en/Agora%20Platform/terms?platform=All%20Platforms#hosting-in). In order for this role to take effect, please contact our support team to enable authentication for Hosting-in for you. Otherwise, Role_Subscriber still has the same privileges as Role_Publisher.</li>
     * </ul>
     * @param privilegeTs represented by the number of seconds elapsed since 1/1/1970.
     *        If, for example, you want to access the Agora Service within 10 minutes
     *        after the token is generated, set expireTimestamp as the current time stamp
     *        + 600 (seconds).                             
     */
    public String buildTokenWithUserAccount(String appId, String appCertificate, 
    		String channelName, String account, Role role, int privilegeTs) {
    	
    	// Assign appropriate access privileges to each role.
    	AccessToken builder = new AccessToken(appId, appCertificate, channelName, account);
    	builder.addPrivilege(AccessToken.Privileges.kJoinChannel, privilegeTs);
    	if (role == Role.Role_Publisher || role == Role.Role_Subscriber || role == Role.Role_Admin) {
    		builder.addPrivilege(AccessToken.Privileges.kPublishAudioStream, privilegeTs);
    		builder.addPrivilege(AccessToken.Privileges.kPublishVideoStream, privilegeTs);
    		builder.addPrivilege(AccessToken.Privileges.kPublishDataStream, privilegeTs);
    	}
    	
    	try {
			return builder.build();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
    }
}
