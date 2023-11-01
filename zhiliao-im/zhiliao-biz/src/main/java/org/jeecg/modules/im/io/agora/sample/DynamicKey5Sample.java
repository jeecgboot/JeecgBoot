package org.jeecg.modules.im.io.agora.sample;

import io.agora.media.DynamicKey5;

import java.util.Date;
import java.util.Random;

/**
 * Created by Li on 10/1/2016.
 */
public class DynamicKey5Sample {
    static String appID = "970ca35de60c44645bbae8a215061b33";
    static String appCertificate = "5cfd2fd1755d40ecb72977518be15d3b";
    static String channel = "7d72365eb983485397e3e3f9d460bdda";
    static int ts = (int)(new Date().getTime()/1000);
    static int r = new Random().nextInt();
    static long uid = 2882341273L;
    static int expiredTs = 0;

    public static void main(String[] args) throws Exception {
        System.out.println(DynamicKey5.generateMediaChannelKey(appID, appCertificate, channel, ts, r, uid, expiredTs));
        System.out.println(DynamicKey5.generateRecordingKey(appID, appCertificate, channel, ts, r, uid, expiredTs));
        System.out.println(DynamicKey5.generateInChannelPermissionKey(appID, appCertificate, channel, ts, r, uid, expiredTs, DynamicKey5.noUpload));
        System.out.println(DynamicKey5.generateInChannelPermissionKey(appID, appCertificate, channel, ts, r, uid, expiredTs, DynamicKey5.audioVideoUpload));
    }
}
