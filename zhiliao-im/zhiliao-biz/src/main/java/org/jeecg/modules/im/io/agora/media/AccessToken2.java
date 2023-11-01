package org.jeecg.modules.im.io.agora.media;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;
import java.util.TreeMap;

public class AccessToken2 {
    public enum PrivilegeRtc {
        PRIVILEGE_JOIN_CHANNEL(1),
        PRIVILEGE_PUBLISH_AUDIO_STREAM(2),
        PRIVILEGE_PUBLISH_VIDEO_STREAM(3),
        PRIVILEGE_PUBLISH_DATA_STREAM(4),
        ;

        public short intValue;

        PrivilegeRtc(int value) {
            intValue = (short) value;
        }
    }

    public enum PrivilegeRtm {
        PRIVILEGE_LOGIN(1),
        ;

        public short intValue;

        PrivilegeRtm(int value) {
            intValue = (short) value;
        }
    }

    public enum PrivilegeFpa {
        PRIVILEGE_LOGIN(1),
        ;

        public short intValue;

        PrivilegeFpa(int value) {
            intValue = (short) value;
        }
    }

    public enum PrivilegeChat {
        PRIVILEGE_CHAT_USER(1),
        PRIVILEGE_CHAT_APP(2),
        ;

        public short intValue;

        PrivilegeChat(int value) {
            intValue = (short) value;
        }
    }

    public enum PrivilegeEducation {
        PRIVILEGE_ROOM_USER(1),
        PRIVILEGE_USER(2),
        PRIVILEGE_APP(3),
        ;

        public short intValue;

        PrivilegeEducation(int value) {
            intValue = (short) value;
        }
    }

    private static final String VERSION = "007";
    public static final short SERVICE_TYPE_RTC = 1;
    public static final short SERVICE_TYPE_RTM = 2;
    public static final short SERVICE_TYPE_FPA = 4;
    public static final short SERVICE_TYPE_CHAT = 5;
    public static final short SERVICE_TYPE_EDUCATION = 7;

    public String appCert = "";
    public String appId = "";
    public int expire;
    public int issueTs;
    public int salt;
    public Map<Short, Service> services = new TreeMap<>();

    public AccessToken2() {
    }

    public AccessToken2(String appId, String appCert, int expire) {
        this.appCert = appCert;
        this.appId = appId;
        this.expire = expire;
        this.issueTs = Utils.getTimestamp();
        this.salt = Utils.randomInt();
    }

    public void addService(Service service) {
        this.services.put(service.getServiceType(), service);
    }

    public String build() throws Exception {
        if (!Utils.isUUID(this.appId) || !Utils.isUUID(this.appCert)) {
            return "";
        }

        ByteBuf buf = new ByteBuf().put(this.appId).put(this.issueTs).put(this.expire).put(this.salt).put((short) this.services.size());
        byte[] signing = getSign();

        this.services.forEach((k, v) -> {
            v.pack(buf);
        });

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(signing, "HmacSHA256"));
        byte[] signature = mac.doFinal(buf.asBytes());

        ByteBuf bufferContent = new ByteBuf();
        bufferContent.put(signature);
        bufferContent.buffer.put(buf.asBytes());

