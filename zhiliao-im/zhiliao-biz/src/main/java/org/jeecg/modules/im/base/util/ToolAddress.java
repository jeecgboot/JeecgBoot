package org.jeecg.modules.im.base.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class ToolAddress {
    public static final String IP = "ip";
    public static final String IP_INFO = "ip_info";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String NET = "net";
    public static Map<String,String> getIp(){
        Map map = new HashMap();
        try {
            URL url = new URL("http://2021.ip138.com/ic.asp");
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            map.put(IP,result.substring(result.indexOf("[")+1,result.indexOf("]")));
//            map.put(PROVINCE,result.substring(result.indexOf("来自：")+3,result.indexOf("省")));
//            map.put(CITY,result.substring(result.indexOf("省")+1,result.indexOf("市")));
//            map.put(NET,result.substring(result.indexOf("市")+2,result.indexOf("</center>")));
            map.put(IP_INFO,result.substring(result.indexOf("来自：")+3,result.indexOf("</center>")));

        } catch (IOException e) {
            return null;
        }
        return map;
    }
    public static void main(String[] args) {
        System.out.println(getIp());
    }
}
