package org.springframework.base.system.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class MacAddress {
    private static final Logger logger = LoggerFactory.getLogger(MacAddress.class);

    public static String getMac2() {
        String mac = "";
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                StringBuilder StringBuilder = new StringBuilder();
                NetworkInterface networkInterface = enumeration.nextElement();
                if (networkInterface != null) {
                    byte[] bytes = networkInterface.getHardwareAddress();
                    if (bytes != null) {
                        for (int i = 0; i < bytes.length; i++) {
                            if (i != 0) {
                                StringBuilder.append("T");
                            }
                            int tmp = bytes[i] & 0xFF;
                            String str = Integer.toHexString(tmp);
                            if (str.length() == 1) {
                                StringBuilder.append("0" + str);
                            } else {
                                StringBuilder.append(str);
                            }
                        }
                        mac = StringBuilder.toString().toUpperCase();
                    }
                }
                mac = mac.trim();
                if (!mac.equals("")) {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return mac;
    }

    public static void main(String[] args)
            throws Exception {
    }

    public static String getLocalMac() {
        StringBuilder sb = new StringBuilder();
        try {
            InetAddress ia = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                int temp = mac[i] & 0xFF;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0" + str);
                } else {
                    sb.append(str);
                }
            }
        } catch (Exception localException) {
        }
        String str = DigestUtils.md5Hex(sb.toString().toUpperCase());
        return str;
    }
}