        return getVersion() + Utils.base64Encode(Utils.compress(bufferContent.asBytes()));
    }

    public Service getService(short serviceType) {
        if (serviceType == SERVICE_TYPE_RTC) {
            return new ServiceRtc();
        }
        if (serviceType == SERVICE_TYPE_RTM) {
            return new ServiceRtm();
        }
        if (serviceType == SERVICE_TYPE_FPA) {
            return new ServiceFpa();
        }
        if (serviceType == SERVICE_TYPE_CHAT) {
            return new ServiceChat();
        }
        if (serviceType == SERVICE_TYPE_EDUCATION) {
            return new ServiceEducation();
        }
        throw new IllegalArgumentException(String.format("unknown service type: `%d`", serviceType));
    }

    public byte[] getSign() throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(new ByteBuf().put(this.issueTs).asBytes(), "HmacSHA256"));
        byte[] signing = mac.doFinal(this.appCert.getBytes());
        mac.init(new SecretKeySpec(new ByteBuf().put(this.salt).asBytes(), "HmacSHA256"));
        return mac.doFinal(signing);
    }

    public static String getUidStr(int uid) {
        if (uid == 0) {
            return "";
        }
        return String.valueOf(uid & 0xFFFFFFFFL);
    }

    public static String getVersion() {
        return VERSION;
    }

    public boolean parse(String token) {
        if (!getVersion().equals(token.substring(0, Utils.VERSION_LENGTH))) {
            return false;
        }
        byte[] data = Utils.decompress(Utils.base64Decode(token.substring(Utils.VERSION_LENGTH)));
        ByteBuf buff = new ByteBuf(data);
        String signature = buff.readString();
        this.appId = buff.readString();
        this.issueTs = buff.readInt();
        this.expire = buff.readInt();
        this.salt = buff.readInt();
        short servicesNum = buff.readShort();

        for (int i = 0; i < servicesNum; i++) {
            short serviceType = buff.readShort();
            Service service = getService(serviceType);
            service.unpack(buff);
            this.services.put(serviceType, service);
        }
        return true;
    }

    public static class Service {
        public short type;
        public TreeMap<Short, Integer> privileges = new TreeMap<Short, Integer>() {
        };

        public Service() {
        }

        public Service(short serviceType) {
            this.type = serviceType;
        }

        public void addPrivilegeRtc(PrivilegeRtc privilege, int expire) {
            this.privileges.put(privilege.intValue, expire);
        }

        public void addPrivilegeRtm(PrivilegeRtm privilege, int expire) {
            this.privileges.put(privilege.intValue, expire);
        }

        public void addPrivilegeFpa(PrivilegeFpa privilege, int expire) {
            this.privileges.put(privilege.intValue, expire);
        }

        public void addPrivilegeChat(PrivilegeChat privilege, int expire) {
            this.privileges.put(privilege.intValue, expire);
        }

        public void addPrivilegeEducation(PrivilegeEducation privilege, int expire) {
            this.privileges.put(privilege.intValue, expire);
        }

        public TreeMap<Short, Integer> getPrivileges() {
            return this.privileges;
        }

        public short getServiceType() {
            return this.type;
        }

        public ByteBuf pack(ByteBuf buf) {
            return buf.put(this.type).putIntMap(this.privileges);
        }

        public void unpack(ByteBuf byteBuf) {
            this.privileges = byteBuf.readIntMap();
        }
    }

    public static class ServiceRtc extends Service {
        public String channelName;
        public String uid;

        public ServiceRtc() {
            this.type = SERVICE_TYPE_RTC;
        }

        public ServiceRtc(String channelName, String uid) {
            this.type = SERVICE_TYPE_RTC;
            this.channelName = channelName;
            this.uid = uid;
        }

        public String getChannelName() {
            return this.channelName;
        }

        public String getUid() {
            return this.uid;
        }

        public ByteBuf pack(ByteBuf buf) {
            return super.pack(buf).put(this.channelName).put(this.uid);
        }

        public void unpack(ByteBuf byteBuf) {
            super.unpack(byteBuf);
            this.channelName = byteBuf.readString();
            this.uid = byteBuf.readString();
        }
    }

    public static class ServiceRtm extends Service {
        public String userId;

        public ServiceRtm() {
            this.type = SERVICE_TYPE_RTM;
        }

        public ServiceRtm(String userId) {
            this.type = SERVICE_TYPE_RTM;
            this.userId = userId;
        }

        public String getUserId() {
            return this.userId;
        }

        public ByteBuf pack(ByteBuf buf) {
            return super.pack(buf).put(this.userId);
        }

        public void unpack(ByteBuf byteBuf) {
            super.unpack(byteBuf);
            this.userId = byteBuf.readString();
        }
    }

    public static class ServiceFpa extends Service {
        public ServiceFpa() {
            this.type = SERVICE_TYPE_FPA;
        }

        public ByteBuf pack(ByteBuf buf) {
            return super.pack(buf);
        }

        public void unpack(ByteBuf byteBuf) {
            super.unpack(byteBuf);
        }
    }

    public static class ServiceChat extends Service {
        public String userId;

        public ServiceChat() {
            this.type = SERVICE_TYPE_CHAT;
            this.userId = "";
        }

        public ServiceChat(String userId) {
            this.type = SERVICE_TYPE_CHAT;
            this.userId = userId;
        }

        public String getUserId() {
            return this.userId;
        }

        public ByteBuf pack(ByteBuf buf) {
            return super.pack(buf).put(this.userId);
        }

        public void unpack(ByteBuf byteBuf) {
            super.unpack(byteBuf);
            this.userId = byteBuf.readString();
        }
    }

    public static class ServiceEducation extends Service {
        public String roomUuid;
        public String userUuid;
        public Short role;

        public ServiceEducation() {
            this.type = SERVICE_TYPE_EDUCATION;
            this.roomUuid = "";
            this.userUuid = "";
            this.role = -1;
        }

        public ServiceEducation(String roomUuid, String userUuid, Short role) {
            this.type = SERVICE_TYPE_EDUCATION;
            this.roomUuid = roomUuid;
            this.userUuid = userUuid;
            this.role = role;
        }

        public ServiceEducation(String userUuid) {
            this.type = SERVICE_TYPE_EDUCATION;
            this.roomUuid = "";
            this.userUuid = userUuid;
            this.role = -1;
        }

        public String getRoomUuid() {
            return this.roomUuid;
        }

        public String getUserUuid() {
            return this.userUuid;
        }

        public Short getRole() {
            return this.role;
        }

        public ByteBuf pack(ByteBuf buf) {
            return super.pack(buf).put(this.roomUuid).put(this.userUuid).put(this.role);
        }

        public void unpack(ByteBuf byteBuf) {
            super.unpack(byteBuf);
            this.roomUuid = byteBuf.readString();
            this.userUuid = byteBuf.readString();
            this.role = byteBuf.readShort();
        }
    }
}
