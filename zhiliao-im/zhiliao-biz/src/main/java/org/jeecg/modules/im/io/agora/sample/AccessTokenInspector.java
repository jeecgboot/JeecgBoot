package org.jeecg.modules.im.io.agora.sample;


import org.jeecg.modules.im.io.agora.media.AccessToken2;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class AccessTokenInspector {
    private static String token = "007eJxTYBBbsMMnKq7p9Hf/HcIX5kce9b518kCiQgSr5Zrp4X1Tu6UUGCzNDZwdjU1TUs0Mkk1MzExMk5ISUy0SjQxNDcwMk4yN3b8IMEQwMTAwMoAwBIL4CgzmKeZGxmamqUmWFsYmFqbGluapxqnGaZYpJmYGSSkpiVwMRhYWRsYmhkbmxgDCaiTj";

    public static void main(String[] args) {
        AccessTokenInspector inspector = new AccessTokenInspector();
        inspector.inspect(token);
    }

    void inspect(String input) {
        AccessToken2 token = new AccessToken2();
        System.out.printf("parsing token: %s\n\n", input);
        token.parse(input);
        System.out.printf("appId:%s\n", token.appId);
        System.out.printf("appCert:%s\n", token.appCert);
        System.out.printf("salt:%d\n", token.salt);
        System.out.printf("issueTs:%d\n", token.issueTs);
        System.out.printf("expire:%d\n", token.expire);
        System.out.printf("services:\n");

        for (AccessToken2.Service service : token.services.values()) {
            System.out.printf("\t{%s}\n", toServiceStr(service));
        }
    }

    String toServiceStr(AccessToken2.Service service) {
        if (service.getServiceType() == AccessToken2.SERVICE_TYPE_RTC) {
            AccessToken2.ServiceRtc serviceRtc = (AccessToken2.ServiceRtc) service;
            return String.format("type:rtc, channel:%s, uid: %s, privileges: [%s]}", serviceRtc.getChannelName(),
                    serviceRtc.getUid(), toRtcPrivileges(serviceRtc.getPrivileges()));
        } else if (service.getServiceType() == AccessToken2.SERVICE_TYPE_RTM) {
            AccessToken2.ServiceRtm serviceRtm = (AccessToken2.ServiceRtm) service;
            return String.format("type:rtm, user_id:%s, privileges:[%s]", serviceRtm.getUserId(),
                    toRtmPrivileges(serviceRtm.getPrivileges()));
        } else if (service.getServiceType() == AccessToken2.SERVICE_TYPE_CHAT) {
            AccessToken2.ServiceChat serviceChat = (AccessToken2.ServiceChat) service;
            return String.format("type:chat, user_id:%s, privileges:[%s]", serviceChat.getUserId(),
                    toChatPrivileges(serviceChat.getPrivileges()));
        }
        return "unknown";
    }

    private String toRtcPrivileges(TreeMap<Short, Integer> privileges) {
        List<String> privilegeStrList = new ArrayList<>(privileges.size());
        if (privileges.containsKey(AccessToken2.PrivilegeRtc.PRIVILEGE_JOIN_CHANNEL.intValue)) {
            privilegeStrList.add(String.format("JOIN_CHANNEL(%d)",
                    privileges.get(AccessToken2.PrivilegeRtc.PRIVILEGE_JOIN_CHANNEL.intValue)));
        }
        if (privileges.containsKey(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_AUDIO_STREAM.intValue)) {
            privilegeStrList.add(String.format("PUBLISH_AUDIO_STREAM(%d)",
                    privileges.get(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_AUDIO_STREAM.intValue)));
        }
        if (privileges.containsKey(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_VIDEO_STREAM.intValue)) {
            privilegeStrList.add(String.format("PUBLISH_VIDEO_STREAM(%d)",
                    privileges.get(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_VIDEO_STREAM.intValue)));
        }
        if (privileges.containsKey(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_DATA_STREAM.intValue)) {
            privilegeStrList.add(String.format("PUBLISH_DATA_STREAM(%d)",
                    privileges.get(AccessToken2.PrivilegeRtc.PRIVILEGE_PUBLISH_DATA_STREAM.intValue)));
        }
        return String.join(",", privilegeStrList);
    }

    private String toRtmPrivileges(TreeMap<Short, Integer> privileges) {
        List<String> privilegeStrList = new ArrayList<>(privileges.size());
        if (privileges.containsKey(AccessToken2.PrivilegeRtm.PRIVILEGE_LOGIN.intValue)) {
            privilegeStrList.add(String.format("JOIN_LOGIN(%d)",
                    privileges.get(AccessToken2.PrivilegeRtm.PRIVILEGE_LOGIN.intValue)));
        }
        return String.join(",", privilegeStrList);
    }

    private String toChatPrivileges(TreeMap<Short, Integer> privileges) {
        List<String> privilegeStrList = new ArrayList<>(privileges.size());
        if (privileges.containsKey(AccessToken2.PrivilegeChat.PRIVILEGE_CHAT_USER.intValue)) {
            privilegeStrList.add(String.format("USER(%d)",
                    privileges.get(AccessToken2.PrivilegeChat.PRIVILEGE_CHAT_USER.intValue)));
        }
        if (privileges.containsKey(AccessToken2.PrivilegeChat.PRIVILEGE_CHAT_APP.intValue)) {
            privilegeStrList.add(String.format("APP(%d)",
                    privileges.get(AccessToken2.PrivilegeChat.PRIVILEGE_CHAT_APP.intValue)));
        }
        return String.join(",", privilegeStrList);
    }
}
