package org.jeecg.modules.im.io.agora.media;

import java.io.ByteArrayOutputStream;

public class DynamicKey3 {

    /**
     * Manipulate Agora dynamic key for media connection.
     *
     * @param appID   App ID assigned by Agora when register
     * @param appCertificate App Certificate assigned by Agora
     * @param channelName name of channel to join
     * @param unixTs      unix timestamp by seconds
     * @param randomInt   random uint32 salt for generating dynamic key
     * @return String representation of dynamic key to join Agora media server
     * @throws Exception if any error occurs
     */
    public static String generate(String appID, String appCertificate, String channelName, int unixTs, int randomInt, long uid, int expiredTs) throws Exception {
        String version = "003";
        String unixTsStr = ("0000000000" + Integer.toString(unixTs)).substring(Integer.toString(unixTs).length());
        String randomIntStr = ("00000000" + Integer.toHexString(randomInt)).substring(Integer.toHexString(randomInt).length());
        uid = uid & 0xFFFFFFFFL;
        String uidStr = ("0000000000" + Long.toString(uid)).substring(Long.toString(uid).length());
        String expiredTsStr = ("0000000000" + Integer.toString(expiredTs)).substring(Integer.toString(expiredTs).length());
        String signature = generateSignature3(appID, appCertificate, channelName, unixTsStr, randomIntStr, uidStr, expiredTsStr);
        return String.format("%s%s%s%s%s%s%s", version, signature, appID, unixTsStr, randomIntStr, uidStr, expiredTsStr);
    }

    private static String generateSignature3(String appID, String appCertificate, String channelName, String unixTsStr, String randomIntStr, String uidStr, String expiredTsStr) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(appID.getBytes());
        baos.write(unixTsStr.getBytes());
        baos.write(randomIntStr.getBytes());
        baos.write(channelName.getBytes());
        baos.write(uidStr.getBytes());
        baos.write(expiredTsStr.getBytes());
        byte[] sign = DynamicKeyUtil.encodeHMAC(appCertificate, baos.toByteArray());
        return DynamicKeyUtil.bytesToHex(sign);
    }
}
