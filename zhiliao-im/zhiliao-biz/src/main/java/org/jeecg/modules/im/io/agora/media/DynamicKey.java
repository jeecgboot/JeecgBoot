package org.jeecg.modules.im.io.agora.media;

import java.io.ByteArrayOutputStream;

/**
 * Created by hefeng on 15/8/10.
 * Util to generate Agora media dynamic key.
 */
public class DynamicKey {
    /**
     * Generate Dynamic Key for media channel service
     * @param appID App ID assigned by Agora
     * @param appCertificate App Certificate assigned by Agora
     * @param channelName name of channel to join, limited to 64 bytes and should be printable ASCII characters
     * @param unixTs unix timestamp in seconds when generating the Dynamic Key
     * @param randomInt salt for generating dynamic key
     * @return String representation of dynamic key
     * @throws Exception
     */
    public static String generate(String appID, String appCertificate, String channelName, int unixTs, int randomInt) throws Exception {
        String unixTsStr = ("0000000000" + Integer.toString(unixTs)).substring(Integer.toString(unixTs).length());
        String randomIntStr = ("00000000" + Integer.toHexString(randomInt)).substring(Integer.toHexString(randomInt).length());
        String signature = generateSignature(appID, appCertificate, channelName, unixTsStr, randomIntStr);
        return String.format("%s%s%s%s", signature, appID, unixTsStr, randomIntStr);
    }

    private static String generateSignature(String appID, String appCertificate, String channelName, String unixTsStr, String randomIntStr) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(appID.getBytes());
        baos.write(unixTsStr.getBytes());
        baos.write(randomIntStr.getBytes());
        baos.write(channelName.getBytes());
        byte[] sign = DynamicKeyUtil.encodeHMAC(appCertificate, baos.toByteArray());
        return DynamicKeyUtil.bytesToHex(sign);
    }

}
