package org.jeecg.modules.im.io.agora.sample;

import io.agora.media.DynamicKey5;
import org.apache.commons.codec.binary.Hex;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by liwei on 8/2/17.
 */
public class Verifier5 {
    public static void main(String[] args) throws Exception {
        if (args.length < 5) {
            System.out.println("java io.agora.media.sample.Verifier5 appID appCertificate channelName uid channelKey");
            return;
        }

        String appID = args[0];
        String appCertificate = args[1];
        String channelName = args[2];
        int uid = Integer.parseInt(args[3]);
        String channelKey = args[4];

        DynamicKey5 key5 = new DynamicKey5();
        if (! key5.fromString(channelKey)) {
            System.out.println("Faile to parse key");
            return ;
        }

        System.out.println("signature " + key5.content.signature);
        System.out.println("appID     " + new String(Hex.encodeHex(key5.content.appID, false)));
        System.out.println("unixTs    " + key5.content.unixTs);
        System.out.println("randomInt " + key5.content.salt);
        System.out.println("expiredTs " + key5.content.expiredTs);
        System.out.println("extra     [" + toString(key5.content.extra) + "]");
        System.out.println("service   " + key5.content.serviceType);

        System.out.println();
        System.out.println("Original \t\t " + channelKey);

        if (key5.content.serviceType == DynamicKey5.MEDIA_CHANNEL_SERVICE) {
            System.out.println("Uid = 0 \t\t " + DynamicKey5.generateMediaChannelKey(appID, appCertificate, channelName, key5.content.unixTs, key5.content.salt, 0, key5.content.expiredTs));
            System.out.println("Uid =  " + uid + " \t " + DynamicKey5.generateMediaChannelKey(appID, appCertificate, channelName, key5.content.unixTs, key5.content.salt, uid, key5.content.expiredTs));
        } else if (key5.content.serviceType == DynamicKey5.RECORDING_SERVICE) {
            System.out.println("Uid = 0 \t\t " + DynamicKey5.generateRecordingKey(appID, appCertificate, channelName, key5.content.unixTs, key5.content.salt, 0, key5.content.expiredTs));
            System.out.println("Uid =  " + uid + " \t " + DynamicKey5.generateRecordingKey(appID, appCertificate, channelName, key5.content.unixTs, key5.content.salt, uid, key5.content.expiredTs));
        } else if (key5.content.serviceType == DynamicKey5.IN_CHANNEL_PERMISSION) {
            String permission = key5.content.extra.get(DynamicKey5.ALLOW_UPLOAD_IN_CHANNEL);
            if (permission != DynamicKey5.noUpload && permission != DynamicKey5.audioVideoUpload) {
                System.out.println("Unknown in channel upload permission " + permission + " in extra [" + toString(key5.content.extra) + "]");
                return ;
            }
            System.out.println("Uid = 0 \t\t " + DynamicKey5.generateInChannelPermissionKey(appID, appCertificate, channelName, key5.content.unixTs, key5.content.salt, 0, key5.content.expiredTs, permission));
            System.out.println("Uid =  " + uid + " \t " + DynamicKey5.generateInChannelPermissionKey(appID, appCertificate, channelName, key5.content.unixTs, key5.content.salt, uid, key5.content.expiredTs, permission));
        } else {
            System.out.println("Unknown service type " + key5.content.serviceType);
        }

        String signature = DynamicKey5.generateSignature(appCertificate,
                key5.content.serviceType,
                appID,
                key5.content.unixTs,
                key5.content.salt,
                channelName,
                uid,
                0,
                key5.content.extra
        );
        System.out.println("generated signature " + signature);
    }

    private static String toString(TreeMap<Short, String> extra) {
        String s = "";

        String separator = "";

        for (Map.Entry<Short,String> v : extra.entrySet()) {
            s += separator;
            s += v.getKey();
            s += ":";
            s += v.getValue();
            separator = ", ";
        }

        return s;
    }


}
