package org.jeecg.test.sqlparse;

import net.sf.jsqlparser.JSQLParserException;
import org.jeecg.common.util.IpUtils;
import org.jeecg.common.util.oConvertUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author: scott
 * @date: 2024年04月29日 16:48
 */
public class TestIpUtil {
    public static void main(String[] args) {
        Map<String, String[]> map = new HashMap<>();
        map.put("key1", new String[]{"value1", "value2", "value3"});
        map.put("key4", null);
        map.put("key2", new String[]{"value4", "value5"});
        map.put("key3", new String[]{"value6"});
        System.out.println(oConvertUtils.mapToString(map));
    }

    @Test
    public void test() {
        String ip = "2408:8207:1851:10e0:50bd:1a50:60c8:b030, 115.231.101.180";
        String[] ipAddresses = ip.split(",");
        for (String ipAddress : ipAddresses) {
            System.out.println(ipAddress);
            ipAddress = ipAddress.trim();
            if (IpUtils.isValidIpAddress(ipAddress)) {
                System.out.println("ipAddress= " + ipAddress);
            }
        }
    }
}
