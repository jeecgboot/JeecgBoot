package org.jeecg.modules.im.io.agora.media;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.TreeMap;

import static io.agora.media.Utils.crc32;

public class AccessToken {
    public enum Privileges {
        kJoinChannel(1),
        kPublishAudioStream(2),
        kPublishVideoStream(3),
        kPublishDataStream(4),
        
        // For RTM only
        kRtmLogin(1000);
    	
        public short intValue;

        Privileges(int value) {
            intValue = (short) value;
        }
    }

    private static final String VER = "006";
    
    public String appId;
    public String appCertificate;
    public String channelName;
    public String uid;
    public byte[] signature;
    public byte[] messageRawContent;
    public int crcChannelName;
    public int crcUid;
    public PrivilegeMessage message;
    public int expireTimestamp;

    public AccessToken(String appId, String appCertificate, String channelName, String uid) {
        this.appId = appId;
        this.appCertificate = appCertificate;
        this.channelName = channelName;
        this.uid = uid;
        this.crcChannelName = 0;
        this.crcUid = 0;
        this.message = new PrivilegeMessage();
    }

    public String build() throws Exception {
        if (! Utils.isUUID(appId)) {
            return "";
        }

        if (!Utils.isUUID(appCertificate)) {
            return "";
        }

        messageRawContent = Utils.pack(message);
        signature = generateSignature(appCertificate, 
        		appId, channelName, uid, messageRawContent);
        crcChannelName = crc32(channelName);
        crcUid = crc32(uid);

        PackContent packContent = new PackContent(signature, crcChannelName, crcUid, messageRawContent);
        byte[] content = Utils.pack(packContent);
        return getVersion() + this.appId + Utils.base64Encode(content);
    }

    public void addPrivilege(Privileges privilege, int expireTimestamp) {
        message.messages.put(privilege.intValue, expireTimestamp);
    }

    public static String getVersion() {
        return VER;
    }

    public static byte[] generateSignature(String appCertificate, 
    		String appID, String channelName, String uid, byte[] message) throws Exception {
    	
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(appID.getBytes());
            baos.write(channelName.getBytes());
            baos.write(uid.getBytes());
            baos.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Utils.hmacSign(appCertificate, baos.toByteArray());
    }

    public boolean fromString(String token) {
        if (!getVersion().equals(token.substring(0, Utils.VERSION_LENGTH))) {
            return false;
        }
        
        try {
            appId = token.substring(Utils.VERSION_LENGTH, Utils.VERSION_LENGTH + Utils.APP_ID_LENGTH);
            PackContent packContent = new PackContent();
            Utils.unpack(Utils.base64Decode(token.substring(Utils.VERSION_LENGTH + Utils.APP_ID_LENGTH, token.length())), packContent);
            signature = packContent.signature;
            crcChannelName = packContent.crcChannelName;
            crcUid = packContent.crcUid;
            messageRawContent = packContent.rawMessage;
            Utils.unpack(messageRawContent, message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    public class PrivilegeMessage implements PackableEx {
        public int salt;
        public int ts;
        public TreeMap<Short, Integer> messages;

        public PrivilegeMessage() {
            salt = Utils.randomInt();
            ts = Utils.getTimestamp() + 24 * 3600;
            messages = new TreeMap<>();
        }

        @Override
        public ByteBuf marshal(ByteBuf out) {
            return out.put(salt).put(ts).putIntMap(messages);
        }

        @Override
        public void unmarshal(ByteBuf in) {
            salt = in.readInt();
            ts = in.readInt();
            messages = in.readIntMap();
        }
    }

    public class PackContent implements PackableEx {
        public byte[] signature;
        public int crcChannelName;
        public int crcUid;
        public byte[] rawMessage;

        public PackContent() {
        	// Nothing done
        }
        
        public PackContent(byte[] signature, int crcChannelName, int crcUid, byte[] rawMessage) {
            this.signature = signature;
            this.crcChannelName = crcChannelName;
            this.crcUid = crcUid;
            this.rawMessage = rawMessage;
        }

        @Override
        public ByteBuf marshal(ByteBuf out) {
            return out.put(signature).put(crcChannelName).put(crcUid).put(rawMessage);
        }

        @Override
        public void unmarshal(ByteBuf in) {
            signature = in.readBytes();
            crcChannelName = in.readInt();
            crcUid = in.readInt();
            rawMessage = in.readBytes();
        }
    }
}
