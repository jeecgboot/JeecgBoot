package org.jeecg.modules.im.io.agora.media;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.util.TreeMap;

/**
 * Created by Li on 10/1/2016.
 */
public class DynamicKey5 {
    public final static String version = "005";
    public final static String noUpload = "0";
    public final static String audioVideoUpload = "3";

    // ServiceType
    public final static short MEDIA_CHANNEL_SERVICE = 1;
    public final static short RECORDING_SERVICE = 2;
    public final static short PUBLIC_SHARING_SERVICE = 3;
    public final static short IN_CHANNEL_PERMISSION = 4;

    // InChannelPermissionKey
    public final static short ALLOW_UPLOAD_IN_CHANNEL = 1;

    public DynamicKey5Content content;

    public boolean fromString(String key) {
        if (! key.substring(0, 3).equals(version)) {
            return false;
        }

        byte[] rawContent = new Base64().decode(key.substring(3));
        if (rawContent.length == 0) {
            return false;
        }

        content = new DynamicKey5Content();
        ByteBuf buffer = new ByteBuf(rawContent);
        content.unmarshall(buffer);
        return true;
    }

    public static String generateSignature(String appCertificate, short service, String appID, int unixTs, int salt, String channelName, long uid, int expiredTs, TreeMap<Short, String> extra) throws Exception {
        // decode hex to avoid case problem
        Hex hex = new Hex();
        byte[] rawAppID = hex.decode(appID.getBytes());
        byte[] rawAppCertificate = hex.decode(appCertificate.getBytes());

        Message m = new Message(service, rawAppID, unixTs, salt, channelName, (int)(uid & 0xFFFFFFFFL), expiredTs, extra);
        byte[] toSign = pack(m);
        return new String(Hex.encodeHex(DynamicKeyUtil.encodeHMAC(rawAppCertificate, toSign), false));
    }

    public static String generateDynamicKey(String appID, String appCertificate, String channel, int ts, int salt, long uid, int expiredTs, TreeMap<Short, String> extra, short service) throws Exception {
        String signature = generateSignature(appCertificate, service, appID, ts, salt, channel, uid, expiredTs, extra);
        DynamicKey5Content content = new DynamicKey5Content(service, signature, new Hex().decode(appID.getBytes()), ts, salt, expiredTs, extra);
        byte[] bytes = pack(content);
        byte[] encoded = new Base64().encode(bytes);
        String base64 = new String(encoded);
        return version + base64;
    }

    private static byte[] pack(Packable content) {
        ByteBuf buffer = new ByteBuf();
        content.marshal(buffer);
        return buffer.asBytes();
    }

    public static String generatePublicSharingKey(String appID, String appCertificate, String channel, int ts, int salt, long uid, int expiredTs) throws Exception {
        return generateDynamicKey(appID, appCertificate, channel, ts, salt, uid, expiredTs, new TreeMap<Short, String>(), PUBLIC_SHARING_SERVICE);
    }

    public static String generateRecordingKey(String appID, String appCertificate, String channel, int ts, int salt, long uid, int expiredTs) throws Exception {
        return generateDynamicKey(appID, appCertificate, channel, ts, salt, uid, expiredTs, new TreeMap<Short, String>(), RECORDING_SERVICE);
    }

    public static String generateMediaChannelKey(String appID, String appCertificate, String channel, int ts, int salt, long uid, int expiredTs) throws Exception {
        return generateDynamicKey(appID, appCertificate, channel, ts, salt, uid, expiredTs, new TreeMap<Short, String>(), MEDIA_CHANNEL_SERVICE);
    }

    public static String generateInChannelPermissionKey(String appID, String appCertificate, String channel, int ts, int salt, long uid, int expiredTs, String permission) throws Exception {
        TreeMap<Short, String> extra = new TreeMap<Short, String>();
        extra.put(ALLOW_UPLOAD_IN_CHANNEL, permission);
        return generateDynamicKey(appID, appCertificate, channel, ts, salt, uid, expiredTs, extra, IN_CHANNEL_PERMISSION);
    }

    static class Message implements Packable {
        public short serviceType;
        public byte[] appID;
        public int unixTs;
        public int salt;
        public String channelName;
        public int uid;
        public int expiredTs;
        public TreeMap<Short, String> extra;

        public Message(short serviceType, byte[] appID, int unixTs, int salt, String channelName, int uid, int expiredTs, TreeMap<Short, String> extra) {
            this.serviceType = serviceType;
            this.appID = appID;
            this.unixTs = unixTs;
            this.salt = salt;
            this.channelName = channelName;
            this.uid = uid;
            this.expiredTs = expiredTs;
            this.extra = extra;
        }

        public ByteBuf marshal(ByteBuf out) {
            return out.put(serviceType).put(appID).put(unixTs).put(salt).put(channelName).put(uid).put(expiredTs).put(extra);
        }
    }

    public static class DynamicKey5Content implements Packable {
        public short serviceType;
        public String signature;
        public byte[] appID;
        public int unixTs;
        public int salt;
        public int expiredTs;
        public TreeMap<Short, String> extra;

        public DynamicKey5Content() {
        }

        public DynamicKey5Content(short serviceType, String signature, byte[] appID, int unixTs, int salt, int expiredTs, TreeMap<Short, String> extra) {
            this.serviceType = serviceType;
            this.signature = signature;
            this.appID = appID;
            this.unixTs = unixTs;
            this.salt = salt;
            this.expiredTs = expiredTs;
            this.extra = extra;
        }

        public ByteBuf marshal(ByteBuf out) {
            return out.put(serviceType).put(signature).put(appID).put(unixTs).put(salt).put(expiredTs).put(extra);
        }

        public void unmarshall(ByteBuf in) {
            this.serviceType = in.readShort();
            this.signature = in.readString();
            this.appID = in.readBytes();
            this.unixTs = in.readInt();
            this.salt = in.readInt();
            this.expiredTs = in.readInt();
            this.extra = in.readMap();
        }
    }
}